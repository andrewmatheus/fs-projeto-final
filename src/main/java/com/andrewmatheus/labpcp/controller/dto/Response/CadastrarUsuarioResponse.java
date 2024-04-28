package com.andrewmatheus.labpcp.controller.dto.Response;

import com.andrewmatheus.labpcp.datasource.enums.Papeis;

public record CadastrarUsuarioResponse(String login, Papeis papel, String mensagem) {}
