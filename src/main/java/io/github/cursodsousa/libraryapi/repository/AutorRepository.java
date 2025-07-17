package io.github.cursodsousa.libraryapi.repository;

import io.spring.springdatajpa.libraryapi.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AutorRepository extends JpaRepository<Autor, UUID> {
}
