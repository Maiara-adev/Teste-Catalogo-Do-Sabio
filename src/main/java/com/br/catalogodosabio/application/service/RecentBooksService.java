package com.br.catalogodosabio.application.service;

import com.br.catalogodosabio.application.dto.BookDTO;
import java.util.List;

public interface RecentBooksService {

    List<BookDTO> getRecentBooks();

    void addBookToRecent(BookDTO bookDTO);
}