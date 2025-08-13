package com.br.catalogodosabio.application.service.impl;

import com.br.catalogodosabio.application.dto.BookDTO;
import com.br.catalogodosabio.application.service.BookServiceImpl;
import com.br.catalogodosabio.domain.entity.Book;
import com.br.catalogodosabio.domain.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookServiceImpl bookService;

    @Test
    void listaTodosOsLivros() {
        Book book = new Book();
        book.setTitulo("A Dama da Noite");
        List<Book> bookList = List.of(book);
        Page<Book> bookPage = new PageImpl<>(bookList);
        when(bookRepository.findAll(any(Pageable.class))).thenReturn(bookPage);

        Page<BookDTO> resultado = bookService.listAllBooks(0, 10);

        assertEquals(1, resultado.getTotalElements());
        assertEquals("A Dama da Noite", resultado.getContent().get(0).getTitulo());
    }

    @Test
    void buscaLivroPorId_quandoExiste() {
        Book book = new Book();
        book.setTitulo("A Dama da Noite");
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        Optional<BookDTO> resultado = bookService.findBookById(1L);

        assertTrue(resultado.isPresent());
        assertEquals("A Dama da Noite", resultado.get().getTitulo());
    }

    @Test
    void buscaLivroPorId_quandoNaoExiste() {
        when(bookRepository.findById(2L)).thenReturn(Optional.empty());

        Optional<BookDTO> resultado = bookService.findBookById(2L);

        assertTrue(resultado.isEmpty());
    }

    @Test
    void buscaLivrosPorAutor_quandoExistem() {
        Book book = new Book();
        book.setAutor("Stephen King");
        List<Book> bookList = List.of(book);
        when(bookRepository.findByAutor("Stephen King")).thenReturn(bookList);

        List<BookDTO> resultado = bookService.findBooksByAutor("Stephen King");

        assertEquals(1, resultado.size());
        assertEquals("Stephen King", resultado.get(0).getAutor());
    }

    @Test
    void buscaLivrosPorAutor_quandoNaoExistem() {
        when(bookRepository.findByAutor("Autor Inexistente")).thenReturn(Collections.emptyList());

        List<BookDTO> resultado = bookService.findBooksByAutor("Autor Inexistente");

        assertTrue(resultado.isEmpty());
    }

    @Test
    void buscaLivrosPorGenero_quandoExistem() {
        Book book = new Book();
        book.setGenero("Terror");
        List<Book> bookList = List.of(book);
        when(bookRepository.findByGenero("Terror")).thenReturn(bookList);

        List<BookDTO> resultado = bookService.findBooksByGenero("Terror");

        assertEquals(1, resultado.size());
        assertEquals("Terror", resultado.get(0).getGenero());
    }

    @Test
    void buscaLivrosPorTitulo_quandoExistem() {
        Book book = new Book();
        book.setTitulo("A Dama da Noite");
        List<Book> bookList = List.of(book);
        when(bookRepository.findByTitulo("A Dama da Noite")).thenReturn(bookList);

        List<BookDTO> resultado = bookService.findBooksByTitulo("A Dama da Noite");

        assertEquals(1, resultado.size());
        assertEquals("A Dama da Noite", resultado.get(0).getTitulo());
    }
}