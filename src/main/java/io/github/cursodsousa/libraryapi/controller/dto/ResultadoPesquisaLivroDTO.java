package io.github.cursodsousa.libraryapi.controller.dto;

import io.github.cursodsousa.libraryapi.model.GeneroLivro;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record ResultadoPesquisaLivroDTO(UUID id, // id do livro
                                        String titulo,
                                        LocalDate dataPublicacao,
                                        GeneroLivro generoLivro,
                                        BigDecimal preco,
                                        // AutorDTO pois estamos na camada representacional
                                        AutorDTO autor)  {
    

}
