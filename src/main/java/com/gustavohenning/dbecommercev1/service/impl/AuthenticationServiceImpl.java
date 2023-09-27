package com.gustavohenning.dbecommercev1.service.impl;

import com.google.gson.*;
import com.gustavohenning.dbecommercev1.entity.ApplicationUser;
import com.gustavohenning.dbecommercev1.entity.Cart;
import com.gustavohenning.dbecommercev1.entity.Role;
import com.gustavohenning.dbecommercev1.entity.dto.LoginResponseDTO;
import com.gustavohenning.dbecommercev1.entity.dto.UserDTO;
import com.gustavohenning.dbecommercev1.entity.exception.UsernameAlreadyExistsException;
import com.gustavohenning.dbecommercev1.repository.CartRepository;
import com.gustavohenning.dbecommercev1.repository.RoleRepository;
import com.gustavohenning.dbecommercev1.repository.UserRepository;
import com.gustavohenning.dbecommercev1.service.AuthenticationService;
import com.gustavohenning.dbecommercev1.service.CartService;
import com.gustavohenning.dbecommercev1.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URL;
import java.net.URLConnection;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class AuthenticationServiceImpl implements AuthenticationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private CartService cartService;

    @Autowired CartRepository cartRepository;

    public Optional<ApplicationUser> registerUser(String username, String password, String name, String surname, String document, String postalCode, String state, String city, String neighborhood, String street) throws Exception {

                Optional<ApplicationUser> existingUser = userRepository.findByUsername(username);

                if(existingUser.isPresent()) {
                    throw new UsernameAlreadyExistsException(username);
                }

                String encodedPassword = passwordEncoder.encode(password);
                Role userRole = roleRepository.findByAuthority("USER").get();

                Set<Role> authorities = new HashSet<>();

                authorities.add(userRole);

                Cart cart = new Cart();

                ApplicationUser userWithAddress = new ApplicationUser(0L, username, encodedPassword, name, surname, document, postalCode, state, city, neighborhood, street, cart, authorities);
                userWithAddress.setCart(cart);
                cartService.addCart(cart, userWithAddress);
                getAddress(userWithAddress);

                try {
                    getAddress(userWithAddress);
                } catch (IllegalArgumentException e) {
                    throw new IllegalArgumentException("Invalid postal code");
        }
                userRepository.save(userWithAddress);
                Long userId = userRepository.findByUsername(username).get().getUserId();
                cart.setUserId(userId);
        return userRepository.findByUsername(username);
    }

    public  ApplicationUser getAddress(ApplicationUser user) throws Exception {
        String postalCode = user.getPostalCode();

        if (postalCode.length() == 5) {
            // API Zippopotamus
            URL url = new URL("https://api.zippopotam.us/ES/" + postalCode);
            URLConnection connection = url.openConnection();
            InputStream is = connection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));

            StringBuilder jsonCep = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                jsonCep.append(line);
            }
            JsonElement rootElement = JsonParser.parseString(jsonCep.toString());
            JsonObject jsonObject = rootElement.getAsJsonObject();

            String city = jsonObject.getAsJsonArray("places").get(0).getAsJsonObject().get("place name").getAsString();
            String state = jsonObject.getAsJsonArray("places").get(0).getAsJsonObject().get("state").getAsString();

            user.setCity(city);
            user.setState(state);

            br.close();
            is.close();

        } else if (postalCode.length() == 8) {
            // API CEP
            URL url = new URL("https://brasilapi.com.br/api/cep/v1/" + postalCode);
            URLConnection connection = url.openConnection();
            InputStream is = connection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));

            String postCode = "";
            StringBuilder jsonPostalCode = new StringBuilder();

            while ((postCode = br.readLine()) != null) {
                jsonPostalCode.append(postCode);
            }
            Gson gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new JsonDeserializer<LocalDateTime>() {
                @Override
                public LocalDateTime deserialize(JsonElement json, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
                    return ZonedDateTime.parse(json.getAsJsonPrimitive().getAsString()).toLocalDateTime();
                }
            }).create();

            ApplicationUser userAux = gson.fromJson(jsonPostalCode.toString(), ApplicationUser.class);

            user.setPostalCode(postalCode);
            user.setState(userAux.getState());
            user.setCity(userAux.getCity());
            user.setNeighborhood(userAux.getNeighborhood());
            user.setStreet(userAux.getStreet());

            br.close();
            is.close();

        } else {
            throw new IllegalArgumentException("Invalid postal code only BR and ES available");
        }
        return user;
    }


    public LoginResponseDTO loginUser(String username, String password) {
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );

            String token = tokenService.generateJwt(auth);

            ApplicationUser user = userRepository.findByUsername(username).orElse(null);
            UserDTO userDTO = mapUserToUserDTO(user);

            return new LoginResponseDTO(userDTO, token);
        } catch (AuthenticationException e) {
            return new LoginResponseDTO(null, "");
        }
    }

    private UserDTO mapUserToUserDTO(ApplicationUser user) {
        if (user == null) {
            return null;
        }
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(user.getUsername());
        userDTO.setUserId(user.getUserId());

        return userDTO;
    }


}
