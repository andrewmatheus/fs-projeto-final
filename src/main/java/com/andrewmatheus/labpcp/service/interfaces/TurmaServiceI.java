package com.andrewmatheus.labpcp.service.interfaces;

import com.andrewmatheus.labpcp.controller.dto.Request.CadastrarTurmaRequest;
import com.andrewmatheus.labpcp.datasource.entity.TurmaEntity;

import java.util.List;

public interface TurmaServiceI {
    TurmaEntity criarTurma(CadastrarTurmaRequest cadastrarTurmaRequest, String token);
    TurmaEntity atualizaTurmaPorId(Long id, CadastrarTurmaRequest cadastrarTurmaRequest, String token);
    TurmaEntity buscaTurmaPorId(Long id, String token);
    List<TurmaEntity> buscaTodos(String token);
    void removerTurmaPorId(Long id, String token);
}
