package com.andrewmatheus.labpcp.datasource.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "aluno")
public class AlunoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "data_nascimento", nullable = false)
    private LocalDateTime dataNascimento;

    @Column(name = "id_usuario", unique = true, nullable = false)
    private int idUsuario;

    @ManyToOne
    @JoinColumn(name = "id_turma", nullable = false)
    private TurmaEntity turma;
}
