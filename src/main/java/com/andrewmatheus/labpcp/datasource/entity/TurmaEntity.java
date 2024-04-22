package com.andrewmatheus.labpcp.datasource.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "turma")
public class TurmaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "nome", nullable = false)
    private String nome;

    @OneToMany(mappedBy = "turma", cascade = CascadeType.ALL)
    private List<AlunoEntity> alunos;

    @ManyToOne
    @JoinColumn(name = "id_docente", nullable = false)
    private DocenteEntity professor;

    @JsonIgnoreProperties("turmas")
    @ManyToOne
    @JoinColumn(name = "id_curso", nullable = false)
    private CursoEntity curso;
}
