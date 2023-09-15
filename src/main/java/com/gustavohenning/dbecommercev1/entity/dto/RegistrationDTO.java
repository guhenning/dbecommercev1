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

    public String toString(){
        return "Registration info: username: " + this.username + " password: " + this.password;
    }
}