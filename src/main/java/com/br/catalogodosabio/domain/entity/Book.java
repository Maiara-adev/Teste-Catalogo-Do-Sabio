package com.br.catalogodosabio.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "books")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;

    private String autor;

    private String genero;

    private String publicacao;

    private String isbn;

    private LocalDate dataPublicacao;

    @Column(length = 2000)
    private String descricao;
}
