package io.github.cursodsousa.libraryapi.repository;

import io.github.cursodsousa.libraryapi.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;


                                                        // Entidade / Tipo do campo da Primary Key
public interface ClientRepository extends JpaRepository<Client, UUID> {

    Client findByClientId(String id);
}
