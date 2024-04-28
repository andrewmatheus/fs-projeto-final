package com.andrewmatheus.labpcp.service;

import com.andrewmatheus.labpcp.controller.dto.Request.CadastrarTurmaRequest;
import com.andrewmatheus.labpcp.datasource.entity.CursoEntity;
import com.andrewmatheus.labpcp.datasource.entity.DocenteEntity;
import com.andrewmatheus.labpcp.datasource.entity.TurmaEntity;
import com.andrewmatheus.labpcp.datasource.repository.CursoRepository;
import com.andrewmatheus.labpcp.datasource.repository.DocenteRepository;
import com.andrewmatheus.labpcp.datasource.repository.TurmaRepository;
import com.andrewmatheus.labpcp.exceptions.GenericException;
import com.andrewmatheus.labpcp.service.interfaces.TurmaServiceI;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TurmaService implements TurmaServiceI {

    private final AuthService authService;
    private final TurmaRepository turmaRepository;
    private final DocenteRepository docenteRepository;
    private final CursoRepository cursoRepository;

    @Override
    public TurmaEntity criarTurma(CadastrarTurmaRequest cadastrarTurmaRequest, String token) {
        verifyPermmision(token);

        log.info("buscando docente informado!");
        Optional<DocenteEntity> professor = docenteRepository.findById(cadastrarTurmaRequest.id_docente());

        if (professor.isEmpty()) {
            log.error("Professor não encontrado!");
            throw new GenericException("Professor não encontrado!", HttpStatus.NOT_FOUND);
        }

        log.info("buscando curso informado!");
        Optional<CursoEntity> curso = cursoRepository.findById(cadastrarTurmaRequest.id_curso());

        if (curso.isEmpty()) {
            log.error("Curso não encontrado!");
            throw new GenericException("Curso não encontrado!", HttpStatus.NOT_FOUND);
        }

        TurmaEntity novaTurma = new TurmaEntity();
        novaTurma.setNome(cadastrarTurmaRequest.nome());
        novaTurma.setProfessor(professor.get());
        novaTurma.setCurso(curso.get());

        log.info("Salvando nova turma!");
        turmaRepository.save(novaTurma);

        return novaTurma;
    }

    @Override
    public TurmaEntity atualizaTurmaPorId(Long id, CadastrarTurmaRequest cadastrarTurmaRequest, String token) {
        verifyPermmision(token);

        log.info("buscando docente informado!");
        Optional<DocenteEntity> professor = docenteRepository.findById(cadastrarTurmaRequest.id_docente());

        if (professor.isEmpty()) {
            log.error("Professor não encontrado!");
            throw new GenericException("Professor não encontrado!", HttpStatus.NOT_FOUND);
        }

        log.info("buscando curso informado!");
        Optional<CursoEntity> curso = cursoRepository.findById(cadastrarTurmaRequest.id_curso());

        if (curso.isEmpty()) {
            log.error("Curso não encontrado!");
            throw new GenericException("Curso não encontrado!", HttpStatus.NOT_FOUND);
        }

        log.info("buscando turma informado!");
        Optional<TurmaEntity> turmaBuscado = turmaRepository.findById(id);

        if (turmaBuscado.isEmpty()) {
            log.error("Turma não encontrada!");
            throw new GenericException("Turma não encontrada!", HttpStatus.NOT_FOUND);
        }

        TurmaEntity turmaExistente = turmaBuscado.get();

        turmaExistente.setNome(cadastrarTurmaRequest.nome());
        turmaExistente.setCurso(curso.get());
        turmaExistente.setProfessor(professor.get());

        log.info("Atualizando Turma!");
        turmaRepository.save(turmaExistente);

        return turmaExistente;
    }

    @Override
    public TurmaEntity buscaTurmaPorId(Long id, String token) {
        log.info("Buscando turma por ID!");
        Optional<TurmaEntity> turma = turmaRepository.findById(id);

        if (turma.isEmpty()) {
            log.error("Turma não encontrada!");
            throw new GenericException("Turma não encontrada!", HttpStatus.NOT_FOUND);
        }

        return turma.get();
    }

    @Override
    public List<TurmaEntity> buscaTodos(String token) {
        verifyPermmision(token);

        log.info("Retornando lista de turmas!");
        return turmaRepository.findAll();
    }

    @Override
    public void removerTurmaPorId(Long id, String token) {
        log.info("Iniciando verificação de autenticação!");
        String papelToken =  authService.buscaCampoToken(token, "scope");

        if (!papelToken.equalsIgnoreCase("ADM")
        )  {
            log.error("Usuario não tem acesso a essa funcionalidade!");
            throw new RuntimeException("Usuario não tem acesso a essa funcionalidade");
        }

        log.info("Buscando turma por ID!");
        Optional<TurmaEntity> turma = turmaRepository.findById(id);

        if (turma.isEmpty()) {
            log.error("Turma não encontrada!");
            throw new GenericException("Turma não encontrada!", HttpStatus.NOT_FOUND);
        }

        log.info("Removendo Turma por ID!");
        turmaRepository.delete(turma.get());
    }

    private void verifyPermmision(String token) {
        log.info("Iniciando verificação de autenticação!");
        String papelToken =  authService.buscaCampoToken(token, "scope");

        if (!papelToken.equalsIgnoreCase("ADM") &&
                !papelToken.equalsIgnoreCase("PEDAGOGICO")
        )  {
            log.error("Usuario não tem acesso a essa funcionalidade!");
            throw new RuntimeException("Usuario não tem acesso a essa funcionalidade");
        }
    }
}
