package com.alura.literalura.main.model;

import com.alura.literalura.service.ConvertData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;

@Entity
@Table(name = "livros")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String lang;
    private Integer downloads;
    @ManyToOne
    private Author author;


    public Book(){}

    public Book(String json) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(json);

        this.title = String.valueOf(node.path("results").get(0).get("title")).split(";")[0];
        this.lang = String.valueOf(node.path("results").get(0).get("languages"));
        this.downloads = Integer.valueOf(String.valueOf(node.path("results").get(0).get("download_count")));
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public Integer getDownloads() {
        return downloads;
    }

    public void setDownloads(Integer downloads) {
        this.downloads = downloads;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    @Override
    public String toString() {
        return "----- LIVRO -----" + "\n"
                + "Titulo: " + this.title + "\n"
                + "Autor: " + this.author.getName() + "\n"
                + "Idioma: " + this.lang + "\n"
                + "Numero de downloads: " + this.downloads;
    }
}
