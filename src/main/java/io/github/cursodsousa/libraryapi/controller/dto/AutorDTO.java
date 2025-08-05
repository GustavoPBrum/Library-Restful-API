package io.github.cursodsousa.libraryapi.controller.dto;

import io.github.cursodsousa.libraryapi.model.Autor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.UUID;

// Data Transfer Object -> Objeto de transferencia de dados simples
// Ja colocamos o construtor na declaracao do Record, IMUTAVEL

// SERVE PARA SEPARAR NOSSA CAMADA DE VIEW (input) DA CAMADA DE PERSISTENCIA (entidade com mais dados)
public record AutorDTO(UUID ID,
                       // Cada validacao tem sua mensagem
                       @NotBlank(message = "Campo obrigatorio")  // NotNull para String, nao permite nula e nem vazia
                       @Size(max = 100, message = "Tamanho maximo excedido")
                       String nome,
                       @NotNull(message = "Campo obrigatorio")  // --> campos que podem vir
                       @Past(message = "Nao pode ser uma data futura")
                       LocalDate dataNascimento,
                       @NotBlank(message = "Campo obrigatorio")
                       @Size(max = 50, message = "Tamanho maximo excedido")
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
