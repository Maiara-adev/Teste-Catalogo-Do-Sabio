package com.br.catalogodosabio.web.controller;

import com.br.catalogodosabio.application.dto.BookDTO;
import com.br.catalogodosabio.application.service.BookService;
import com.br.catalogodosabio.application.service.RecentBooksService;
import com.br.catalogodosabio.application.service.TokenService;
import com.br.catalogodosabio.web.BookController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookController.class)
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @MockBean
    private RecentBooksService recentBooksService;

    @MockBean
    private TokenService tokenService;

    @MockBean
    private UserDetailsService userDetailsService;

    @Test
    void retornaOkBuscaTodosLivros() throws Exception {
        BookDTO bookDTO = BookDTO.builder().titulo("Java para Iniciantes").build();
        when(bookService.listAllBooks(anyInt(), anyInt())).thenReturn(new PageImpl<>(List.of(bookDTO)));

        mockMvc.perform(get("/books").with(user("testuser")).with(csrf()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].titulo").value("Java para Iniciantes"));
    }

    @Test
    void retornaOkBuscaPorIdComLivroExistente() throws Exception {
        BookDTO bookDTO = BookDTO.builder().titulo("Java para Iniciantes").build();
        when(bookService.findBookById(1L)).thenReturn(Optional.of(bookDTO));

        mockMvc.perform(get("/books/1").with(user("testuser")).with(csrf()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo").value("Java para Iniciantes"));
    }

    @Test
    void retornaNotFoundBuscaPorIdComLivroInexistente() throws Exception {
        when(bookService.findBookById(2L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/books/2").with(user("testuser")).with(csrf()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void retornaOkBuscaPorAutorComLivrosExistentes() throws Exception {
        BookDTO bookDTO = BookDTO.builder().autor("Stephen King").build();
        when(bookService.findBooksByAutor(anyString())).thenReturn(List.of(bookDTO));

        mockMvc.perform(get("/books/autor/Stephen King").with(user("testuser")).with(csrf()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].autor").value("Stephen King"));
    }

    @Test
    void retornaNotFoundBuscaPorAutorSemLivros() throws Exception {
        when(bookService.findBooksByAutor(anyString())).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/books/autor/AutorInexistente").with(user("testuser")).with(csrf()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}