package com.gustavohenning.dbecommercev1.service;

import com.gustavohenning.dbecommercev1.entity.ApplicationUser;
import com.gustavohenning.dbecommercev1.entity.dto.LoginResponseDTO;

import java.util.Optional;


public interface AuthenticationService {

    public Optional<ApplicationUser> registerUser(String username, String password, String name, String surname, String document, String postalCode, String state, String city, String neighborhood, String street) throws Exception;

    public  ApplicationUser getAddress(ApplicationUser user) throws Exception;

    public LoginResponseDTO loginUser(String username, String password);

}

