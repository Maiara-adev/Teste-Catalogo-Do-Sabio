package com.br.catalogodosabio.application.service;

import com.br.catalogodosabio.application.dto.BookDTO;
import org.springframework.data.domain.Page;
import java.util.List;
import java.util.Optional;

public interface BookService {

    Page<BookDTO> listAllBooks(int page, int size);

    Optional<BookDTO> findBookById(Long id);

    List<BookDTO> findBooksByGenero(String genero);

    List<BookDTO> findBooksByAutor(String autor);

    List<BookDTO> findBooksByTitulo(String titulo);
}