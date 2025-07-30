package com.alura.literalura.main;

import com.alura.literalura.main.model.Author;
import com.alura.literalura.main.model.Book;
import com.alura.literalura.main.model.BookData;
import com.alura.literalura.repository.AuthorRepository;
import com.alura.literalura.service.CallApi;
import com.alura.literalura.service.ConvertData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.*;
import java.util.stream.Collectors;

public class Main {
    private Scanner scanner = new Scanner(System.in);
    private CallApi callApi = new CallApi();
    private ConvertData convert = new ConvertData();
    private ObjectMapper mapper = new ObjectMapper();
    private AuthorRepository repository;

    public Main(AuthorRepository repository) {
        this.repository = repository;
    }


    public void showMenu() throws JsonProcessingException {
        var option = -1;
        while (option != 0) {
            var menu = """
                    1 - buscar livro pelo titulo
                    2 - listar livros registrados
                    3 - listar autores registrados
                    4 - listar autores vivos em um determinado ano
                    5 - listar livros em um determinado idioma
                    
                    0 - Exit
                    """;
            System.out.println(menu);
            option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 1:
                    searchBookByTitle();
                    break;

                case 2:
                    listSearchedBooks();
                    break;

                case 3:
                    listAuthors();
                    break;
                case 4:
                    alive();
                    break;
                case 5:
                    listByLang();
                    break;

                case 0:
                    System.out.println("Exiting...");
                    break;

                default:
                    System.out.println("Invalid option");
            }
        }
    }

    private void listByLang() {
        System.out.println("Digite o idioma:");
        String lang = scanner.nextLine();

        List<Book> bookList = repository.findAll().stream()
                .flatMap(a -> a.getLivros().stream())
                .collect(Collectors.toList());

        bookList.stream()
                .filter(b -> b.getLang().contains(lang))
                .forEach(System.out::println);

    }

    private void alive() {
        System.out.println("Digite o ano:");
        Integer ano = scanner.nextInt();
        repository.findAll().stream()
                .filter(a -> a.getBirthYear() < ano && a.getDeathYear() > ano)
                .forEach(System.out::println);
    }

    private void listAuthors() {
        repository.findAll().stream()
                .forEach(System.out::println);
    }

    private void listSearchedBooks() {
        repository.findAll().stream()
                .forEach(a -> a.getLivros().stream()
                        .forEach(System.out::println));
    }

    private void searchBookByTitle() throws JsonProcessingException {
        List<Book> books = new ArrayList<>();

        System.out.println("Digite o nome do livro:");
        String name = scanner.nextLine();
        var json = callApi.getData("https://gutendex.com/books/?search=" + name.replace(" ", "+"));
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(json);
        Book book = new Book(json);

        Optional<Author> authorFound = repository.findByNameContainingIgnoreCase(node.path("results").get(0).get("authors").findValue("name").asText());
        if(authorFound.isPresent()){
            books = authorFound.get().getLivros().stream()
                    .collect(Collectors.toList());
            books.add(book);
            authorFound.get().setLivros(books);
            System.out.println("aaaaaaa");
            repository.save(authorFound.get());
        }else {

            Author author = new Author(node.path("results").get(0).get("authors"));
            books.add(book);
            author.setLivros(books);
            repository.save(author);
        }

        System.out.println(
                            "----- LIVRO -----" + "\n"
                            + "Titulo: " + book.getTitle() + "\n"
                            + "Autor: " + book.getAuthor().getName() + "\n"
                            + "Idioma: " + book.getLang() + "\n"
                            + "Numero de downloads: " + book.getDownloads());

    }
}
