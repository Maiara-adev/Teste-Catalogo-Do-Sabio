package com.br.catalogodosabio.application.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@Builder
public class BookDTO implements Serializable {
    private Long id;
    private String titulo;
    private String autor;
    private String genero;
    private String editora;
    private String isbn;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate dataPublicacao;

    private String descricao;
}
