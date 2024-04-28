package com.andrewmatheus.labpcp.service;

import com.andrewmatheus.labpcp.controller.dto.Request.CadastrarAlunoRequest;
import com.andrewmatheus.labpcp.datasource.entity.AlunoEntity;
import com.andrewmatheus.labpcp.datasource.entity.TurmaEntity;
import com.andrewmatheus.labpcp.datasource.entity.UsuarioEntity;
import com.andrewmatheus.labpcp.datasource.repository.AlunoRepository;
import com.andrewmatheus.labpcp.datasource.repository.TurmaRepository;
import com.andrewmatheus.labpcp.datasource.repository.UsuarioRepository;
import com.andrewmatheus.labpcp.exceptions.GenericException;
import com.andrewmatheus.labpcp.service.interfaces.AlunoServiceI;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AlunoService implements AlunoServiceI {

    private final AuthService authService;
    private final AlunoRepository alunoRepository;
    private final TurmaRepository turmaRepository;
    private final UsuarioRepository usuarioRepository;

    @Override
    public AlunoEntity criarAluno(CadastrarAlunoRequest cadastrarAlunoRequest, String token) {
        verifyPermmision(token);
        log.info("buscando usuario informado!");
        Optional<UsuarioEntity> usuario = usuarioRepository.findById(cadastrarAlunoRequest.id_usuario());

        if (usuario.isEmpty()) {
            log.error("Usuário não encontrado!");
            throw new GenericException("Usuário não encontrado!", HttpStatus.NOT_FOUND);
        }

        if (!usuario.get().getPapel().getNome().toString().equalsIgnoreCase("ALUNO")) {
            log.error("Usuário informado não é um aluno!");
            throw new GenericException("Usuário informado não é um aluno!", HttpStatus.BAD_REQUEST);
        }

        log.info("buscando turma informado!");
        Optional<TurmaEntity> turma = turmaRepository.findById(cadastrarAlunoRequest.id_turma());

        if (turma.isEmpty()) {
            log.error("Turma não encontrada!");
            throw new GenericException("Turma não encontrada!", HttpStatus.NOT_FOUND);
        }

        Optional<AlunoEntity> alunoExists = alunoRepository.findByIdUsuario(Math.toIntExact(usuario.get().getId()));

        if (!alunoExists.isEmpty()) {
            log.error("Usuário informado já possui vinculo com aluno cadastrado anteriormente!");
            throw new GenericException("Usuário informado já possui vinculo com aluno: " + alunoExists.get().getNome(), HttpStatus.NOT_FOUND);
        }

        AlunoEntity aluno = new AlunoEntity();
        aluno.setNome(cadastrarAlunoRequest.nome());
        aluno.setDataNascimento(cadastrarAlunoRequest.dataNascimento());
        aluno.setTurma(turma.get());
        aluno.setIdUsuario(Math.toIntExact(cadastrarAlunoRequest.id_usuario()));

        log.info("Salvando Aluno!");
        alunoRepository.save(aluno);

        return aluno;
    }

    @Override
    public AlunoEntity atualizaAlunoPorId(Long id, CadastrarAlunoRequest cadastrarAlunoRequest, String token) {
        verifyPermmision(token);

        log.info("buscando usuario informado!");
        Optional<UsuarioEntity> usuario = usuarioRepository.findById(cadastrarAlunoRequest.id_usuario());

        if (usuario.isEmpty()) {
            log.error("Usuário não encontrado!");
            throw new GenericException("Usuário não encontrado!", HttpStatus.NOT_FOUND);
        }

        if (!usuario.get().getPapel().getNome().toString().equalsIgnoreCase("ALUNO")) {
            log.error("Usuário informado não é um aluno!");
            throw new GenericException("Usuário informado não é um aluno!", HttpStatus.BAD_REQUEST);
        }

        log.info("buscando turma informado!");
        Optional<TurmaEntity> turma = turmaRepository.findById(cadastrarAlunoRequest.id_turma());

        if (turma.isEmpty()) {
            log.error("Turma não encontrada!");
            throw new GenericException("Turma não encontrada!", HttpStatus.NOT_FOUND);
        }

        log.info("buscando aluno informado!");
        Optional<AlunoEntity> alunoBuscado = alunoRepository.findById(id);

        if (alunoBuscado.isEmpty()) {
            log.error("Aluno não encontrado!");
            throw new GenericException("Aluno não encontrado!", HttpStatus.NOT_FOUND);
        }

        AlunoEntity alunoExistente = alunoBuscado.get();

        alunoExistente.setNome(cadastrarAlunoRequest.nome());
        alunoExistente.setDataNascimento(cadastrarAlunoRequest.dataNascimento());
        alunoExistente.setTurma(turma.get());
        alunoExistente.setIdUsuario(Math.toIntExact(cadastrarAlunoRequest.id_usuario()));

        log.info("Atualizando Aluno!");
        alunoRepository.save(alunoExistente);

        return alunoExistente;

    }

    @Override
    public AlunoEntity buscaAlunoPorId(Long id, String token) {
        log.info("Buscando aluno por ID!");
        Optional<AlunoEntity> aluno = alunoRepository.findById(id);

        if (aluno.isEmpty()) {
            log.error("Aluno não encontrado!");
            throw new GenericException("Aluno não encontrado!", HttpStatus.NOT_FOUND);
        }

        return aluno.get();
    }

    @Override
    public List<AlunoEntity> buscaTodos(String token) {
        verifyPermmision(token);

        log.info("Retornando lista de alunos!");
        return alunoRepository.findAll();
    }

    @Override
    public void removerAlunoPorId(Long id, String token) {
        log.info("Iniciando verificação de autenticação!");
        String papelToken =  authService.buscaCampoToken(token, "scope");

        if (!papelToken.equalsIgnoreCase("ADM")
        )  {
            log.error("Usuario não tem acesso a essa funcionalidade!");
            throw new RuntimeException("Usuario não tem acesso a essa funcionalidade");
        }

        log.info("Buscando aluno por ID!");
        Optional<AlunoEntity> aluno = alunoRepository.findById(id);

        if (aluno.isEmpty()) {
            log.error("Aluno não encontrado!");
            throw new GenericException("Aluno não encontrado!", HttpStatus.NOT_FOUND);
        }

        log.info("Removendo Aluno por ID!");
        alunoRepository.delete(aluno.get());
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
