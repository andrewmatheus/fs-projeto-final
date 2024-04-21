package com.andrewmatheus.labpcp.service;

import com.andrewmatheus.labpcp.controller.dto.Request.CadastrarDocenteRequest;
import com.andrewmatheus.labpcp.datasource.entity.DocenteEntity;
import com.andrewmatheus.labpcp.datasource.entity.UsuarioEntity;
import com.andrewmatheus.labpcp.datasource.repository.DocenteRepository;
import com.andrewmatheus.labpcp.datasource.repository.UsuarioRepository;
import com.andrewmatheus.labpcp.exceptions.GenericException;
import com.andrewmatheus.labpcp.service.interfaces.DocenteServiceI;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class DocenteService implements DocenteServiceI {

    private final AuthService authService;
    private final DocenteRepository docenteRepository;
    private final UsuarioRepository usuarioRepository;

    @Override
    public DocenteEntity criarDocente(
            @RequestBody CadastrarDocenteRequest cadastrarDocenteRequest,
            String token
    ) {
        log.info("Iniciando verificação de autenticação!");
        String papelToken =  authService.buscaCampoToken(token, "scope");
        Long usuarioId = Long.valueOf(authService.buscaCampoUserIdToken(token));

        if (!papelToken.equalsIgnoreCase("ADM") &&
            !papelToken.equalsIgnoreCase("PEDAGOGICO") &&
            !papelToken.equalsIgnoreCase("RECRUITER")
        )  {
            log.error("Usuario não tem acesso a essa funcionalidade!");
            throw new RuntimeException("Usuario não tem acesso a essa funcionalidade");
        }

        log.info("buscando usuario da autenticação!");
        Optional<UsuarioEntity> usuario = usuarioRepository.findById(usuarioId);

        if (usuario.isEmpty()) {
            log.error("Usuario não encontrado!");
            throw new RuntimeException("Usuario não encontrado!");
        }

        if (Boolean.TRUE.equals(docenteExist(cadastrarDocenteRequest.nome()))) {
            log.error("Não permitido cadastrar docentes com mesmo nome!");
            throw new RuntimeException("Não permitido cadastrar docentes com mesmo nome!");
        }

        DocenteEntity novoDocente = new DocenteEntity();
        novoDocente.setNome(cadastrarDocenteRequest.nome());
        novoDocente.setDataEntrada(cadastrarDocenteRequest.dataEntrada());
        novoDocente.setUsuario(usuario.get());
        log.info("Salvando novo docente!");
        docenteRepository.save(novoDocente);

        return novoDocente;
    }

    public DocenteEntity atualizaDocentePorId(
            Long id,
            @RequestBody CadastrarDocenteRequest cadastrarDocenteRequest,
            String token
    ) {
        log.info("Iniciando verificação de autenticação!");
        String papelToken =  authService.buscaCampoToken(token, "scope");

        if (!papelToken.equalsIgnoreCase("ADM") &&
                !papelToken.equalsIgnoreCase("PEDAGOGICO") &&
                !papelToken.equalsIgnoreCase("RECRUITER")
        )  {
            log.error("Usuario não tem acesso a essa funcionalidade!");
            throw new RuntimeException("Usuario não tem acesso a essa funcionalidade");
        }

        log.info("buscando docente...");
        Optional<DocenteEntity> docenteBuscado = docenteRepository.findById(id);

        if (docenteBuscado.isEmpty()) {
            log.error("Docente não encontrado!");
            throw new GenericException("Docente não encontrado!", HttpStatus.NOT_FOUND);
        }

        DocenteEntity docenteExistente = docenteBuscado.get();

        docenteExistente.setNome(cadastrarDocenteRequest.nome());
        docenteExistente.setDataEntrada(cadastrarDocenteRequest.dataEntrada());

        log.info("Atualizando docente!");
        docenteRepository.save(docenteExistente);

        return docenteExistente;
    }

    public List<DocenteEntity> buscaTodos(String token) {
        log.info("Iniciando verificação de autenticação!");
        String papelToken =  authService.buscaCampoToken(token, "scope");

        if (!papelToken.equalsIgnoreCase("ADM") &&
                !papelToken.equalsIgnoreCase("PEDAGOGICO") &&
                !papelToken.equalsIgnoreCase("RECRUITER")
        )  {
            log.error("Usuario não tem acesso a essa funcionalidade!");
            throw new GenericException("Usuario não tem acesso a essa funcionalidade", HttpStatus.UNAUTHORIZED);
        }
        log.info("Retornando lista de docentes!");
        return docenteRepository.findAll();
    }

    public DocenteEntity buscaDocentePorId(Long id, String token) {
        log.info("Iniciando verificação de autenticação!");
        String papelToken =  authService.buscaCampoToken(token, "scope");

        if (!papelToken.equalsIgnoreCase("ADM") &&
                !papelToken.equalsIgnoreCase("PEDAGOGICO") &&
                !papelToken.equalsIgnoreCase("RECRUITER")
        )  {
            log.error("Usuario não tem acesso a essa funcionalidade!");
            throw new GenericException("Usuario não tem acesso a essa funcionalidade", HttpStatus.UNAUTHORIZED);
        }

        log.info("Buscando docente por ID!");
        Optional<DocenteEntity> docente = docenteRepository.findById(id);

        if (docente.isEmpty()) {
            log.error("Docente não encontrado!");
            throw new GenericException("Docente não encontrado!", HttpStatus.NOT_FOUND);
        }

        return docente.get();
    }

    public void removerDocentePorId(Long id, String token) {
        log.info("Iniciando verificação de autenticação!");
        String papelToken =  authService.buscaCampoToken(token, "scope");

        if (!papelToken.equalsIgnoreCase("ADM"))  {
            log.error("Usuario não tem acesso a essa funcionalidade!");
            throw new GenericException("Usuario não tem acesso a essa funcionalidade", HttpStatus.UNAUTHORIZED);
        }

        log.info("Buscando docente por ID!");
        Optional<DocenteEntity> docente = docenteRepository.findById(id);

        if (docente.isEmpty()) {
            log.error("Docente não encontrado!");
            throw new GenericException("Docente não encontrado!", HttpStatus.NOT_FOUND);
        }

        log.info("Removendo docente por ID!");
        docenteRepository.delete(docente.get());
    }

    private Boolean docenteExist(String nome) {
        log.info("Verificando se docente já está cadastrado!");
        Optional<DocenteEntity> docente = docenteRepository.findByNome(nome);

        return docente.isPresent();
    }

}
