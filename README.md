# O Catálogo do Sábio

## Descrição do Projeto
O desafio "O Catálogo do Sábio" é uma API RESTful dedicada ao gerenciamento de livros. Ela oferece funcionalidades para consultar dados de livros fictícios de forma eficiente. O projeto foi desenvolvido com Spring Boot 3.x e utiliza uma arquitetura limpa (Clean Architecture), garantindo a separação de responsabilidades e facilitando a manutenção e escalabilidade do sistema.

Para otimizar o desempenho, implementamos um sistema de cache com Redis, que armazena dados frequentemente consultados e os livros visualizados recentemente. O projeto também utiliza Docker para simplificar a execução em qualquer ambiente.

## Tecnologias Utilizadas
- **Linguagem**: Java 17
- **Framework**: Spring Boot 3.3.2
- **Contêineres**: Docker e Docker Compose
- **Banco de Dados**: PostgreSQL
- **Cache**: Redis
- **Persistência**: Spring Data JPA
- **Segurança**: Spring Security com JWT
- **Documentação**: Swagger (OpenAPI 3) com springdoc-openapi
- **Testes**: JUnit 5 e Mockito
- **Outros**: Lombok, MapStruct (para o mapper)

## Estrutura do Projeto com Clean Architecture
A aplicação é organizada em camadas, seguindo os princípios da arquitetura limpa:

- **Application**: Contém os serviços de aplicação (`BookService`, `TokenService`) e os DTOs (`BookDTO`, `ErrorResponseDTO`). É responsável pela lógica de negócio e pela conversão de dados.
- **Config**: Contém as configurações essenciais, como a configuração de segurança (`SecurityConfig`), o setup do Redis (`RedisConfig`) e as constantes de cache.
- **Domain**: O coração do sistema. Contém a entidade `Book`, o `BookRepository` e as regras de negócio. É independente de frameworks e tecnologias.
- **Infra**: Contém implementações técnicas, como o serializador de página para o cache (`PageSerializer`).
- **Web**: Lida com a interação HTTP. Inclui o `BookController` e o `GlobalExceptionHandler` para roteamento e tratamento de erros.

## Funcionalidades da API
A API oferece uma série de endpoints para interagir com o catálogo de livros, todos acessíveis a partir da URL base `http://localhost:8080/books`.

- `GET /books`: Recupera todos os livros com paginação.
- `GET /books/{id}`: Recupera um livro específico pelo seu ID.
- `GET /books/autor/{autor}`: Busca livros por autor (match parcial).
- `GET /books/genero/{genero}`: Busca livros por gênero (match parcial).
- `GET /books/titulo/{titulo}`: Busca livros por título (match parcial).
- `GET /books/recent`: Recupera livros visualizados recentemente.
- `POST /auth/login`: Autentica um usuário e retorna um token JWT.

A documentação interativa da API está disponível no Swagger UI, em `http://localhost:8080/swagger-ui.html`.

## Instruções de Execução com Docker
Para garantir a reprodutibilidade da solução, o projeto utiliza Docker e Docker Compose.

1.  **Garanta que o Docker esteja instalado e ativo.**
2.  **Clone este repositório.**
3.  **Abra o terminal na pasta do projeto e inicie o ambiente Docker.**
    ```bash
    docker-compose up -d
    ```
4.  **Aguarde o status dos contêineres** para garantir que o PostgreSQL e o Redis estejam prontos.
    ```bash
    docker-compose ps
    ```
5.  **Inicie a aplicação**: A aplicação será iniciada automaticamente como um dos serviços do Docker Compose.

## Testes
O projeto inclui testes unitários com JUnit 5 e Mockito para as camadas de serviço, controller e repositório, garantindo o funcionamento adequado das funcionalidades. Para rodar os testes, utilize sua IDE ou o seguinte comando:
```bash
mvn test