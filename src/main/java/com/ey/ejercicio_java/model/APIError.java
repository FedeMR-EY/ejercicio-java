package com.ey.ejercicio_java.model;

import lombok.*;

@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class APIError {
    private String mensaje;
}