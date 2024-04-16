package com.andrewmatheus.labpcp.service;

import com.andrewmatheus.labpcp.controller.dto.Request.CadastrarUsuarioRequest;
import com.andrewmatheus.labpcp.datasource.entity.PapelEntity;
import com.andrewmatheus.labpcp.datasource.entity.UsuarioEntity;
import com.andrewmatheus.labpcp.datasource.enums.Papeis;
import com.andrewmatheus.labpcp.datasource.repository.PapelRepository;
import com.andrewmatheus.labpcp.datasource.repository.UsuarioRepository;
import com.andrewmatheus.labpcp.exceptions.PapelNotFoundException;
import com.andrewmatheus.labpcp.exceptions.UsuarioAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final PapelRepository papelRepository;

    public void cadastraNovoUsuario(
            @RequestBody CadastrarUsuarioRequest cadastrarUsuarioRequest
    ) {
        boolean existeUsuario = usuarioRepository
                .findByLogin(cadastrarUsuarioRequest.login())
                .isPresent();

        if (existeUsuario) {
            throw new UsuarioAlreadyExistsException("Nome de usuário já foi cadastrado!");
        }

        Optional<PapelEntity> papelInformado = papelRepository.findByNome((Papeis) cadastrarUsuarioRequest.papel());

        if (papelInformado.isEmpty()) {
            throw new PapelNotFoundException("Papel informado não encontrado!");
        }

        UsuarioEntity novoUsuario = new UsuarioEntity();
        novoUsuario.setLogin(cadastrarUsuarioRequest.login());
        novoUsuario.setSenha(cadastrarUsuarioRequest.senha());
        novoUsuario.setPapel(papelInformado.get());

        usuarioRepository.save(novoUsuario);
    }
}
