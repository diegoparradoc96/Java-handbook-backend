package com.dipadev.personal_blog.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq")
    @SequenceGenerator(
            name = "user_seq",
            sequenceName = "user_sequence",
            allocationSize = 1
    )
    private Integer id;
    private String username;
    private String password;
    private Byte typeUser;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Article> articles;

    public User() {}

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public Integer getId() {
        return id;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Byte getTypeUser() {
        return typeUser;
    }

    public void setTypeUser(Byte typeUser) {
        this.typeUser = typeUser;
    }

    public List<Article> getArticles() {
        return articles;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String roleName = switch (this.typeUser) {
            case 1 -> "ROLE_ADMIN";
            case 2 -> "ROLE_USER";
            default -> "ROLE_USER";
        };
        return List.of(new SimpleGrantedAuthority(roleName));
    }

    // is recommended return tru for a typical development
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
