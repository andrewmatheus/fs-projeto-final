package com.andrewmatheus.labpcp.controller;

import com.andrewmatheus.labpcp.controller.dto.Request.CadastrarDocenteRequest;
import com.andrewmatheus.labpcp.controller.dto.Response.DocenteResponse;
import com.andrewmatheus.labpcp.controller.dto.Response.cadastrarDocenteResponse;
import com.andrewmatheus.labpcp.datasource.entity.DocenteEntity;
import com.andrewmatheus.labpcp.exceptions.GenericException;
import com.andrewmatheus.labpcp.service.DocenteService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/docentes")
public class DocenteController {

    @Autowired
    private DocenteService docenteService;

    @PostMapping
    public ResponseEntity novoDocente(
            @RequestHeader(name = "Authorization") String token,
            @RequestBody CadastrarDocenteRequest cadastrarDocenteRequest
    ) {
        try {
            DocenteEntity docente = docenteService.criarDocente(cadastrarDocenteRequest, token.substring(7));

            return ResponseEntity.status(HttpStatus.CREATED).body(new cadastrarDocenteResponse(
                    docente.getNome(),
                    docente.getDataEntrada(),
                    "Docente cadastrado com sucesso!"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity buscaTodosDocentes(
            @RequestHeader(name = "Authorization") String token
    ) {
        try {
            List<DocenteEntity> docentes = docenteService.buscaTodos(token.substring(7));

            if (!docentes.isEmpty()) {
                return ResponseEntity.status(HttpStatus.OK).body(docentes);
            }

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("docentes não cadastrados!");
        } catch (GenericException e) {
            return ResponseEntity.status(e.getStatus()).body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity buscaDocenteId(
            @PathVariable Long id,
            @RequestHeader(name = "Authorization") String token
    ) {
        try {
            DocenteEntity docente = docenteService.buscaDocentePorId(id, token.substring(7));

            return ResponseEntity.status(HttpStatus.OK).body(new DocenteResponse(
                    docente.getNome(),
                    docente.getDataEntrada().format(DateTimeFormatter.ofPattern("YYYY-MM-DD")),
                    docente.getUsuario().getLogin()
                )
            );
        } catch (GenericException e) {
            return ResponseEntity.status(e.getStatus()).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity AtualizaDocenteId(
            @PathVariable Long id,
            @RequestHeader(name = "Authorization") String token,
            @RequestBody CadastrarDocenteRequest atualizarDocenteRequest
    ) {
        try {
            DocenteEntity docente = docenteService.atualizaDocentePorId(id, atualizarDocenteRequest, token.substring(7));

            return ResponseEntity.status(HttpStatus.OK).body(new cadastrarDocenteResponse(docente.getNome(), docente.getDataEntrada(), "Docente atualizado com sucesso!"));
        } catch (GenericException e) {
            return ResponseEntity.status(e.getStatus()).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity removerDocenteId(
            @PathVariable Long id,
            @RequestHeader(name = "Authorization") String token
    ) {
        try {
            docenteService.removerDocentePorId(id, token.substring(7));

            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Docente excluído com sucesso!");

        } catch (GenericException e) {
            return ResponseEntity.status(e.getStatus()).body("message: " + e.getMessage());
        }
    }

}
