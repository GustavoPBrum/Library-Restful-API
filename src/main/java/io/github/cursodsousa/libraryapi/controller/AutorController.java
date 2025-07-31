package io.github.cursodsousa.libraryapi.controller;

import io.github.cursodsousa.libraryapi.controller.dto.AutorDTO;
import io.github.cursodsousa.libraryapi.model.Autor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController  // Quando queremos transformar uma classe Spring em um controlador Rest
@RequestMapping("/autores")  //http://localhost:8080/autores  --> URL que este Controller vai ficar escutando
public class AutorController {

    //@RequestMapping(method = RequestMethod.POST)  // Ou desta forma com mais parametros
    @PostMapping
    public ResponseEntity salvar(@RequestBody AutorDTO autorDTO) {  // Indica que este objeto vai vir no corpo da request
        // Representa uma resposta
        return new ResponseEntity("Autor salvo com sucesso " + autorDTO, HttpStatus.CREATED);
    }
}
