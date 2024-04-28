package com.andrewmatheus.labpcp.service.interfaces;

import com.andrewmatheus.labpcp.controller.dto.Request.CadastrarNotasRequest;
import com.andrewmatheus.labpcp.controller.dto.Response.NotaResponse;
import com.andrewmatheus.labpcp.controller.dto.Response.PontuacaoResponse;
import com.andrewmatheus.labpcp.datasource.entity.NotasEntity;

import java.util.List;

public interface NotaServiceI {
    NotasEntity criarNota(CadastrarNotasRequest cadastrarNotasRequest, String token);
    NotasEntity atualizaNotaPorId(Long id, CadastrarNotasRequest cadastrarNotasRequest, String token);
    NotasEntity buscaNotaPorId(Long id, String token);
    List<NotaResponse> buscaNotasPorAluno(Long id, String token);
    PontuacaoResponse buscarPontuacaoPorAluno(Long id, String token);
    void removerNotaPorId(Long id, String token);
}
