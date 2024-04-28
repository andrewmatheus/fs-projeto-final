package com.andrewmatheus.labpcp.controller.dto.Response;

import java.time.LocalDateTime;

public record cadastrarDocenteResponse(String nome, LocalDateTime dataNascimento, String mensagem) {}
