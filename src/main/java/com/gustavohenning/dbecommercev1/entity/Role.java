package com.gustavohenning.dbecommercev1.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
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

//    public Role(){
//        super();
//    }

    public Role(String authority){
        this.authority = authority;
    }

//    public Role(Integer roleId, String authority){
//        this.roleId = roleId;
//        this.authority = authority;
//    }

//    @Override
//    public String getAuthority() {
//        return this.authority;
//    }

//    public void setAuthority(String authority){
//        this.authority = authority;
//    }
//
//    public Integer getRoleId(){
//        return this.roleId;
//    }
//
//    public void setRoleId(Integer roleId){
//        this.roleId = roleId;
//    }
}