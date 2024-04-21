package com.andrewmatheus.labpcp.service;

import com.andrewmatheus.labpcp.controller.dto.Request.CadastrarUsuarioRequest;
import com.andrewmatheus.labpcp.datasource.entity.PapelEntity;
import com.andrewmatheus.labpcp.datasource.entity.UsuarioEntity;
import com.andrewmatheus.labpcp.datasource.repository.PapelRepository;
import com.andrewmatheus.labpcp.datasource.repository.UsuarioRepository;
import com.andrewmatheus.labpcp.exceptions.PapelNotFoundException;
import com.andrewmatheus.labpcp.exceptions.UsuarioAlreadyExistsException;
import com.andrewmatheus.labpcp.service.interfaces.AuthServiceI;
import com.andrewmatheus.labpcp.service.interfaces.UsuarioServiceI;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UsuarioService implements UsuarioServiceI {
    private final UsuarioRepository usuarioRepository;
    private final PapelRepository papelRepository;
    private final AuthServiceI authServiceI;
    private final BCryptPasswordEncoder senhaEncoder;

    public void cadastraNovoUsuario(
            @RequestBody CadastrarUsuarioRequest cadastrarUsuarioRequest,
            String token
    ) {
        log.info("Iniciando verificação de autenticação!");
        String papelToken =  authServiceI.buscaCampoToken(token, "scope");

        if (!papelToken.equalsIgnoreCase("ADM")){
            log.error("Usuario não tem acesso a essa funcionalidade!");
            throw new RuntimeException("Usuario não tem acesso a essa funcionalidade");
        }

        boolean existeUsuario = usuarioRepository
                .findByLogin(cadastrarUsuarioRequest.login())
                .isPresent();

        if (existeUsuario) {
            log.error("Nome de usuário já foi cadastrado!");
            throw new UsuarioAlreadyExistsException("Nome de usuário já foi cadastrado!");
        }

        Optional<PapelEntity> papelInformado = papelRepository.findByNome(cadastrarUsuarioRequest.papel());

        if (papelInformado.isEmpty()) {
            log.error("Papel informado não encontrado!");
            throw new PapelNotFoundException("Papel informado não encontrado!");
        }

        UsuarioEntity novoUsuario = new UsuarioEntity();
        novoUsuario.setLogin(cadastrarUsuarioRequest.login());
        novoUsuario.setSenha(senhaEncoder.encode(cadastrarUsuarioRequest.senha()));
        novoUsuario.setPapel(papelInformado.get());

        usuarioRepository.save(novoUsuario);
    }


}
