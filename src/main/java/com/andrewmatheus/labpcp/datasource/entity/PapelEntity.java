package com.andrewmatheus.labpcp.datasource.entity;

import com.andrewmatheus.labpcp.datasource.enums.Papeis;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name="papel")
public class PapelEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome", nullable = false)
    @Enumerated(EnumType.STRING)
    private Papeis nome;

}
