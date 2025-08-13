package com.br.catalogodosabio.application.mapper;

import com.br.catalogodosabio.application.dto.BookDTO;
import com.br.catalogodosabio.domain.entity.Book;

public class BookMapper {
    public static BookDTO toDTO(Book book) {

        return BookDTO.builder()
                .id(book.getId())
                .titulo(book.getTitulo())
                .autor(book.getAutor())
                .genero(book.getGenero())
                .editora(book.getPublicacao())
                .isbn(book.getIsbn())
                .dataPublicacao(book.getDataPublicacao())
                .descricao(book.getDescricao())
                .build();
    }
}