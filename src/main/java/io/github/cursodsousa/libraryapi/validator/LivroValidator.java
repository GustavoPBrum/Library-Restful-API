package io.github.cursodsousa.libraryapi.validator;

import io.github.cursodsousa.libraryapi.exceptions.CampoInvalidoException;
import io.github.cursodsousa.libraryapi.exceptions.RegistroDuplicadoException;
import io.github.cursodsousa.libraryapi.model.Livro;
import io.github.cursodsousa.libraryapi.repository.LivroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor // Cria um CONSTRUTOR com todas as propriedades FINAL e cria em tempo de comp.
public class LivroValidator {

    private static final int ANO_EXIGENCIA_PRECO = 2020;

    private final LivroRepository repository;

    public void validar(Livro livro) {
        if(existeLivroComIsbn(livro)) {
            throw new RegistroDuplicadoException("ISBN ja cadastrado!");
        }

        if(isPrecoObrigatorioNulo(livro)) {
            throw new CampoInvalidoException("preco",
                    "Livros com Data de Publicacao 2020 para frente, preco eh obrigatorio");
        }
    }

    private boolean existeLivroComIsbn(Livro livro) {
        Optional<Livro> livroEncontrado = repository.findByIsbn(livro.getIsbn());

        if(livro.getId() == null) {
            return livroEncontrado.isPresent();  // O **livro** esta sendo cadastrado mas ja existe este ISBN no BD
        }

        return livroEncontrado
                .map(Livro::getId)
                .stream()
                // Se o id do **livroEncontrado** tem o id diferente do **livro** da att
                // Caso nao tenha, estaremos att o mesmo livro
                .anyMatch(id -> !id.equals(livro.getId()));
    }

    private boolean isPrecoObrigatorioNulo(Livro livro) {
        return livro.getPreco() == null &&
                livro.getDataPublicacao().getYear() >= ANO_EXIGENCIA_PRECO;
    }
}
