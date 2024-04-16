package com.andrewmatheus.labpcp.controller;

import com.andrewmatheus.labpcp.controller.dto.Request.CadastrarUsuarioRequest;
import com.andrewmatheus.labpcp.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UsuarioController {
    private final UsuarioService usuarioService;

    @PostMapping("cadastro")
    public ResponseEntity<String> newUser(@RequestBody CadastrarUsuarioRequest cadastrarUsuarioRequest) {

        usuarioService.cadastraNovoUsuario(cadastrarUsuarioRequest);

        return new ResponseEntity<>("Usuário" + cadastrarUsuarioRequest.login() + "criado com sucesso!", HttpStatus.CREATED);
    }
}
