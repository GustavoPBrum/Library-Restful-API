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
@Getter
public class CustomAuthentication  implements Authentication {

    // Quando criar essa Authentication, tem que passar o Usuario autenticado
    private final Usuario usuario;

    @Override
    // Este metodo que ira autorizar o usuario
    public Collection<GrantedAuthority> getAuthorities() {
        return this.usuario
                .getRoles()  // Transformado uma lista de String, para uma lista de SimpleGrantedAuthority
                .stream().map(SimpleGrantedAuthority::new)// role = authority e add na lista
                .collect(Collectors.toList());
    }

    @Override
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
        return true; // Caso esteja falso, nunca conseguira logar!!!
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {

    }

    @Override
    public String getName() {
        return usuario.getLogin();
    }
}
