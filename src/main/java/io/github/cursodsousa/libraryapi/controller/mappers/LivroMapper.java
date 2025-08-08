package io.github.cursodsousa.libraryapi.controller.mappers;

import io.github.cursodsousa.libraryapi.controller.dto.CadastroLivroDTO;
import io.github.cursodsousa.libraryapi.model.Livro;
import io.github.cursodsousa.libraryapi.repository.AutorRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class LivroMapper {  // Permite utilizacao de classes abstratas

    @Autowired
    AutorRepository repository;

    @Mapping(target = "autor", expression = "java( repository.findById(dto.idAutor()).orElse(null) )")
    public abstract Livro toEntity(CadastroLivroDTO dto);
}
