package com.andrewmatheus.labpcp.controller;

import com.andrewmatheus.labpcp.controller.dto.Request.CursoRequest;
import com.andrewmatheus.labpcp.controller.dto.Response.CursoResponse;
import com.andrewmatheus.labpcp.datasource.entity.CursoEntity;
import com.andrewmatheus.labpcp.exceptions.GenericException;
import com.andrewmatheus.labpcp.service.CursoService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cursos")
public class CursoController {

    @Autowired
    private CursoService cursoService;

    @PostMapping
    public ResponseEntity novoCurso(
            @RequestHeader(name = "Authorization") String token,
            @RequestBody CursoRequest cursoRequest
    ) {
        try {
            CursoEntity curso = cursoService.criarCurso(cursoRequest, token.substring(7));

            return ResponseEntity.status(HttpStatus.CREATED).body(curso.getNome());
        } catch (GenericException e) {
            return ResponseEntity.status(e.getStatus()).body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity buscaTodosCursos(
            @RequestHeader(name = "Authorization") String token
    ) {
        try {
            List<CursoEntity> cursos = cursoService.buscaTodos(token.substring(7));

            if (!cursos.isEmpty()) {
                return ResponseEntity.status(HttpStatus.OK).body(cursos);
            }

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nenhum curso não foi cadastrado!");
        } catch (GenericException e) {
            return ResponseEntity.status(e.getStatus()).body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity buscaCursoId(
            @PathVariable Long id,
            @RequestHeader(name = "Authorization") String token
    ) {
        try {
            CursoEntity curso = cursoService.buscaCursoPorId(id, token.substring(7));

            return ResponseEntity.status(HttpStatus.OK).body(new CursoResponse(
                    curso.getNome()
                )
            );
        } catch (GenericException e) {
            return ResponseEntity.status(e.getStatus()).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity AtualizaCursoId(
            @PathVariable Long id,
            @RequestHeader(name = "Authorization") String token,
            @RequestBody CursoRequest atualizarCursoRequest
    ) {
        try {
            CursoEntity curso = cursoService.atualizaCursoPorId(id, atualizarCursoRequest, token.substring(7));

            return ResponseEntity.status(HttpStatus.OK).body(new CursoResponse(curso.getNome()));
        } catch (GenericException e) {
            return ResponseEntity.status(e.getStatus()).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity removerCursoId(
            @PathVariable Long id,
            @RequestHeader(name = "Authorization") String token
    ) {
        try {
            cursoService.removerCursoPorId(id, token.substring(7));

            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Curso excluído com sucesso!");

        } catch (GenericException e) {
            return ResponseEntity.status(e.getStatus()).body("message: " + e.getMessage());
        }
    }

}
