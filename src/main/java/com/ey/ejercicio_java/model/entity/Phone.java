package com.ey.ejercicio_java.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Entity
@Getter
@ToString
@Table(name = "phones")
public class Phone {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Setter private String number;
    @Setter private Integer cityCode;
    @Setter private Integer countryCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @Setter private User user;
}
