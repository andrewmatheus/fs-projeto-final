package com.andrewmatheus.labpcp.datasource.repository;

import com.andrewmatheus.labpcp.datasource.entity.MateriaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MateriaRepository extends JpaRepository<MateriaEntity, Long> {}
