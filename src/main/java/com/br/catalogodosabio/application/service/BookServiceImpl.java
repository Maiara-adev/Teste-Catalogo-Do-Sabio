package com.br.catalogodosabio.application.service;

import com.br.catalogodosabio.application.dto.BookDTO;
import com.br.catalogodosabio.application.mapper.BookMapper;
import com.br.catalogodosabio.application.service.BookService;
import com.br.catalogodosabio.domain.entity.Book;
import com.br.catalogodosabio.domain.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository repository;

    @Autowired
    public BookServiceImpl(BookRepository repository) {
        this.repository = repository;
    }

    @Override
    public Page<BookDTO> listAllBooks(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("titulo").ascending());
        Page<Book> books = repository.findAll(pageable);
        return books.map(BookMapper::toDTO);
    }

    @Override
    @Cacheable(value = "book", key = "#id")
    public Optional<BookDTO> findBookById(Long id) {
        return repository.findById(id).map(BookMapper::toDTO);
    }

    @Override
    @Cacheable(value = "booksByGenero", key = "#genero?.toLowerCase()")
    public List<BookDTO> findBooksByGenero(String genero) {
        if (genero == null) {
            return Collections.emptyList();
        }
        return repository.findByGenero(genero).stream()
                .map(BookMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Cacheable(value = "booksByAutor", key = "#autor?.toLowerCase()")
    public List<BookDTO> findBooksByAutor(String autor) {
        if (autor == null) {
            return Collections.emptyList();
        }
        return repository.findByAutor(autor).stream()
                .map(BookMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Cacheable(value = "booksByTitulo", key = "#titulo?.toLowerCase()")
    public List<BookDTO> findBooksByTitulo(String titulo) {
        if (titulo == null) {
            return Collections.emptyList();
        }
        return repository.findByTitulo(titulo).stream()
                .map(BookMapper::toDTO)
                .collect(Collectors.toList());
    }
}