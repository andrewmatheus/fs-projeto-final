package com.andrewmatheus.labpcp.datasource.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "materia")
public class MateriaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "nome", nullable = false)
    private String nome;

    @JsonIgnoreProperties("materias")
    @ManyToOne
    @JoinColumn(name = "id_curso", nullable = false)
    private CursoEntity curso;
}