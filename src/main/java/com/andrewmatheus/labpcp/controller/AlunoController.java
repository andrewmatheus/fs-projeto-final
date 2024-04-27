package com.andrewmatheus.labpcp.controller;

import com.andrewmatheus.labpcp.controller.dto.Request.CadastrarAlunoRequest;
import com.andrewmatheus.labpcp.controller.dto.Response.AlunoResponse;
import com.andrewmatheus.labpcp.datasource.entity.AlunoEntity;
import com.andrewmatheus.labpcp.exceptions.GenericException;
import com.andrewmatheus.labpcp.service.AlunoService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/alunos")
public class AlunoController {

    @Autowired
    private AlunoService alunoService;

    @PostMapping
    public ResponseEntity novoAluno(
            @RequestHeader(name = "Authorization") String token,
            @RequestBody CadastrarAlunoRequest cadastrarAlunoRequest
    ) {
        try {
            AlunoEntity alunoCriado = alunoService.criarAluno(cadastrarAlunoRequest, token.substring(7));

            return ResponseEntity.status(HttpStatus.CREATED).body(new AlunoResponse(
                    alunoCriado.getNome(),
                    alunoCriado.getDataNascimento().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                    alunoCriado.getTurma().getNome()
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity buscaTodosAlunos(
            @RequestHeader(name = "Authorization") String token
    ) {
        try {
            List<AlunoEntity> alunos = alunoService.buscaTodos(token.substring(7));
            List<AlunoResponse> alunoResponse = new ArrayList<>();

            for (AlunoEntity aluno : alunos) {
                alunoResponse.add(new AlunoResponse(
                        aluno.getNome(),
                        aluno.getDataNascimento().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                        aluno.getTurma().getNome()
                ));
            }

            if (!alunoResponse.isEmpty()) {
                return ResponseEntity.status(HttpStatus.OK).body(alunoResponse);
            }

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Alunos não cadastradas!");
        } catch (GenericException e) {
            return ResponseEntity.status(e.getStatus()).body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity buscaAlunoId(
            @PathVariable Long id,
            @RequestHeader(name = "Authorization") String token
    ) {
        try {
            AlunoEntity aluno = alunoService.buscaAlunoPorId(id, token.substring(7));

            return ResponseEntity.status(HttpStatus.OK).body(
                    new AlunoResponse(
                            aluno.getNome(),
                            aluno.getDataNascimento().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                            aluno.getTurma().getNome()
                    ));

        } catch (GenericException e) {
            return ResponseEntity.status(e.getStatus()).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity AtualizaAlunoId(
            @PathVariable Long id,
            @RequestHeader(name = "Authorization") String token,
            @RequestBody CadastrarAlunoRequest atualizarAlunoRequest
    ) {
        try {
            AlunoEntity aluno = alunoService.atualizaAlunoPorId(id, atualizarAlunoRequest, token.substring(7));

            return ResponseEntity.status(HttpStatus.OK).body(new AlunoResponse(
                    aluno.getNome(),
                    aluno.getDataNascimento().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                    aluno.getTurma().getNome()
            ));
        } catch (GenericException e) {
            return ResponseEntity.status(e.getStatus()).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity removerAlunoId(
            @PathVariable Long id,
            @RequestHeader(name = "Authorization") String token
    ) {
        try {
            alunoService.removerAlunoPorId(id, token.substring(7));

            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Aluno excluído com sucesso!");

        } catch (GenericException e) {
            return ResponseEntity.status(e.getStatus()).body("message: " + e.getMessage());
        }
    }
}
