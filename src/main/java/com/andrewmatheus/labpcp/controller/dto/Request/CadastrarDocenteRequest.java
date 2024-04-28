package com.andrewmatheus.labpcp.controller.dto.Request;

import java.time.LocalDateTime;

public record CadastrarDocenteRequest(String nome, LocalDateTime dataEntrada) {}