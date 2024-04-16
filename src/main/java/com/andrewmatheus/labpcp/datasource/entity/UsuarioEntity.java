package com.andrewmatheus.labpcp.datasource.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name="usuario")
public class UsuarioEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome_usuario", nullable = false)
    private String login;

    @Column(nullable = false)
    private String senha;

    @ManyToOne
    @JoinColumn(name = "id_papel", nullable = false)
    private PapelEntity papel;

}
