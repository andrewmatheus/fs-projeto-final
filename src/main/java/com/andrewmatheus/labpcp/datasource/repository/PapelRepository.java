package com.andrewmatheus.labpcp.datasource.repository;

import com.andrewmatheus.labpcp.datasource.entity.PapelEntity;
import com.andrewmatheus.labpcp.datasource.enums.Papeis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface PapelRepository extends JpaRepository<PapelEntity, Long> {
    Optional<PapelEntity> findByNome(Papeis nome);
}
