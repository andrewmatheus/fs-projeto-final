package com.andrewmatheus.labpcp.controller.dto.Request;

import java.time.LocalDateTime;

public record CadastrarAlunoRequest(String nome, LocalDateTime dataNascimento, Long id_usuario, Long id_turma) {}
