package com.andrewmatheus.labpcp.controller;

import com.andrewmatheus.labpcp.controller.dto.Request.LoginRequest;
import com.andrewmatheus.labpcp.controller.dto.Response.LoginResponse;
import com.andrewmatheus.labpcp.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity token(@RequestBody LoginRequest loginRequest) {
        try {
            LoginResponse response = authService.gerarToken(loginRequest);
            if (response.status() == HttpStatus.OK.value()) {
                return ResponseEntity.ok(response);
            } else if (response.status() == HttpStatus.UNAUTHORIZED.value()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido, faça login novamente!");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Verifique os dados informados!");
            }
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao fazer login");
        }
    }
}
