package com.andrewmatheus.labpcp.infra.security;

import com.andrewmatheus.labpcp.datasource.entity.PapelEntity;
import com.andrewmatheus.labpcp.datasource.entity.UsuarioEntity;
import com.andrewmatheus.labpcp.datasource.enums.Papeis;
import com.andrewmatheus.labpcp.datasource.repository.PapelRepository;
import com.andrewmatheus.labpcp.datasource.repository.UsuarioRepository;
import com.andrewmatheus.labpcp.exceptions.PapelNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.boot.CommandLineRunner;

import java.util.Optional;

@Component
public class AdminUserConfig implements CommandLineRunner {

    private final PapelRepository papelRepository;
    private final UsuarioRepository usuarioRepository;
    private final BCryptPasswordEncoder senhaEncoder;

    public AdminUserConfig(PapelRepository papelRepository,
                           UsuarioRepository usuarioRepository,
                           BCryptPasswordEncoder senhaEncoder) {
        this.papelRepository = papelRepository;
        this.usuarioRepository = usuarioRepository;
        this.senhaEncoder = senhaEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {

        Optional<PapelEntity> papelAdmin = papelRepository.findByNome(Papeis.ADM);

        if (papelAdmin.isEmpty()) {
            throw new PapelNotFoundException("Papel informado não encontrado!");
        }

        Optional<UsuarioEntity> usuarioAdmin = usuarioRepository.findByLogin("administrador");

        usuarioAdmin.ifPresentOrElse(
            user -> {
                System.out.println("usuario administrador já existe");
            },
            () -> {
                UsuarioEntity usuario = new UsuarioEntity();
                usuario.setLogin("administrador");
                usuario.setSenha(senhaEncoder.encode("123456"));
                usuario.setPapel(papelAdmin.get());
                usuarioRepository.save(usuario);
            }
        );
    }
}
