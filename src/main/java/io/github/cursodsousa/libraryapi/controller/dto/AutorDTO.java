package io.github.cursodsousa.libraryapi.controller.dto;

import java.time.LocalDate;

// Data Transfer Object -> Objeto de transferencia de dados simples
// Ja colocamos o construtor na declaracao do Record, IMUTAVEL

// SERVE PARA SEPARAR NOSSA CAMADA DE VIEW (input) DA CAMADA DE PERSISTENCIA (entidade com mais dados)
public record AutorDTO(String nome,
                       LocalDate dataNascimento,
                       String nacionalidade) { // Camada representacional, um objeto que REPRESENTA um JSON
}
