package com.gustavohenning.dbecommercev1.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;

@Getter
@Entity
@Table(name="users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationUser implements UserDetails {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long userId;
    @Column(unique=true)
    private String username;
    private String password;
    private String name;
    private String email;
    private String postalCode;
    private String state;
    private String city;
    private String neighborhood;
    private String street;


    @ManyToMany(fetch=FetchType.EAGER)
    @JoinTable(
            name="user_role_junction",
            joinColumns = {@JoinColumn(name="user_id")},
            inverseJoinColumns = {@JoinColumn(name="role_id")}
    )
    private Set<Role> authorities;


    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "cart_id")
    @JsonIgnore
    private Cart cart;



    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }


}