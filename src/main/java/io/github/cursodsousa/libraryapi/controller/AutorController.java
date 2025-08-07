package io.github.cursodsousa.libraryapi.controller;

import io.github.cursodsousa.libraryapi.controller.dto.AutorDTO;
import io.github.cursodsousa.libraryapi.controller.dto.ErroResposta;
import io.github.cursodsousa.libraryapi.controller.mappers.AutorMapper;
import io.github.cursodsousa.libraryapi.exceptions.OperacaoNaoPermitidaException;
import io.github.cursodsousa.libraryapi.exceptions.RegistroDuplicadoException;
import io.github.cursodsousa.libraryapi.model.Autor;
import io.github.cursodsousa.libraryapi.service.AutorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.annotation.processing.Generated;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController  // Quando queremos transformar uma classe Spring em um controlador Rest
@RequestMapping("/autores")  //http://localhost:8080/autores  --> URL que este Controller vai ficar escutando
@RequiredArgsConstructor
public class AutorController {

    private final AutorService service;

    private final AutorMapper mapper;

    //@RequestMapping(method = RequestMethod.POST)  // Ou desta forma com mais parametros
    @PostMapping                        // Indica que este objeto vai vir no corpo da request
    public ResponseEntity<Object> salvar(@RequestBody @Valid AutorDTO dto) {  // Valid ja faz a validacao no comeco
        try {
            Autor autor = mapper.toEntity(dto);
            service.salvar(autor);

            // Pega os dados da requisicao atual para criar nova URL, pois ela pega o DOMINIO e PATH da API
            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()  // Pega o caminho da request
                    .path("/{id}")  // O que sera adicionado
                    .buildAndExpand(autor.getId())  // A entidade pega a URI criada
                    .toUri();

            // Representa uma resposta
            return ResponseEntity.created(location).build();
        }catch (RegistroDuplicadoException e) {
            var erroDTO = ErroResposta.conflito(e.getMessage());
            return ResponseEntity.status(erroDTO.status()).body(erroDTO);
        }
    }

    @GetMapping("{id}")  // Optional pois pode existir ou nao
    public ResponseEntity<AutorDTO> obterDetalhes(@PathVariable("id") String id){
        var idAutor = UUID.fromString(id);

        return service
                .obterPorId(idAutor)
                .map(autor -> {
                 AutorDTO dto = mapper.toDTO(autor);
                 return ResponseEntity.ok(dto);
                })
                .orElseGet( () -> ResponseEntity.notFound().build());  // Retorna a func sem parametro
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Object> deletar(@PathVariable("id") String id) {
        try {
            var idAutor = UUID.fromString(id);
            Optional<Autor> autorOptional = service.obterPorId(idAutor);

            if (autorOptional.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            service.deletar(autorOptional.get());
            return ResponseEntity.noContent().build();
        } catch (OperacaoNaoPermitidaException e) {
            var erroReposta = ErroResposta.respostaPadrao(e.getMessage());
            return ResponseEntity.status(erroReposta.status()).body(erroReposta);
        }
    }

    // Sempre na entrada e saida de dados utilizando o DTO, pois faz parte da camada representacional
    @GetMapping
    public ResponseEntity<List<AutorDTO>> pesquisa(
            // // Caso queira nomes diferentes na URL, usar value = "...", required por causa que nao sao obrigatorios
            @RequestParam(value = "nome", required = false) String nome,
            @RequestParam(value = "nacionalidade", required = false) String nacionalidade) {

        List<Autor> pesquisa = service.pesquisaByExample(nome, nacionalidade);
        List<AutorDTO> lista = pesquisa
                .stream()
                // Pode usar dessa forma pois o parametro de ambos sao iguais. Entra autor/dto sai
                .map(mapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(lista);
    }

    @PutMapping("{id}")
    public ResponseEntity<Object> atualizar(
            @PathVariable("id") String id,
            @RequestBody @Valid AutorDTO autorDTO) { // Transforma o JSON que veio no Body no autorDTO

        try {

            var idAutor = UUID.fromString(id);
            Optional<Autor> autorOptional = service.obterPorId(idAutor);

            if (autorOptional.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            var autor = autorOptional.get();
            autor.setNome(autorDTO.nome());
            autor.setNacionalidade(autorDTO.nacionalidade());
            autor.setDataNascimento(autorDTO.dataNascimento());

            service.atualizar(autor);

            return ResponseEntity.noContent().build();
        } catch (RegistroDuplicadoException e) {
            var erroDTO = ErroResposta.conflito(e.getMessage());
            return ResponseEntity.status(erroDTO.status()).body(erroDTO);
        }
    }
}
