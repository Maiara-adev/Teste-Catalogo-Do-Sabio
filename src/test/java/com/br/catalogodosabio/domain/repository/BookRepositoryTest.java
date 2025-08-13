package com.br.catalogodosabio.domain.repository;

import com.br.catalogodosabio.domain.entity.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@TestPropertySource(properties = "spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect")
class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @BeforeEach
    void preparaAmbiente() {
        Book book = new Book();
        book.setTitulo("A Dama da Noite");
        book.setAutor("Stephen King");
        book.setGenero("Terror");
        bookRepository.save(book);
    }

    @Test
    void testaBuscaPorAutor_quandoAutorExiste() {
        List<Book> livros = bookRepository.findByAutor("king");

        assertFalse(livros.isEmpty());
        assertEquals(1, livros.size());
        assertEquals("Stephen King", livros.get(0).getAutor());
    }

    @Test
    void testaBuscaPorTitulo_quandoTituloExiste() {
        List<Book> livros = bookRepository.findByTitulo("Dama");

        assertFalse(livros.isEmpty());
        assertEquals(1, livros.size());
        assertEquals("A Dama da Noite", livros.get(0).getTitulo());
    }

    @Test
    void testaBuscaPorAutor_quandoAutorNaoExiste_retornaListaVazia() {
        List<Book> livros = bookRepository.findByAutor("Autor Inexistente");

        assertTrue(livros.isEmpty());
    }
}