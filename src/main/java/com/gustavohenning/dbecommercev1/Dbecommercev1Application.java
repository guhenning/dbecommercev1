package com.gustavohenning.dbecommercev1;

import com.gustavohenning.dbecommercev1.entity.ApplicationUser;
import com.gustavohenning.dbecommercev1.entity.Cart;
import com.gustavohenning.dbecommercev1.entity.Role;
import com.gustavohenning.dbecommercev1.repository.CartRepository;
import com.gustavohenning.dbecommercev1.repository.RoleRepository;
import com.gustavohenning.dbecommercev1.repository.UserRepository;
import com.gustavohenning.dbecommercev1.service.CartService;
import com.gustavohenning.dbecommercev1.service.EmailSenderService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
public class Dbecommercev1Application {



	public static void main(String[] args) {
		SpringApplication.run(Dbecommercev1Application.class, args);
	}




	@Bean
	CommandLineRunner run(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncode, CartService cartService, CartRepository cartRepository){
		return args ->{
			if(roleRepository.findByAuthority("ADMIN").isPresent()) return;
			Role adminRole = roleRepository.save(new Role("ADMIN"));
			roleRepository.save(new Role("USER"));

			Set<Role> roles = new HashSet<>();
			roles.add(adminRole);
			Cart cart = new Cart();

			ApplicationUser admin = new ApplicationUser(1L, "admin@hotmail.com", passwordEncode.encode("password"), "null", "null", "null", "null", "null", "null", "null", "null", cart, roles);

			cartService.addCart(cart, admin);
			admin.setCart(cart);
			cart.setUserId(admin.getUserId());
			cartRepository.save(cart);


			userRepository.save(admin);
		};
	}
}
