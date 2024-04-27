package com.andrewmatheus.labpcp.service;

import com.andrewmatheus.labpcp.controller.dto.Request.CadastrarNotasRequest;
import com.andrewmatheus.labpcp.controller.dto.Response.PontuacaoResponse;
import com.andrewmatheus.labpcp.datasource.entity.*;
import com.andrewmatheus.labpcp.datasource.repository.AlunoRepository;
import com.andrewmatheus.labpcp.datasource.repository.DocenteRepository;
import com.andrewmatheus.labpcp.datasource.repository.MateriaRepository;
import com.andrewmatheus.labpcp.datasource.repository.NotaRepository;
import com.andrewmatheus.labpcp.exceptions.GenericException;
import com.andrewmatheus.labpcp.service.interfaces.NotaServiceI;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotaService implements NotaServiceI {

    private final AuthService authService;

    private final NotaRepository notaRepository;
    private final AlunoRepository alunoRepository;
    private final DocenteRepository docenteRepository;
    private final MateriaRepository materiaRepository;

    @Override
    public NotasEntity criarNota(CadastrarNotasRequest cadastrarNotasRequest, String token) {
        verifyPermission(token);

        log.info("Validando nota!");
        if (cadastrarNotasRequest.valor() < 0 || cadastrarNotasRequest.valor() > 10) {
            log.error("Nota deve ser no mínimo 0 e no máximo 10!");
            throw new GenericException("Nota deve ser no mínimo 0 e no máximo 10!", HttpStatus.BAD_REQUEST);
        }

        Optional<AlunoEntity> alunoBuscado = alunoRepository.findById(cadastrarNotasRequest.id_aluno());

        if (alunoBuscado.isEmpty()) {
            log.error("Aluno não encontrado!");
            throw new GenericException("Aluno não encontrado!", HttpStatus.NOT_FOUND);
        }

        Optional<DocenteEntity> docenteBuscado = docenteRepository.findById(cadastrarNotasRequest.id_professor());

        if (docenteBuscado.isEmpty()) {
            log.error("Docente não encontrado!");
            throw new GenericException("Docente não encontrado!", HttpStatus.NOT_FOUND);
        }

        Optional<MateriaEntity> materiaBuscado = materiaRepository.findById(cadastrarNotasRequest.id_materia());

        if (materiaBuscado.isEmpty()) {
            log.error("Matéria não encontrada!");
            throw new GenericException("Matéria não encontrada!", HttpStatus.NOT_FOUND);
        }

        NotasEntity novaNota = new NotasEntity();

        novaNota.setValor(cadastrarNotasRequest.valor());
        novaNota.setIdAluno(alunoBuscado.get().getId());
        novaNota.setIdProfessor(docenteBuscado.get().getId());
        novaNota.setIdMateria(materiaBuscado.get().getId());
        novaNota.setData(LocalDateTime.now());

        notaRepository.save(novaNota);

        return novaNota;
    }

    @Override
    public NotasEntity atualizaNotaPorId(Long id, CadastrarNotasRequest cadastrarNotasRequest, String token) {
        verifyPermission(token);

        log.info("Validando nota!");
        if (cadastrarNotasRequest.valor() < 0 || cadastrarNotasRequest.valor() > 10) {
            log.error("Nota deve ser no mínimo 0 e no máximo 10!");
            throw new GenericException("Nota deve ser no mínimo 0 e no máximo 10!", HttpStatus.BAD_REQUEST);
        }

        Optional<AlunoEntity> alunoBuscado = alunoRepository.findById(cadastrarNotasRequest.id_aluno());

        if (alunoBuscado.isEmpty()) {
            log.error("Aluno não encontrado!");
            throw new GenericException("Aluno não encontrado!", HttpStatus.NOT_FOUND);
        }

        Optional<DocenteEntity> docenteBuscado = docenteRepository.findById(cadastrarNotasRequest.id_professor());

        if (docenteBuscado.isEmpty()) {
            log.error("Docente não encontrado!");
            throw new GenericException("Docente não encontrado!", HttpStatus.NOT_FOUND);
        }

        Optional<MateriaEntity> materiaBuscado = materiaRepository.findById(cadastrarNotasRequest.id_materia());

        if (materiaBuscado.isEmpty()) {
            log.error("Matéria não encontrada!");
            throw new GenericException("Matéria não encontrada!", HttpStatus.NOT_FOUND);
        }

        log.info("buscando nota pelo id informado!");
        Optional<NotasEntity> notaBuscado = notaRepository.findById(id);

        if (notaBuscado.isEmpty()) {
            log.error("Nota não encontrada!");
            throw new GenericException("Nota não encontrada!", HttpStatus.NOT_FOUND);
        }

        NotasEntity notaExistente = notaBuscado.get();

        notaExistente.setValor(cadastrarNotasRequest.valor());
        notaExistente.setIdAluno(alunoBuscado.get().getId());
        notaExistente.setIdProfessor(docenteBuscado.get().getId());
        notaExistente.setIdMateria(materiaBuscado.get().getId());
        notaExistente.setData(LocalDateTime.now());

        log.info("Atualizando Nota!");
        notaRepository.save(notaExistente);

        return notaExistente;
    }

    @Override
    public NotasEntity buscaNotaPorId(Long id, String token) {
        verifyPermission(token);

        log.info("Buscando nota por ID!");
        Optional<NotasEntity> notaBuscada = notaRepository.findById(id);

        if (notaBuscada.isEmpty()) {
            log.error("Nota não encontrada!");
            throw new GenericException("Nota não encontrada!", HttpStatus.NOT_FOUND);
        }

        return notaBuscada.get();
    }

    @Override
    public List<NotasEntity> buscaNotasPorAluno(Long id, String token) {
        return List.of();
    }

    @Override
    public PontuacaoResponse buscarPontuacaoPorAluno(Long id, String token) {

        log.info("Iniciando busca do aluno!");
        Optional<AlunoEntity> alunoEncontrado = alunoRepository.findById(id);

        if (alunoEncontrado.isEmpty()) {
            log.error("Aluno não encontrado!");
            throw new GenericException("Aluno não encontrado!", HttpStatus.NOT_FOUND);
        }

        log.info("Iniciando busca de notas do aluno!");
        List<NotasEntity> notasDoAluno = notaRepository.findAllByIdAluno(alunoEncontrado.get().getId());

        if (notasDoAluno.isEmpty()) {
            log.info("Aluno não possui notas cadastradas pontuação mínima!");
            return new PontuacaoResponse(
               alunoEncontrado.get().getNome(),
               0.0
            );
        }

        Double somaNotas = notasDoAluno.stream()
                .mapToDouble(NotasEntity::getValor)
                .sum();

        Double notaTotal = (somaNotas * 100 / notasDoAluno.size()) / 100;

        return new PontuacaoResponse(
                alunoEncontrado.get().getNome(),
                notaTotal
        );
    }

    @Override
    public void removerNotaPorId(Long id, String token) {
        log.info("Iniciando verificação de autenticação!");
        String papelToken =  authService.buscaCampoToken(token, "scope");

        if (!papelToken.equalsIgnoreCase("ADM"))  {
            log.error("Usuario não tem acesso a essa funcionalidade!");
            throw new RuntimeException("Usuario não tem acesso a essa funcionalidade");
        }

        log.info("Buscando nota por ID!");
        Optional<NotasEntity> notaBuscada = notaRepository.findById(id);

        if (notaBuscada.isEmpty()) {
            log.error("Nota não encontrada!");
            throw new GenericException("Nota não encontrada!", HttpStatus.NOT_FOUND);
        }

        log.info("Removendo Nota por ID!");
        notaRepository.delete(notaBuscada.get());
    }

    private void verifyPermission(String token) {
        log.info("Iniciando verificação de autenticação!");
        String papelToken =  authService.buscaCampoToken(token, "scope");

        if (!papelToken.equalsIgnoreCase("ADM") &&
            !papelToken.equalsIgnoreCase("PEDAGOGICO") &&
            !papelToken.equalsIgnoreCase("RECRUITER") &&
            !papelToken.equalsIgnoreCase("PROFESSOR")
        )  {
            log.error("Usuario não tem acesso a essa funcionalidade!");
            throw new RuntimeException("Usuario não tem acesso a essa funcionalidade");
        }
    }
}
