package com.br.catalogodosabio.config;

import com.br.catalogodosabio.domain.entity.Book;
import com.br.catalogodosabio.domain.repository.BookRepository;
import com.github.javafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private BookRepository bookRepository;

    @Override
    public void run(String... args) throws Exception {
        // Se o banco já tiver livros, não faz nada
        if (bookRepository.count() > 0) {
            return;
        }

        Faker faker = new Faker(new Locale("pt-BR"));

        for (int i = 0; i < 30; i++) {
            com.github.javafaker.Book fakeBook = faker.book();

            // Converte a data do Faker para LocalDate
            LocalDate publishDate = faker.date()
                    .past(3650, TimeUnit.DAYS) // Até 10 anos no passado
                    .toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();

            Book book = Book.builder()
                    .titulo(fakeBook.title())
                    .autor(fakeBook.author())
                    .genero(fakeBook.genre())
                    .publicacao(fakeBook.publisher())
                    .isbn(faker.code().isbn13())
                    .dataPublicacao(publishDate)
                    .descricao(faker.lorem().paragraph(5))
                    .build();

            bookRepository.save(book);
        }
        System.out.println(">>> BANCO DE DADOS POPULADO COM 30 LIVROS. <<<");
    }
}