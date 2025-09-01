package io.github.cursodsousa.libraryapi.security;

import io.github.cursodsousa.libraryapi.model.Usuario;
import io.github.cursodsousa.libraryapi.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component  // Bean gerenciado pelo Spring Context
@RequiredArgsConstructor
// Atende as autenticacoes criadas, Basic e form login
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final UsuarioService usuarioService;
    private final PasswordEncoder encoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String login = authentication.getName();
        // Credentials eh um object, pois a senha pode ser muitos fatores (senha digitada, digital, facial...)
        String senhaDigitada = authentication.getCredentials().toString();

        Usuario usuarioEncontrado = usuarioService.obterPorLogin(login);

        if(usuarioEncontrado == null) {
            throw getErroUsuarioNaoEncontrado();
        }

        String senhaCriptografada = usuarioEncontrado.getSenha();

        boolean senhasBatem = encoder.matches(senhaDigitada, senhaCriptografada);

        if(senhasBatem) {
            return new CustomAuthentication(usuarioEncontrado);
        }

        throw getErroUsuarioNaoEncontrado();
    }

    private UsernameNotFoundException getErroUsuarioNaoEncontrado() {
        return new UsernameNotFoundException("Usuario e/ou senha incorretos! ");
    }

    @Override
    // UsernamePasswordAuthenticationToken -> que sera passado para o metodo authenticate
    public boolean supports(Class<?> authentication) {
        // Tipo de autenticacao suportada, no caso quando usuario digita usuario e a senha
        // Tanto no Basic quanto no form de login, vai criar uma instancia de *UsernamePasswordAuthenticationToken*
        return authentication.isAssignableFrom(UsernamePasswordAuthenticationToken.class);
    }
}
