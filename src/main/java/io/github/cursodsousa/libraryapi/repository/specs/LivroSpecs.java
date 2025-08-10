package io.github.cursodsousa.libraryapi.repository.specs;

import io.github.cursodsousa.libraryapi.model.GeneroLivro;
import io.github.cursodsousa.libraryapi.model.Livro;
import org.springframework.data.jpa.domain.Specification;

public class LivroSpecs {

    // Specification nada mais eh do que uma interface FUNCIONAL
    // root = dados que queremos
    // query = obj criteria query
    // cb = criteria builder

    public static Specification<Livro> isbnEqual(String isbn){
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("isbn"), isbn);
    }

    public static Specification<Livro> tituloLike(String titulo) {
        // Compara o meu titulo na no banco em caixa alta
        return (root, query, criteriaBuilder) ->
                // Vai selecionar contendo qualquer parte da String
                criteriaBuilder.like(criteriaBuilder.upper(root.get("titulo")),  "%" +
                titulo.toUpperCase() + "%");  // Compara com o titulo do param em caixa alta
    }

    public static Specification<Livro> generoEqual(GeneroLivro genero) {
        return (root, query, criteriaBuilder) ->
                // No root.get sempre por o nome da propriedade e nao o nome do campo no BD
                criteriaBuilder.equal(root.get("genero"), genero);
    }
}
