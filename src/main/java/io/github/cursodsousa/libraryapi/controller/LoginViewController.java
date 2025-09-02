package io.github.cursodsousa.libraryapi.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller // Controller -> para paginas WEB
public class LoginViewController {

    // Quando dentro de um @Controller, ele retorna uma String para indicar qual paginar ir quando chamar essa request
    @GetMapping("/login")
    public String paginaLogin(){
        return "login";
    }

    @GetMapping("/")
    @ResponseBody  // Pega o retorno (String) e coloca no corpo da resposta nao tendo que retornar uma pagina WEB
    public String paginaHome(Authentication authentication){
        if(authentication instanceof CustomAuthentication customAuth) {
            System.out.println(customAuth.getUsuario());
        }

        return "Ola " + authentication.getName();
    }
}
