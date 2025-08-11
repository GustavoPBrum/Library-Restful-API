package io.github.cursodsousa.libraryapi.service;

import io.github.cursodsousa.libraryapi.model.GeneroLivro;
import io.github.cursodsousa.libraryapi.model.Livro;
import io.github.cursodsousa.libraryapi.repository.LivroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

// Podemos agora usar diretamente os metodos staticos
import static io.github.cursodsousa.libraryapi.repository.specs.LivroSpecs.*;

@Service
@RequiredArgsConstructor
public class LivroService {

    private final LivroRepository repository;

    public Livro salvar(Livro livro) {
        return repository.save(livro);
    }

    public Optional<Livro> obterPorId(UUID id) {
        return repository.findById(id);
    }

    public void deletar(Livro livro){
        repository.delete(livro);
    }

    // isbn, titulo, nome autor, genero, ano de publicação
    public List<Livro> pesquisa(String isbn,
                                String titulo,
                                String nomeAutor,
                                GeneroLivro genero,
                                Integer anoPublicação){

        // Conjunction -> nada mais eh do que o criterio verdadeiro (retorna 0 = 0)
        // select * from Livro where 0 = 0
        Specification<Livro> specs = Specification.where((root, query, cb) -> cb.conjunction());

        if (isbn != null) {
            // Por spec ser IMUTAVEL, para ter um novo CRITERIO tem que receber ela mesma com .and
            specs = specs.and(isbnEqual(isbn));
        }


        if (titulo != null) {
            specs = specs.and(tituloLike(titulo));
        }

        if (genero != null) {
            specs = specs.and(generoEqual(genero));
        }

        return repository.findAll(specs);  // Encontre todos baseado nas Specifications passadas
    }
}
