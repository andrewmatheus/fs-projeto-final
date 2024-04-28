package com.andrewmatheus.labpcp.controller;

import com.andrewmatheus.labpcp.controller.dto.Request.CadastrarTurmaRequest;
import com.andrewmatheus.labpcp.controller.dto.Response.TurmaResponse;
import com.andrewmatheus.labpcp.datasource.entity.TurmaEntity;
import com.andrewmatheus.labpcp.exceptions.GenericException;
import com.andrewmatheus.labpcp.service.TurmaService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/turmas")
public class TurmaController {

    @Autowired
    private TurmaService turmaService;

    @PostMapping
    public ResponseEntity novaTurma(
            @RequestHeader(name = "Authorization") String token,
            @RequestBody CadastrarTurmaRequest cadastrarTurmaRequest
    ) {
        try {
            TurmaEntity turmaCriada = turmaService.criarTurma(cadastrarTurmaRequest, token.substring(7));
            String nomeProfessor = turmaCriada.getProfessor().getNome();
            String nomeCurso = turmaCriada.getCurso().getNome();
            return ResponseEntity.status(HttpStatus.CREATED).body(new TurmaResponse(
                    turmaCriada.getNome(),
                    nomeCurso,
                    nomeProfessor
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity buscaTodasTurmas(
            @RequestHeader(name = "Authorization") String token
    ) {
        try {
            List<TurmaEntity> turmas = turmaService.buscaTodos(token.substring(7));
            List<TurmaResponse> turmaResponse = new ArrayList<>();

            for (TurmaEntity turma : turmas) {
                String nomeProfessor = turma.getProfessor().getNome();
                String nomeCurso = turma.getCurso().getNome();
                turmaResponse.add(new TurmaResponse(turma.getNome(), nomeCurso, nomeProfessor));
            }

            if (!turmas.isEmpty()) {
                return ResponseEntity.status(HttpStatus.OK).body(turmaResponse);
            }

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Turmas não cadastradas!");
        } catch (GenericException e) {
            return ResponseEntity.status(e.getStatus()).body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity buscaTurmaId(
            @PathVariable Long id,
            @RequestHeader(name = "Authorization") String token
    ) {
        try {
            TurmaEntity turma = turmaService.buscaTurmaPorId(id, token.substring(7));

            return ResponseEntity.status(HttpStatus.OK).body(new TurmaResponse(
                    turma.getNome(),
                    turma.getCurso().getNome(),
                    turma.getProfessor().getNome()
            ));

        } catch (GenericException e) {
            return ResponseEntity.status(e.getStatus()).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity AtualizaTurmaId(
            @PathVariable Long id,
            @RequestHeader(name = "Authorization") String token,
            @RequestBody CadastrarTurmaRequest atualizarTurmaRequest
    ) {
        try {
            TurmaEntity turma = turmaService.atualizaTurmaPorId(id, atualizarTurmaRequest, token.substring(7));
            String nomeProfessor = turma.getProfessor().getNome();
            String nomeCurso = turma.getCurso().getNome();
            return ResponseEntity.status(HttpStatus.OK).body(new TurmaResponse(
                    turma.getNome(),
                    nomeCurso,
                    nomeProfessor
            ));
        } catch (GenericException e) {
            return ResponseEntity.status(e.getStatus()).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity removerTurmaId(
            @PathVariable Long id,
            @RequestHeader(name = "Authorization") String token
    ) {
        try {
            turmaService.removerTurmaPorId(id, token.substring(7));

            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Turma excluída com sucesso!");

        } catch (GenericException e) {
            return ResponseEntity.status(e.getStatus()).body("message: " + e.getMessage());
        }
    }
}
