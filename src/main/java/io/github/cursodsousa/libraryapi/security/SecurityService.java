package io.github.cursodsousa.libraryapi.security;

import io.github.cursodsousa.libraryapi.model.Usuario;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;


@Component // Permite nossa classe ser registrada como um @Bean no contexto SPRING
// Bean -> Um objeto gerenciado pelo Spring (vai criar a instancia, cuidar do ciclo de vida, das dependencias, unica
// instancia em toda aplicacao (singleton) e permite ser injetado)
public class  SecurityService {


    // Método que obtém usuario logado apenas de CustomAuthentication
    public Usuario obterUsuarioLogado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Apenas Authentications cujo seja nossa CustomAuthentication que possui o Usuario não necessitando ir no BD
        if(authentication instanceof CustomAuthentication customAuth) {
            return customAuth.getUsuario();
        }

        return null;
    }
}
