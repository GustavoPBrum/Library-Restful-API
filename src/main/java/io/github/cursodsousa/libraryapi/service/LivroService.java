package io.github.cursodsousa.libraryapi.service;

import io.github.cursodsousa.libraryapi.model.GeneroLivro;
import io.github.cursodsousa.libraryapi.model.Livro;
import io.github.cursodsousa.libraryapi.repository.LivroRepository;
import io.github.cursodsousa.libraryapi.validator.LivroValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    private final LivroValidator validator;

    public Livro salvar(Livro livro) {
        validator.validar(livro);
        return repository.save(livro);
    }

    public Optional<Livro> obterPorId(UUID id) {
        return repository.findById(id);
    }

    public void deletar(Livro livro){
        repository.delete(livro);
    }

    // isbn, titulo, nome autor, genero, ano de publicação
    public Page<Livro> pesquisa(String isbn,
                                String titulo,
                                String nomeAutor,
                                GeneroLivro genero,
                                Integer anoPublicação,
                                Integer pagina,
                                Integer tamanhoPagina){

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

        if (anoPublicação != null) {
            specs = specs.and(anoPublicacaoEqual(anoPublicação));
        }

        if(nomeAutor != null) {
            specs = specs.and(nomeAutorLike(nomeAutor));
        }

        Pageable pageRequest = PageRequest.of(pagina, tamanhoPagina);

        // Specs -> parametros da pesquisa
        // pageRequest-> parametros da pagina
        return repository.findAll(specs, pageRequest);  // Encontre todos baseado nas Specifications passadas
    }

    public void atualizar(Livro livro) {
        if(livro.getId() == null) {
            throw new IllegalArgumentException("Para atualizar, e necessario que o livro ja esteja salvo.");
        }

        validator.validar(livro);
        repository.save(livro);
    }
}
