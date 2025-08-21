package io.github.cursodsousa.libraryapi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller // Controller -> para paginas WEB
public class LoginViewController {

    // Quando dentro de um @Controller, ele retorna uma String para indicar qual paginar ir quando chamar essa request
    @GetMapping("/login")
    public String paginaLogin(){
        return "login";
    }
}
