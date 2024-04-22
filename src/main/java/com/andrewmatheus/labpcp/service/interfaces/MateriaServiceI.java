package com.andrewmatheus.labpcp.service.interfaces;

import com.andrewmatheus.labpcp.controller.dto.Request.CadastrarMateriaRequest;
import com.andrewmatheus.labpcp.datasource.entity.MateriaEntity;

import java.util.List;

public interface MateriaServiceI {
    MateriaEntity criarMateria(CadastrarMateriaRequest cadastrarMateriaRequest, String token);
    MateriaEntity atualizaMateriaPorId(Long id, CadastrarMateriaRequest cadastrarMateriaRequest, String token);
    MateriaEntity buscaMateriaPorId(Long id, String token);
    List<MateriaEntity> buscaTodos(String token);
    void removerMateriaPorId(Long id, String token);
}
