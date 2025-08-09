package io.github.cursodsousa.libraryapi.controller.mappers;

import io.github.cursodsousa.libraryapi.controller.dto.CadastroLivroDTO;
import io.github.cursodsousa.libraryapi.controller.dto.ResultadoPesquisaLivroDTO;
import io.github.cursodsousa.libraryapi.model.Livro;
import io.github.cursodsousa.libraryapi.repository.AutorRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

// Quero utilizar outro mapper para fazer o mapeamento (AutorMapper para mapear a entidade Autor)
@Mapper(componentModel = "spring", uses = AutorMapper.class)  // Pode utilizar AutorMapper onde precisar nos mapeamentos
public abstract class LivroMapper {  // Permite utilizacao de classes abstratas

    @Autowired
    AutorRepository repository;

    @Mapping(target = "autor", expression = "java( repository.findById(dto.idAutor()).orElse(null) )")
    public abstract Livro toEntity(CadastroLivroDTO dto);

    // ResultadoPesquisaLivroDTO precisa de um autorDTO para retornar e livro possui apenas a entidade Autor.
    public abstract ResultadoPesquisaLivroDTO toDTO(Livro livro);
}
