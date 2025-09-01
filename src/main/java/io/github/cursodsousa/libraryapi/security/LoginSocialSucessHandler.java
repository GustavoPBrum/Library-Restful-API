package io.github.cursodsousa.libraryapi.security;

import io.github.cursodsousa.libraryapi.model.Usuario;
import io.github.cursodsousa.libraryapi.service.UsuarioService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
// Implementacao de AutenticationSucessHandler
public class LoginSocialSucessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private final UsuarioService usuarioService;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) throws ServletException, IOException {

        // Downcasting de authentication (generico) para Authentication Auth2
        OAuth2AuthenticationToken auth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;

        OAuth2User oAuth2User = auth2AuthenticationToken.getPrincipal(); // Retorna um USER AUTH2

        String email = oAuth2User.getAttribute("email");

        Usuario usuario = usuarioService.obterPorEmail(email);

        // Upcasting (e guardamos na ref que antes tinha OAuth2Authentication o nosso CustomAuthentication)
        authentication = new CustomAuthentication(usuario);

        // A Auth no contexto do Spring Security eh a 'authentication'(param) e quero mudar para CustomAuthentication
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // O 'super' da continuidade para este request (passa para frente)
        super.onAuthenticationSuccess(request, response, authentication);
    }
}
