package com.andrewmatheus.labpcp.controller.dto.Response;

public record LoginResponse(String valorJWT, long tempoExpiracao, int status, String mensagem) {}
