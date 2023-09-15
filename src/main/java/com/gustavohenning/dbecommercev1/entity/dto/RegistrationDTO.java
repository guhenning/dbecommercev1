package com.gustavohenning.dbecommercev1.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RegistrationDTO {

    private String username;
    private String password;
    private String name;
    private String email;
    private String postalCode;
    private String state;
    private String city;
    private String neighborhood;
    private String street;

    public String toString(){
        return "Registration info: username: " + this.username +
                " password: " + this.password +
                " name: " + this.name +
                " email: " + this.email +
                " postalCode: " + this.postalCode +
                " state: " + this.state +
                " city: " + this.city +
                " neighborhood: " + this.neighborhood +
                " street: " + this.street;
    }
}