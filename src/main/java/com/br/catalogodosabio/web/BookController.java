package com.br.catalogodosabio.web;

import com.br.catalogodosabio.application.dto.BookDTO;
import com.br.catalogodosabio.application.dto.ErrorResponseDTO;
import com.br.catalogodosabio.application.service.BookService;
import com.br.catalogodosabio.application.service.RecentBooksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/books")
public class BookController {

    @Autowired
    private BookService bookService;

    @Autowired
    private RecentBooksService recentBooksService;

    @GetMapping
    public Page<BookDTO> getAllBooks(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return bookService.listAllBooks(page, size);
    }

    @GetMapping("/genero/{genero}")
    public ResponseEntity<List<BookDTO>> getBooksByGenre(@PathVariable String genero) {
        List<BookDTO> books = bookService.findBooksByGenero(genero);
        if (books.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(books);
    }

    @GetMapping("/genero")
    public ResponseEntity<ErrorResponseDTO> getBooksByGenreWithoutParam() {
        return createNotFoundErrorResponse("Por favor, insira um valor para o gênero.", "/books/genero");
    }

    @GetMapping("/autor/{autor}")
    public ResponseEntity<List<BookDTO>> getBooksByAutor(@PathVariable String autor) {
        List<BookDTO> books = bookService.findBooksByAutor(autor);
        if (books.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(books);
    }

    @GetMapping("/autor")
    public ResponseEntity<ErrorResponseDTO> getBooksByAutorWithoutParam() {
        return createNotFoundErrorResponse("Por favor, insira um valor para o autor.", "/books/autor");
    }

    @GetMapping("/titulo/{titulo}")
    public ResponseEntity<List<BookDTO>> getBooksByTitle(@PathVariable String titulo) {
        List<BookDTO> books = bookService.findBooksByTitulo(titulo);
        if (books.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(books);
    }

    @GetMapping("/titulo")
    public ResponseEntity<ErrorResponseDTO> getBooksByTitleWithoutParam() {
        return createNotFoundErrorResponse("Por favor, insira um valor para o título.", "/books/titulo");
    }

    @GetMapping("/recent")
    public List<BookDTO> getRecentBooks() {
        return recentBooksService.getRecentBooks();
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookDTO> getBookById(@PathVariable Long id) {
        return bookService.findBookById(id)
                .map(bookDTO -> {
                    recentBooksService.addBookToRecent(bookDTO);
                    return ResponseEntity.ok(bookDTO);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    private ResponseEntity<ErrorResponseDTO> createNotFoundErrorResponse(String message, String path) {
        ErrorResponseDTO errorResponse = ErrorResponseDTO.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND.value())
                .error(HttpStatus.NOT_FOUND.getReasonPhrase())
                .message(message)
                .path(path)
                .traceId(UUID.randomUUID().toString())
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
}