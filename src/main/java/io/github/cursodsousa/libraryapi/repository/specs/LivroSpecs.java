package io.github.cursodsousa.libraryapi.repository.specs;

import io.github.cursodsousa.libraryapi.model.GeneroLivro;
import io.github.cursodsousa.libraryapi.model.Livro;
import org.springframework.data.jpa.domain.Specification;

public class LivroSpecs {

    // Specification nada mais eh do que uma interface FUNCIONAL
    // root = projecao (o que queremos da query, os dados que queremos)
    // query = obj query
    // cb = criteria builder (que vai possuir os criterios)

    public static Specification<Livro> isbnEqual(String isbn){
        // root.get("") --> campo que queremos comparar , parametro de comparacao
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("isbn"), isbn);
    }

    public static Specification<Livro> tituloLike(String titulo) {
        return (root, query, cb) ->
                // Vai comparar o campo do obj "titulo" em caixa alta...
                cb.like(cb.upper(root.get("titulo")),"%" +  // % entre o param para pesquisar em qualquer lugar
                titulo.toUpperCase() + "%");  // ...Com o param tbm em CAIXA ALTA
    }

    public static Specification<Livro> generoEqual(GeneroLivro genero) {
        return (root, query, criteriaBuilder) ->
                // No root.get sempre por o nome da propriedade e nao o nome do campo no BD
                criteriaBuilder.equal(root.get("genero"), genero);
    }

    public static Specification<Livro> anoPublicacaoEqual(Integer anoPublicacao) {
        // and to_char(data_publicacao, "YYYY") = :anoPublicacao
        return (root, query, cb) -> cb.equal(
                // nome da funcao || o que retorna || propriedade comparada (entidade) || expression literal
                cb.function("to_char", String.class, root.get("dataPublicacao"), cb.literal("YYYY")),

                // Vai ser uma comparacao de String
                anoPublicacao.toString());
    }
}
