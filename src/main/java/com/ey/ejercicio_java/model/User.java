package com.ey.ejercicio_java.model;


import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Entity
@Getter
@ToString
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(generator = "UUID-USER", strategy = GenerationType.UUID)
    private UUID id;
    @Setter private String name;
    @Setter private String email;
    @Setter private String password;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Setter private List<Phone> phones;
}
