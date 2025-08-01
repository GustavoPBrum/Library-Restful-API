package io.github.cursodsousa.libraryapi.controller;

import io.github.cursodsousa.libraryapi.controller.dto.AutorDTO;
import io.github.cursodsousa.libraryapi.model.Autor;
import io.github.cursodsousa.libraryapi.service.AutorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.annotation.processing.Generated;
import java.net.URI;
import java.util.Optional;
import java.util.UUID;

@RestController  // Quando queremos transformar uma classe Spring em um controlador Rest
@RequestMapping("/autores")  //http://localhost:8080/autores  --> URL que este Controller vai ficar escutando
public class AutorController {

    private final AutorService service;

    public AutorController(AutorService service) {
        this.service = service;
    }

    //@RequestMapping(method = RequestMethod.POST)  // Ou desta forma com mais parametros
    @PostMapping
    public ResponseEntity<Void> salvar(@RequestBody AutorDTO autorDTO) {  // Indica que este objeto vai vir no corpo da request
        Autor autorEntidade = autorDTO.mapearParaAutor();
        service.salvar(autorEntidade);

        // Pega os dados da requisicao atual para criar nova URL, pois ela pega o DOMINIO e PATH da API
        URI location = ServletUriComponentsBuilder
                                .fromCurrentRequest()  // Pega o caminho da request
                                .path("/{id}")  // O que sera adicionado
                                .buildAndExpand(autorEntidade.getId())  // A entidade pega a URI criada
                                .toUri();

        // Representa uma resposta
        return ResponseEntity.created(location).build();
    }

    @GetMapping("{id}")  // Optional pois pode existir ou nao
    public ResponseEntity<AutorDTO> obterDetalhes(@PathVariable("id") String id){
        var idAutor = UUID.fromString(id);
        Optional<Autor> autorOptional = service.obterPorId(idAutor);  // Pegamos os dados do Autor Entidade

        if(autorOptional.isPresent()){
            Autor autor = autorOptional.get();  // Retorna o obj de dentro do Optional
            AutorDTO dto = new AutorDTO(
                    autor.getId(),
                    autor.getNome(),
                    autor.getDataNascimento(),
                    autor.getNacionalidade());

            // Retornamos um autorDTO
            return ResponseEntity.ok(dto);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deletar(@PathVariable("id") String id) {
        var idAutor = UUID.fromString(id);
        Optional<Autor> autorOptional = service.obterPorId(idAutor);

        if(autorOptional.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        service.deletar(autorOptional.get());
        return ResponseEntity.noContent().build();
    }
}
