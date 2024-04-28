package com.andrewmatheus.labpcp.datasource.repository;

import com.andrewmatheus.labpcp.datasource.entity.AlunoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AlunoRepository extends JpaRepository<AlunoEntity, Long> {
    Optional<AlunoEntity> findByIdUsuario(int id_usuario);
}
