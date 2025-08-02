package io.github.cursodsousa.libraryapi.controller;

import io.github.cursodsousa.libraryapi.controller.dto.AutorDTO;
import io.github.cursodsousa.libraryapi.controller.dto.ErroResposta;
import io.github.cursodsousa.libraryapi.exceptions.RegistroDuplicadoException;
import io.github.cursodsousa.libraryapi.model.Autor;
import io.github.cursodsousa.libraryapi.service.AutorService;
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
public class AutorController {

    private final AutorService service;

    public AutorController(AutorService service) {
        this.service = service;
    }

    //@RequestMapping(method = RequestMethod.POST)  // Ou desta forma com mais parametros
    @PostMapping
    public ResponseEntity<Object> salvar(@RequestBody AutorDTO autorDTO) {  // Indica que este objeto vai vir no corpo da request
        try {
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
        }catch (RegistroDuplicadoException e) {
            var erroDTO = ErroResposta.conflito(e.getMessage());
            return ResponseEntity.status(erroDTO.status()).body(erroDTO);
        }
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

    // Sempre na entrada e saida de dados utilizando o DTO, pois faz parte da camada representacional
    @GetMapping
    public ResponseEntity<List<AutorDTO>> pesquisa(
            // O uso do value eh por ter mais de um parametro, required por causa que nao sao obrigatorios
            @RequestParam(value = "nome", required = false) String nome,
            @RequestParam(value = "nacionalidade", required = false) String nacionalidade) {

        List<Autor> pesquisa = service.pesquisa(nome, nacionalidade);
        List<AutorDTO> lista = pesquisa
                .stream()
                .map(autor -> new AutorDTO(
                        autor.getId(),
                        autor.getNome(),
                        autor.getDataNascimento(),
                        autor.getNacionalidade()))
                .collect(Collectors.toList());  // Colleta a stream de autorDTO e transforma em list
        return ResponseEntity.ok(lista);
    }

    @PutMapping("{id}")
    public ResponseEntity<Void> atualizar(
            @PathVariable("id") String id,
            @RequestBody AutorDTO autorDTO) { // Transforma o JSON que veio no Body no autorDTO

        var idAutor = UUID.fromString(id);
        Optional<Autor> autorOptional = service.obterPorId(idAutor);

        if(autorOptional.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        var autor = autorOptional.get();
        autor.setNome(autorDTO.nome());
        autor.setNacionalidade(autorDTO.nacionalidade());
        autor.setDataNascimento(autorDTO.dataNascimento());

        service.atualizar(autor);

        return ResponseEntity.noContent().build();
    }
}
