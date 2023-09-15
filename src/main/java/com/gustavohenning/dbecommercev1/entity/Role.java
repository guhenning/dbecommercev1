package com.gustavohenning.dbecommercev1.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

@Entity
@Table(name="roles")
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Role implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name="role_id")
    private Integer roleId;

    private String authority;



    public Role(String authority){
        this.authority = authority;
    }

}