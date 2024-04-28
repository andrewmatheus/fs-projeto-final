package com.andrewmatheus.labpcp.service.interfaces;

import com.andrewmatheus.labpcp.controller.dto.Request.CadastrarAlunoRequest;
import com.andrewmatheus.labpcp.datasource.entity.AlunoEntity;

import java.util.List;

public interface AlunoServiceI {
    AlunoEntity criarAluno(CadastrarAlunoRequest cadastrarAlunoRequest, String token);
    AlunoEntity atualizaAlunoPorId(Long id, CadastrarAlunoRequest cadastrarAlunoRequest, String token);
    AlunoEntity buscaAlunoPorId(Long id, String token);
    List<AlunoEntity> buscaTodos(String token);
    void removerAlunoPorId(Long id, String token);
}
