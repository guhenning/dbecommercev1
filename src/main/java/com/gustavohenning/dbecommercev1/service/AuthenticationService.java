package com.gustavohenning.dbecommercev1.service;

import com.google.gson.*;
import com.gustavohenning.dbecommercev1.entity.ApplicationUser;
import com.gustavohenning.dbecommercev1.entity.Role;
import com.gustavohenning.dbecommercev1.entity.dto.LoginResponseDTO;
import com.gustavohenning.dbecommercev1.repository.RoleRepository;
import com.gustavohenning.dbecommercev1.repository.UserRepository;
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
import java.util.Set;

@Service
@Transactional
public class AuthenticationService {

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

    public ApplicationUser registerUser(String username, String password, String name, String email, String postalCode, String state, String city, String neighborhood, String street) throws Exception {

        String encodedPassword = passwordEncoder.encode(password);
        Role userRole = roleRepository.findByAuthority("USER").get();

        Set<Role> authorities = new HashSet<>();

        authorities.add(userRole);

        ApplicationUser userWithAddress = new ApplicationUser(0, username, encodedPassword, name, email, postalCode, state, city, neighborhood, street, authorities);
        getAddress(userWithAddress);

        try {
            getAddress(userWithAddress);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid postal code");
        }

        return userRepository.save(userWithAddress);
    }

    public  ApplicationUser getAddress(ApplicationUser user) throws Exception {
        String postalCode = user.getPostalCode();

        if (postalCode.length() == 8) {
            // API CEP
            URL url = new URL("https://brasilapi.com.br/api/cep/v1/" + user.getPostalCode());
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
            throw new IllegalArgumentException("Invalid postal code");
        }
        return user;
    }


    public LoginResponseDTO loginUser(String username, String password){

        try{
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );

            String token = tokenService.generateJwt(auth);

            return new LoginResponseDTO(userRepository.findByUsername(username).get(), token);

        } catch(AuthenticationException e){
            return new LoginResponseDTO(null, "");
        }
    }

}