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


@Component
@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final UsuarioService usuarioService;  // Para verificar o login de usuario
    private final PasswordEncoder encoder;

    // Metodo que ira autenticar, faremos o que o Spring Security fara por debaixo dos panos (testar a senha)
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String login = authentication.getName();
        String senhaDigitada = authentication.getCredentials().toString(); // Credentials retorna um Object

        Usuario usuarioEncontrado = usuarioService.obterPorLogin(login);

        if(usuarioEncontrado == null) {
            throw getErroUsuarioNaoEncontrado();
        }

        // Senha salva no banco de dados para comparar com a senhaDigitada
        String senhaCriptografada = usuarioEncontrado.getSenha();

        // rawPassword, encodedPassword
        boolean senhasBatem = encoder.matches(senhaDigitada, senhaCriptografada);

        if(senhasBatem) {
            return new CustomAuthentication(usuarioEncontrado);
        }

        throw getErroUsuarioNaoEncontrado();
    }

    private UsernameNotFoundException getErroUsuarioNaoEncontrado() {
        return new UsernameNotFoundException("Usuario e/ou senha incorretos!");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        // Criar um objeto a partir do login e senha, e passara o objeto para o nosso Provider verificar se eh suportado
        return authentication.isAssignableFrom(UsernamePasswordAuthenticationToken.class);
    }
}
