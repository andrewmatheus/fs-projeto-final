package com.andrewmatheus.labpcp.service.interfaces;

import com.andrewmatheus.labpcp.controller.dto.Request.LoginRequest;
import com.andrewmatheus.labpcp.controller.dto.Response.LoginResponse;
import com.andrewmatheus.labpcp.datasource.entity.PapelEntity;

public interface AuthServiceI {
    LoginResponse gerarToken(LoginRequest loginRequest);
    String buscaCampoToken(String token, String claim);
}
