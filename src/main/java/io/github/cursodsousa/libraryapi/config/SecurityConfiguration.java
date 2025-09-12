package io.github.cursodsousa.libraryapi.config;

import io.github.cursodsousa.libraryapi.security.CustomUserDetailsService;
import io.github.cursodsousa.libraryapi.security.LoginSocialSuccessHandler;
import io.github.cursodsousa.libraryapi.service.UsuarioService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity  // Por ser uma config de seguranca
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true)  // Habilita para fazer nos controllers (permissoes)
public class SecurityConfiguration {  // Nosso Resource Server!!!

    // Quando declarado esse Bean, desabilita o @Bean padrao e passa a atender as config deste Bean
    @Bean
    // Declarado esse SecurityFilterChain (bean), ele sobrescreve o SecurityFilterChain padrao (que habilitou o form de
    // login, autenticacao Basic...)
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            LoginSocialSuccessHandler successHandler) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)  // Permite que outras app facam uma REQUISICAO pro sistema
                // autenticacao via browser
                .formLogin(configurer -> {  // Dizendo que a page de login eh esta
                    configurer.loginPage("/login");
                })
                .httpBasic(Customizer.withDefaults())
                .authorizeHttpRequests(authorize -> {
                    // permite todos acessar /login sem estarem autenticados
                    authorize.requestMatchers("/login/**").permitAll();

                    // Permite todos cadastrarem seus usuarios.
                    authorize.requestMatchers(HttpMethod.POST, "/usuarios/**").permitAll();

                    // Para alem das requisicoes acima, deve estar pelo menos autenticado (se nao for user, nem admin)
                    authorize.anyRequest().authenticated(); // Qualquer Request pra essa API TEM que estar Autenticado

                    // Qualquer regra abaixo do anyRequest sera ignorada!!!
                })
                // Quando fizer uma autenticação com sucesso, chama a classe informada
                .oauth2Login(oauth2 ->
                        oauth2
                                .loginPage("/login")
                                .successHandler(successHandler))

                .oauth2ResourceServer(oauth2RS -> oauth2RS.jwt(Customizer.withDefaults()))  // JWT pra validar usuario

                .build();  // Para criar um SecurityFilterChain apartir do htpp, preciso chamar o *.build*
    }


    // Configura o prefixo 'ROLE'
    @Bean
    public GrantedAuthorityDefaults grantedAuthorityDefaults() {
        // Remove o prefixo "ROLE_" que o Spring coloca nas roles
        return new GrantedAuthorityDefaults("");
    }

    // Configura no Token JWT, o prefixo 'SCOPE' será removido
    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter(){
        var authoritiesConverter = new JwtGrantedAuthoritiesConverter();
        authoritiesConverter.setAuthorityPrefix("");

        // Obj a retornar
        var converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(authoritiesConverter);

        return converter;
    }
}