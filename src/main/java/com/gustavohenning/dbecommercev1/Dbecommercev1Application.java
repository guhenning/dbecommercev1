package com.gustavohenning.dbecommercev1;

import com.gustavohenning.dbecommercev1.entity.ApplicationUser;
import com.gustavohenning.dbecommercev1.entity.Role;
import com.gustavohenning.dbecommercev1.repository.RoleRepository;
import com.gustavohenning.dbecommercev1.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
public class Dbecommercev1Application {

	public static void main(String[] args) {
		SpringApplication.run(Dbecommercev1Application.class, args);
	}

	@Bean
	CommandLineRunner run(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncode){
		return args ->{
			if(roleRepository.findByAuthority("ADMIN").isPresent()) return;
			Role adminRole = roleRepository.save(new Role("ADMIN"));
			roleRepository.save(new Role("USER"));

			Set<Role> roles = new HashSet<>();
			roles.add(adminRole);

			ApplicationUser admin = new ApplicationUser(1L, "admin", passwordEncode.encode("password"), "null", "null", "null", "null", "null", "null", "null", roles);

			userRepository.save(admin);
		};
	}

}
