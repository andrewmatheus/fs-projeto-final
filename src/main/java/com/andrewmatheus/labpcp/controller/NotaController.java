package com.andrewmatheus.labpcp.controller;

import com.andrewmatheus.labpcp.controller.dto.Request.CadastrarNotasRequest;
import com.andrewmatheus.labpcp.controller.dto.Response.NotaResponse;
import com.andrewmatheus.labpcp.controller.dto.Response.PontuacaoResponse;
import com.andrewmatheus.labpcp.datasource.entity.AlunoEntity;
import com.andrewmatheus.labpcp.datasource.entity.NotasEntity;
import com.andrewmatheus.labpcp.exceptions.GenericException;
import com.andrewmatheus.labpcp.service.AlunoService;
import com.andrewmatheus.labpcp.service.NotaService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notas")
public class NotaController {

    @Autowired
    private NotaService notaService;

    @Autowired
    private AlunoService alunoService;

    @PostMapping
    public ResponseEntity novaNota(
            @RequestHeader(name = "Authorization") String token,
            @RequestBody CadastrarNotasRequest cadastrarNotasRequest
    ) {
        try {
            NotasEntity notaLancada = notaService.criarNota(cadastrarNotasRequest, token.substring(7));
            AlunoEntity aluno = alunoService.buscaAlunoPorId((long) notaLancada.getIdAluno(), token);

            return ResponseEntity.status(HttpStatus.CREATED).body(new NotaResponse(
                    aluno.getNome(),
                    notaLancada.getValor(),
                    notaLancada.getData().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity buscaNotaId(
            @PathVariable Long id,
            @RequestHeader(name = "Authorization") String token
    ) {
        try {
            NotasEntity nota = notaService.buscaNotaPorId(id, token.substring(7));

            return ResponseEntity.status(HttpStatus.OK).body(nota);

        } catch (GenericException e) {
            return ResponseEntity.status(e.getStatus()).body(e.getMessage());
        }
    }

    @GetMapping("/alunos/{id}/notas")
    public ResponseEntity buscaNotaAlunoId(
            @PathVariable Long id,
            @RequestHeader(name = "Authorization") String token
    ) {
        try {
            List<NotaResponse> notasResponse = notaService.buscaNotasPorAluno(id, token.substring(7));

            if (!notasResponse.isEmpty()) {
                return ResponseEntity.status(HttpStatus.OK).body(notasResponse);
            }

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Aluno não possui notas cadastradas!");
        } catch (GenericException e) {
            return ResponseEntity.status(e.getStatus()).body(e.getMessage());
        }
    }

    @GetMapping("/alunos/{id}/pontuacao")
    public ResponseEntity buscaPontuacaoAlunoId(
            @PathVariable Long id,
            @RequestHeader(name = "Authorization") String token
    ) {
        try {
            PontuacaoResponse responsePontuacao = notaService.buscarPontuacaoPorAluno(id, token.substring(7));

            return ResponseEntity.status(HttpStatus.OK).body(responsePontuacao);

        } catch (GenericException e) {
            return ResponseEntity.status(e.getStatus()).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity AtualizaNotaId(
            @PathVariable Long id,
            @RequestHeader(name = "Authorization") String token,
            @RequestBody CadastrarNotasRequest cadastrarNotasRequest
    ) {
        try {
            NotasEntity nota = notaService.atualizaNotaPorId(id, cadastrarNotasRequest, token.substring(7));

            AlunoEntity aluno = alunoService.buscaAlunoPorId((long) nota.getIdAluno(), token);

            return ResponseEntity.status(HttpStatus.OK).body(new NotaResponse(
                    aluno.getNome(),
                    nota.getValor(),
                    nota.getData().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
            ));
        } catch (GenericException e) {
            return ResponseEntity.status(e.getStatus()).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity removerNotaId(
            @PathVariable Long id,
            @RequestHeader(name = "Authorization") String token
    ) {
        try {
            notaService.removerNotaPorId(id, token.substring(7));

            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Nota excluída com sucesso!");

        } catch (GenericException e) {
            return ResponseEntity.status(e.getStatus()).body("message: " + e.getMessage());
        }
    }
}
