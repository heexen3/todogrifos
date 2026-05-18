package com.todogrifos.authms.model;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "usuarios")
@Data
public class Usuario implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;
    private String password;
    private String role; // Ejemplo: ROLE_ADMIN

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Si el rol es nulo, Spring arrojará NullPointerException o IllegalArgumentException al crear el token
        if (this.role == null || this.role.trim().isEmpty()) {
            return List.of(new SimpleGrantedAuthority("USER"));
        }
        return List.of(new SimpleGrantedAuthority(this.role));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Debe ser true, de lo contrario lanza AccountExpiredException (Error 500)
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Debe ser true, de lo contrario lanza LockedException (Error 500)
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Debe ser true
    }

    @Override
    public boolean isEnabled() {
        return true; // Debe ser true, de lo contrario lanza DisabledException (Error 500)
    }
}