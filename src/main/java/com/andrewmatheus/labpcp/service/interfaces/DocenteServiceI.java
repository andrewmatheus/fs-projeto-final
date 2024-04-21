package com.andrewmatheus.labpcp.service.interfaces;

import com.andrewmatheus.labpcp.controller.dto.Request.CadastrarDocenteRequest;
import com.andrewmatheus.labpcp.datasource.entity.DocenteEntity;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface DocenteServiceI {
    DocenteEntity criarDocente(CadastrarDocenteRequest cadastrarDocenteRequest, String token);
    DocenteEntity atualizaDocentePorId(Long id, CadastrarDocenteRequest cadastrarDocenteRequest, String token);
    DocenteEntity buscaDocentePorId(Long id, String token);
    List<DocenteEntity> buscaTodos(String token);
    void removerDocentePorId(Long id, String token);
}
