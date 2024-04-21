package com.andrewmatheus.labpcp.controller;

import com.andrewmatheus.labpcp.controller.dto.Request.LoginRequest;
import com.andrewmatheus.labpcp.controller.dto.Response.LoginResponse;
import com.andrewmatheus.labpcp.exceptions.LoginException;
import com.andrewmatheus.labpcp.service.interfaces.AuthServiceI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthServiceI authServiceI;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> token(@RequestBody LoginRequest loginRequest) throws Exception {
        try {
            LoginResponse response = authServiceI.gerarToken(loginRequest);
            if (response.status() == HttpStatus.OK.value()) {
                return ResponseEntity.ok(response);
            } else if (response.status() == HttpStatus.UNAUTHORIZED.value()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
        } catch (RuntimeException e) {
            throw new LoginException("Erro ao fazer login", e);
        }
    }
}
