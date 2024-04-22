package com.andrewmatheus.labpcp.service;

import com.andrewmatheus.labpcp.controller.dto.Request.CursoRequest;
import com.andrewmatheus.labpcp.datasource.entity.CursoEntity;
import com.andrewmatheus.labpcp.datasource.repository.CursoRepository;
import com.andrewmatheus.labpcp.exceptions.GenericException;
import com.andrewmatheus.labpcp.service.interfaces.CursoServiceI;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CursoService implements CursoServiceI {

    private final AuthService authService;
    private final CursoRepository cursoRepository;

    @Override
    public CursoEntity criarCurso(
            CursoRequest cursoRequest,
            String token
    ) {
        log.info("Iniciando verificação de autenticação!");
        String papelToken =  authService.buscaCampoToken(token, "scope");

        if (!papelToken.equalsIgnoreCase("ADM") &&
            !papelToken.equalsIgnoreCase("PEDAGOGICO")
        )  {
            log.error("Usuario não tem acesso a essa funcionalidade!");
            throw new GenericException("Usuario não tem acesso a essa funcionalidade", HttpStatus.UNAUTHORIZED);
        }

        if (Boolean.TRUE.equals(cursoExist(cursoRequest.nome()))) {
            log.error("Não permitido cadastrar curso com mesmo nome!");
            throw new GenericException("Não permitido cadastrar curso com mesmo nome!", HttpStatus.BAD_REQUEST);
        }

        CursoEntity novoCurso = new CursoEntity();
        novoCurso.setNome(cursoRequest.nome());

        log.info("Salvando novo curso!");
        cursoRepository.save(novoCurso);
        return novoCurso;
    }

    @Override
    public CursoEntity atualizaCursoPorId(
            Long id,
            CursoRequest cursoRequest,
            String token
    ) {
        log.info("Iniciando verificação de autenticação!");
        String papelToken =  authService.buscaCampoToken(token, "scope");

        if (!papelToken.equalsIgnoreCase("ADM") &&
                !papelToken.equalsIgnoreCase("PEDAGOGICO")
        )  {
            log.error("Usuario não tem acesso a essa funcionalidade!");
            throw new GenericException("Usuario não tem acesso a essa funcionalidade", HttpStatus.UNAUTHORIZED);
        }

        log.info("buscando curso por id...");
        Optional<CursoEntity> cursoBuscado = cursoRepository.findById(id);

        if (cursoBuscado.isEmpty()) {
            log.error("Curso não encontrado!");
            throw new GenericException("Curso não encontrado!", HttpStatus.NOT_FOUND);
        }

        CursoEntity cursoExistente = cursoBuscado.get();

        cursoExistente.setNome(cursoRequest.nome());

        log.info("Atualizando curso!");
        cursoRepository.save(cursoExistente);

        return cursoExistente;
    }

    @Override
    public CursoEntity buscaCursoPorId(Long id, String token) {
        log.info("Iniciando verificação de autenticação!");
        String papelToken =  authService.buscaCampoToken(token, "scope");

        if (!papelToken.equalsIgnoreCase("ADM") &&
                !papelToken.equalsIgnoreCase("PEDAGOGICO")
        )  {
            log.error("Usuario não tem acesso a essa funcionalidade!");
            throw new GenericException("Usuario não tem acesso a essa funcionalidade", HttpStatus.UNAUTHORIZED);
        }

        log.info("Buscando curso por ID!");
        Optional<CursoEntity> curso = cursoRepository.findById(id);

        if (curso.isEmpty()) {
            log.error("Curso não encontrado!");
            throw new GenericException("Curso não encontrado!", HttpStatus.NOT_FOUND);
        }

        return curso.get();
    }

    @Override
    public List<CursoEntity> buscaTodos(String token) {
        log.info("Iniciando verificação de autenticação!");
        String papelToken =  authService.buscaCampoToken(token, "scope");

        if (!papelToken.equalsIgnoreCase("ADM") &&
                !papelToken.equalsIgnoreCase("PEDAGOGICO")
        )  {
            log.error("Usuario não tem acesso a essa funcionalidade!");
            throw new GenericException("Usuario não tem acesso a essa funcionalidade", HttpStatus.UNAUTHORIZED);
        }

        log.info("Retornando lista de cursos!");
        return cursoRepository.findAll();
    }

    @Override
    public void removerCursoPorId(Long id, String token) {
        log.info("Iniciando verificação de autenticação!");
        String papelToken =  authService.buscaCampoToken(token, "scope");

        if (!papelToken.equalsIgnoreCase("ADM") &&
                !papelToken.equalsIgnoreCase("PEDAGOGICO")
        )  {
            log.error("Usuario não tem acesso a essa funcionalidade!");
            throw new GenericException("Usuario não tem acesso a essa funcionalidade", HttpStatus.UNAUTHORIZED);
        }

        log.info("Buscando curso por ID!");
        Optional<CursoEntity> curso = cursoRepository.findById(id);

        if (curso.isEmpty()) {
            log.error("Curso não encontrado!");
            throw new GenericException("Curso não encontrado!", HttpStatus.NOT_FOUND);
        }

        log.info("Removendo curso por ID!");
        cursoRepository.delete(curso.get());
    }

    private Boolean cursoExist(String nome) {
        log.info("Verificando se curso já está cadastrado!");
        Optional<CursoEntity> curso = cursoRepository.findByNome(nome);

        return curso.isPresent();
    }
}
