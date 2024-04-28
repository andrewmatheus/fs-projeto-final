package com.andrewmatheus.labpcp.controller.dto.Request;

public record CadastrarNotasRequest(Long id_aluno, Long id_professor, Long id_materia, Double valor) {}
