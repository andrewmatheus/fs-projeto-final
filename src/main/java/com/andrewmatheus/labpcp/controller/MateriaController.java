package com.andrewmatheus.labpcp.controller;

import com.andrewmatheus.labpcp.controller.dto.Request.CadastrarMateriaRequest;
import com.andrewmatheus.labpcp.controller.dto.Response.MateriaResponse;
import com.andrewmatheus.labpcp.datasource.entity.MateriaEntity;
import com.andrewmatheus.labpcp.exceptions.GenericException;
import com.andrewmatheus.labpcp.service.MateriaService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/materias")
public class MateriaController {

    @Autowired
    private MateriaService materiaService;

    @PostMapping
    public ResponseEntity novaMateria(
            @RequestHeader(name = "Authorization") String token,
            @RequestBody CadastrarMateriaRequest cadastrarMateriaRequest
    ) {
        try {
            MateriaEntity materiaCriada = materiaService.criarMateria(cadastrarMateriaRequest, token.substring(7));

            return ResponseEntity.status(HttpStatus.CREATED).body(new MateriaResponse(
                materiaCriada.getNome(),
                materiaCriada.getCurso().getNome(),
                "Materia criada!"
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity buscaTodasMaterias(
            @RequestHeader(name = "Authorization") String token
    ) {
        try {
            List<MateriaEntity> materias = materiaService.buscaTodos(token.substring(7));
            List<MateriaResponse> materiaResponses = new ArrayList<>();

            for (MateriaEntity materia : materias) {
                String nomeCurso = materia.getCurso().getNome();
                materiaResponses.add(new MateriaResponse(materia.getNome(), nomeCurso, ""));
            }

            if (!materiaResponses.isEmpty()) {
                return ResponseEntity.status(HttpStatus.OK).body(materiaResponses);
            }

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Materias não cadastradas!");
        } catch (GenericException e) {
            return ResponseEntity.status(e.getStatus()).body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity buscaMateriaId(
            @PathVariable Long id,
            @RequestHeader(name = "Authorization") String token
    ) {
        try {
            MateriaEntity materia = materiaService.buscaMateriaPorId(id, token.substring(7));

            return ResponseEntity.status(HttpStatus.OK).body(
                new MateriaResponse(
                    materia.getNome(),
                    materia.getCurso().getNome(),
                    "Materia encontrada!"
                )
            );

        } catch (GenericException e) {
            return ResponseEntity.status(e.getStatus()).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity AtualizaMateriaId(
            @PathVariable Long id,
            @RequestHeader(name = "Authorization") String token,
            @RequestBody CadastrarMateriaRequest atualizarMateriaRequest
    ) {
        try {
            MateriaEntity materia = materiaService.atualizaMateriaPorId(id, atualizarMateriaRequest, token.substring(7));

            return ResponseEntity.status(HttpStatus.OK).body(new MateriaResponse(materia.getNome(), materia.getCurso().getNome(), "Materia atualizada com sucesso!"));
        } catch (GenericException e) {
            return ResponseEntity.status(e.getStatus()).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity removerMateriaId(
            @PathVariable Long id,
            @RequestHeader(name = "Authorization") String token
    ) {
        try {
            materiaService.removerMateriaPorId(id, token.substring(7));

            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Materia excluída com sucesso!");

        } catch (GenericException e) {
            return ResponseEntity.status(e.getStatus()).body("message: " + e.getMessage());
        }
    }
}
