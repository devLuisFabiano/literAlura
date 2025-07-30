package com.alura.literalura.main.model;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "autores")
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;
    private Integer birthYear;
    private Integer deathYear;
    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    List<Book> books = new ArrayList<>();

    public Author(){}

    public Author(JsonNode json){
        this.name = json.findValue("name").asText();
        this.birthYear = json.findValue("birth_year").asInt();
        this.deathYear = json.findValue("death_year").asInt();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getBirthYear() {
        return birthYear;
    }

    public void setBirthYear(Integer birthYear) {
        this.birthYear = birthYear;
    }

    public Integer getDeathYear() {
        return deathYear;
    }

    public void setDeathYear(Integer deathYear) {
        this.deathYear = deathYear;
    }

    public List<Book> getLivros() {
        return books;
    }

    public void setLivros(List<Book> books) {
        books.forEach(b -> b.setAuthor(this));
        this.books = books;
    }

    @Override
    public String toString() {
        List<String> books1 = books.stream().map(b -> b.getTitle()).collect(Collectors.toList());
        return "----- Autor -----" + "\n"
                + "Autor: " + this.name + "\n"
                + "Ano de Nascimento: " + this.birthYear + "\n"
                + "Ano de falecimento: " + this.deathYear + "\n"
                + "Livros: " + books1;
    }
}
