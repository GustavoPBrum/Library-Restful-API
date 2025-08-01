package io.github.cursodsousa.libraryapi.controller.dto;

import io.github.cursodsousa.libraryapi.model.Autor;

import java.time.LocalDate;
import java.util.UUID;

// Data Transfer Object -> Objeto de transferencia de dados simples
// Ja colocamos o construtor na declaracao do Record, IMUTAVEL

// SERVE PARA SEPARAR NOSSA CAMADA DE VIEW (input) DA CAMADA DE PERSISTENCIA (entidade com mais dados)
public record AutorDTO(UUID ID,
                       String nome,
                       LocalDate dataNascimento,
                       String nacionalidade
) { // Camada representacional, um objeto que REPRESENTA um JSON

    // Podemos criar metodos para RECORD, apenas uma classe sem *SETS pros VALORES* (imutavel) apenas GETS
    public Autor mapearParaAutor(){
        Autor autor = new Autor();
        autor.setNome(this.nome);
        autor.setDataNascimento(this.dataNascimento);
        autor.setNacionalidade(this.nacionalidade);
        return autor;
    }
}
