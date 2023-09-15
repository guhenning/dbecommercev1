package com.gustavohenning.dbecommercev1.entity.dto;

import com.gustavohenning.dbecommercev1.entity.ApplicationUser;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponseDTO {
    private ApplicationUser user;
    private String jwt;

//    public LoginResponseDTO(){
//        super();
//    }
//
//    public LoginResponseDTO(ApplicationUser user, String jwt){
//        this.user = user;
//        this.jwt = jwt;
//    }
//
//    public void setUser(ApplicationUser user){
//        this.user = user;
//    }
//
//    public void setJwt(String jwt){
//        this.jwt = jwt;
//    }

}
