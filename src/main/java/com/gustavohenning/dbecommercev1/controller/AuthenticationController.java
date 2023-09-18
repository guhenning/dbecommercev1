package com.gustavohenning.dbecommercev1.controller;

import com.gustavohenning.dbecommercev1.entity.ApplicationUser;
import com.gustavohenning.dbecommercev1.entity.dto.LoginResponseDTO;
import com.gustavohenning.dbecommercev1.entity.dto.RegistrationDTO;
import com.gustavohenning.dbecommercev1.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin("*")
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/register")
    @Operation(summary = "Registry User", description = "Registry New User")
    public ApplicationUser registerUser(@RequestBody RegistrationDTO body) throws Exception {
        return authenticationService.registerUser(body.getUsername(), body.getPassword(), body.getName(), body.getEmail(), body.getPostalCode(), body.getState(), body.getCity(), body.getNeighborhood(), body.getStreet());
    }

    @PostMapping("/login")
    @Operation(summary = "Login User", description = "Login User and Show JWT")
    public LoginResponseDTO loginUser(@RequestBody RegistrationDTO body){
        return authenticationService.loginUser(body.getUsername(), body.getPassword());
    }
}