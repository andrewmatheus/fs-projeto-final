package com.andrewmatheus.labpcp.controller;

import com.andrewmatheus.labpcp.controller.dto.Request.CadastrarUsuarioRequest;
import com.andrewmatheus.labpcp.controller.dto.Response.CadastrarUsuarioResponse;
import com.andrewmatheus.labpcp.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UsuarioController {
    private final UsuarioService usuarioService;

    @PostMapping("/cadastro")
    public ResponseEntity novoUsuario(
        @RequestHeader(name = "Authorization") String token,
        @RequestBody CadastrarUsuarioRequest cadastrarUsuarioRequest
    ) {
        try {
            usuarioService.cadastraNovoUsuario(cadastrarUsuarioRequest, token.substring(7));

            return ResponseEntity.status(HttpStatus.CREATED).body(new CadastrarUsuarioResponse(cadastrarUsuarioRequest.login(), cadastrarUsuarioRequest.papel(), "Usuário cadastrado com sucesso!"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Dados ausentes ou incorretos!");
        }
    }
}
