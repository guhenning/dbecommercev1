package com.gustavohenning.dbecommercev1.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
@CrossOrigin("*")
public class AdminController {

    @GetMapping("/")
    @Operation(summary = "Test Admin Access Level", description = "Admin Access Level Test")
    public String helloAdminController(){
        return "Admin level access";
    }

}