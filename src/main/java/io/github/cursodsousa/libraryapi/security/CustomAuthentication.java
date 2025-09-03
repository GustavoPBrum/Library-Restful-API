package io.github.cursodsousa.libraryapi.security;

import io.github.cursodsousa.libraryapi.model.Usuario;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Getter // Pegar o usuario que esta logado
public class CustomAuthentication implements Authentication {

    // Quando criar essa Authentication, passar o usuario autenticado
    private final Usuario usuario;

    @Override
    public Collection<GrantedAuthority> getAuthorities() {

        // Mapear nossa lista de Strings (roles) para GrantedAuthority (uma interface que retorna apenas a Authority)
        return this
                .usuario
                .getRoles()
                // role(string) -> SimpleGrantedAuthority (interface que retorna a Authority)
                .stream().map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toList());
    }

    @Override // Credentials eh a senha do Usuario, nesse caso ja estara autenticado e nao retornara a senha
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getDetails() {
        return usuario;
    }

    @Override
    public Object getPrincipal() {
        return usuario;
    }

    @Override
    public boolean isAuthenticated() {
        return true; // Caso esteja 'false', nunca conseguira logar
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {

    }

    @Override
    public String getName() {
        return usuario.getLogin();
    }
}
