package com.alura.literalura.repository;

import com.alura.literalura.main.model.Author;
import com.alura.literalura.main.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AuthorRepository extends JpaRepository<Author, Long> {

    Optional<Author> findByNameContainingIgnoreCase(String authorName);

//    @Query("SELECT a FROM autores a JOIN a.books b WHERE b.lang = :lang")
//    List<Author> findByLang(String lang);
}
