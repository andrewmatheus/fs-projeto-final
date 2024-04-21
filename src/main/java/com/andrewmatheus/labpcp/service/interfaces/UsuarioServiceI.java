package com.andrewmatheus.labpcp.service.interfaces;
import com.andrewmatheus.labpcp.controller.dto.Request.CadastrarUsuarioRequest;

public interface UsuarioServiceI {
    void cadastraNovoUsuario(CadastrarUsuarioRequest cadastrarUsuarioRequest, String token);
}
