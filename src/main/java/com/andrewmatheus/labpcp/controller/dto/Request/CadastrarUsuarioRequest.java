package com.andrewmatheus.labpcp.controller.dto.Request;

import com.andrewmatheus.labpcp.datasource.enums.Papeis;

public record CadastrarUsuarioRequest(String login, String senha, Papeis papel) {}
