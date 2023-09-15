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

//    public RegistrationDTO(){
//        super();
//    }
//
//    public RegistrationDTO(String username, String password){
//        super();
//        this.username = username;
//        this.password = password;
//    }
//
//    public String getUsername(){
//        return this.username;
//    }
//
//    public void setUsername(String username){
//        this.username = username;
//    }
//
//    public String getPassword(){
//        return this.password;
//    }
//
//    public void setPassword(String password){
//        this.password = password;
//    }

    public String toString(){
        return "Registration info: username: " + this.username + " password: " + this.password;
    }
}