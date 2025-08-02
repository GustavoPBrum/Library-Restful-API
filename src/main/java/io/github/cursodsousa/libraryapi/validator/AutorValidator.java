package io.github.cursodsousa.libraryapi.validator;

import io.github.cursodsousa.libraryapi.exceptions.RegistroDuplicadoException;
import io.github.cursodsousa.libraryapi.model.Autor;
import io.github.cursodsousa.libraryapi.repository.AutorRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component  // Para ser gerenciado pelo Spring e que possa ser injetado
public class AutorValidator {

    private AutorRepository repository;

    public AutorValidator(AutorRepository repository) {
        this.repository = repository;
    }

    public void validar(Autor autor) {
        if(existeAutorCadastrado(autor)){
            throw new RegistroDuplicadoException("Autor ja cadastrado!!!");
         }
    }

    private boolean existeAutorCadastrado(Autor autor){
        Optional<Autor> autorEncontrado = repository.findByNomeAndDataNascimentoAndNacionalidade(
                autor.getNome(), autor.getDataNascimento(), autor.getNacionalidade()
        );

        if(autor.getId() == null) {  // Se nao ha id, eh um novo autor
            return autorEncontrado.isPresent();  // Retorna true se ja existir alguem igual no BD
        }

        // Se ja possuir id, um autor existente. Agora ele compara o ID do autor que esta sendo atualizado
        // Com o ID encontrado no BD. Caso for diferente, vai ser um true pois vai estar duplicado
        // Se for o mesmo id, apenas uma atualizacao do mesmo autor
        return !autor.getId().equals(autorEncontrado.get().getId())  && autorEncontrado.isPresent();
    }
}
