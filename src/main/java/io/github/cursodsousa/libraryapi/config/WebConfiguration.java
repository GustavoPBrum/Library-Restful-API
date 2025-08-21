package io.github.cursodsousa.libraryapi.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc  // Habilitando MVC (model, view, controller) permitindo utilizar paginas web dentro da Aplicacao
public class WebConfiguration implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {

        // Nome da pag criada
        // Quando acessar a URl */login*, sera a pagina login.html
        registry.addViewController("/login").setViewName("login");
        registry.setOrder(Ordered.HIGHEST_PRECEDENCE);  // Mais alta precedencia
    }
}
