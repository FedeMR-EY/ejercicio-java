package com.ey.ejercicio_java.exception;

import com.ey.ejercicio_java.controller.AuthController;
import com.ey.ejercicio_java.model.APIError;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@ControllerAdvice(assignableTypes = AuthController.class)
public class AuthControllerAdvice extends ResponseEntityExceptionHandler {
    @Override
    protected @Nullable ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        //Crea un mapa para almacenar los errores de validación, donde la clave es el nombre del campo y el valor es el mensaje de error correspondiente.
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            errors.put(fieldName,error.getDefaultMessage());
        });

        //Muestra el campo que no cumple con los requisitos y el mensaje de error correspondiente en el log.
        log.error(errors.toString());

        //El formato de respuesta para las Exceptions es {"mensaje": "mensaje de error"} por lo que se usa un mensaje genérico.
        return ResponseEntity.badRequest().body(APIError.builder().mensaje("Algunos campos del formulario no cumplen con los requisitos necesarios").build());
    }
}
