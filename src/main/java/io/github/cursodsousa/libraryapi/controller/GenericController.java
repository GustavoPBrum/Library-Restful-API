package io.github.cursodsousa.libraryapi.controller;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

public interface GenericController {  // URL para acesso a dados de alguma entidade criada

    default URI gerarHeaderLocation(UUID id) {

        // Para declarar um metodo com um corpo dentro de uma interface, precisa ser como *DEFAULT*
        return ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(id)
                .toUri();
    }
}
