package com.andrewmatheus.labpcp.datasource.repository;

import com.andrewmatheus.labpcp.datasource.entity.CursoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CursoRepository extends JpaRepository<CursoEntity, Long> {
    Optional<CursoEntity> findByNome(String nome);
}
