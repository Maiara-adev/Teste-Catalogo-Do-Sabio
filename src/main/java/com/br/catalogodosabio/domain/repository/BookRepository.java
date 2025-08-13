package com.br.catalogodosabio.domain.repository;

import com.br.catalogodosabio.domain.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    @Query("SELECT b FROM Book b WHERE LOWER(b.genero) LIKE LOWER(CONCAT('%', :genero, '%'))")
    List<Book> findByGenero(@Param("genero") String genero);

    @Query("SELECT b FROM Book b WHERE LOWER(b.autor) LIKE LOWER(CONCAT('%', :autor, '%'))")
    List<Book> findByAutor(@Param("autor") String autor);

    @Query("SELECT b FROM Book b WHERE b.titulo LIKE %:titulo%")
    List<Book> findByTitulo(@Param("titulo") String titulo);
}