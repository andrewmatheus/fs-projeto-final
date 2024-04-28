package com.andrewmatheus.labpcp.exceptions.response;

import com.andrewmatheus.labpcp.exceptions.LoginException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

// Se der tempo utilizar
@ControllerAdvice
public class ExceptionResponse {
    @ExceptionHandler(value = {LoginException.class})
    protected ResponseEntity<?> handleExceptionBadRequest(LoginException ex, WebRequest webRequest) {
        Map<String, Object> body = new HashMap<>();
        HttpStatus status = HttpStatus.BAD_REQUEST;
        body.put("Status", status);
        body.put("Mensagem", ex.getMessage());
        body.put("Data", LocalDateTime.now());
        body.put("Causa", webRequest);

        return new ResponseEntity<>(body, status);
    }
}
