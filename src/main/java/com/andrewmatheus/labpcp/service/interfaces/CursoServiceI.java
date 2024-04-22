package com.andrewmatheus.labpcp.service.interfaces;

import com.andrewmatheus.labpcp.controller.dto.Request.CursoRequest;
import com.andrewmatheus.labpcp.datasource.entity.CursoEntity;
import java.util.List;

public interface CursoServiceI {
    CursoEntity criarCurso(CursoRequest cursoRequest, String token);
    CursoEntity atualizaCursoPorId(Long id, CursoRequest cursoRequest, String token);
    CursoEntity buscaCursoPorId(Long id, String token);
    List<CursoEntity> buscaTodos(String token);
    void removerCursoPorId(Long id, String token);
}
