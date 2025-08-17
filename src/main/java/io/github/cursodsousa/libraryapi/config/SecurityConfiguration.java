package io.github.cursodsousa.libraryapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity  // Por ser uma config de seguranca
public class SecurityConfiguration {

    // Quando declarado esse Bean, desabilita o @Bean padrao e passa a atender as config deste Bean
    @Bean
    // Declarado esse SecurityFilterChain (bean), ele sobrescreve o SecurityFilterChain padrao (que habilitou o form de
    // login, autenticacao Basic...)
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)  // Permite que outras app facam uma REQUISICAO pro sistema
                // autenticacao via browser
                .formLogin(configurer -> {  // Dizendo que a page de login eh esta
                    configurer.loginPage("/login").permitAll();
                })  // Formlogin sem config extra, ele add o formulario padrao
                // Postman
                //.httpBasic(Customizer.withDefaults())
                .authorizeHttpRequests(authoize -> {  // Qualquer Request pra essa API TEM que estar Autenticado
                    authoize.anyRequest().authenticated();
                })
                .build();  // Para criar um SecurityFilterChain apartir do htpp, preciso chamar o *.build*
    }
}
