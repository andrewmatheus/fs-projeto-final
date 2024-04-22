package com.andrewmatheus.labpcp.datasource.repository;

import com.andrewmatheus.labpcp.datasource.entity.TurmaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TurmaRepository extends JpaRepository<TurmaEntity, Long> {}
