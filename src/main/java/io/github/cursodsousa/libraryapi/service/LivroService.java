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
                                String nomeAutor,
                                String titulo,
                                GeneroLivro genero,
                                Integer anoPublicação){

        Specification<Livro> specs = null; // Encontre todos dada a Specification passada
        // Apos implementar Specification no Repository, ele permite passa-lo como parametro
        return repository.findAll(specs);
    }
}
