package com.ey.ejercicio_java.exception;

import com.ey.ejercicio_java.model.APIError;

public class APIException extends RuntimeException {

    private final APIError apiError;

    public APIException(APIError apiError) {
        super(apiError.getMensaje());
        this.apiError = apiError;
    }
    public APIException(APIError apiError, Throwable cause) {
        super(apiError.getMensaje(),cause);
        this.apiError = apiError;
    }
}
