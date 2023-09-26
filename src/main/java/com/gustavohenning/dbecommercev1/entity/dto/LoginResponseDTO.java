package com.gustavohenning.dbecommercev1.entity.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponseDTO {

    private UserDTO user;
    private String jwt;
}
