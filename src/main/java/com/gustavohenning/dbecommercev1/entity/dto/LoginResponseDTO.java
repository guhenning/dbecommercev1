package com.gustavohenning.dbecommercev1.entity.dto;

import com.gustavohenning.dbecommercev1.entity.ApplicationUser;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponseDTO {


    private UserDto user;
    private String jwt;



}
