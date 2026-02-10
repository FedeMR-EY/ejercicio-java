package com.ey.ejercicio_java.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@ToString(exclude = "accessToken")
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(generator = "UUID-USER", strategy = GenerationType.UUID)
    private UUID id;

    @Setter
    private String name;

    @Setter
    @Column(unique = true)
    private String email;

    @Setter
    private String password;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Setter
    private List<Phone> phones;

    @Setter
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Setter
    @Column(name = "modified_at")
    private LocalDateTime modifiedAt;

    @Setter
    @Column(name = "last_login")
    private LocalDateTime lastLogin;

    @Setter
    @Column(name = "is_active")
    private Boolean isActive = true;

    @Lob
    @Setter
    @Column(columnDefinition = "TEXT")
    @Basic(fetch = FetchType.EAGER)
    private String accessToken;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getUsername() {
        return this.email;
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
        return this.isActive != null && this.isActive;
    }
}
