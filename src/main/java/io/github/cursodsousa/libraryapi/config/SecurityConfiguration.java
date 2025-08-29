package io.github.cursodsousa.libraryapi.config;

import io.github.cursodsousa.libraryapi.security.CustomUserDetailsService;
import io.github.cursodsousa.libraryapi.service.UsuarioService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity  // Por ser uma config de seguranca
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true)  // Habilita para fazer nos controllers (permissoes)
public class SecurityConfiguration {

    // Quando declarado esse Bean, desabilita o @Bean padrao e passa a atender as config deste Bean
    @Bean
    // Declarado esse SecurityFilterChain (bean), ele sobrescreve o SecurityFilterChain padrao (que habilitou o form de
    // login, autenticacao Basic...)
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)  // Permite que outras app facam uma REQUISICAO pro sistema
                // autenticacao via browser
//                .formLogin(configurer -> {  // Dizendo que a page de login eh esta
//                    configurer.loginPage("/login");
//                })
                .formLogin(Customizer.withDefaults())
                .httpBasic(Customizer.withDefaults())
                .authorizeHttpRequests(authorize -> {
                    // permite todos acessar /login sem estarem autenticados
                    authorize.requestMatchers("/login/**").permitAll();

                    // Permite todos cadastrarem seus usuarios.
                    authorize.requestMatchers(HttpMethod.POST, "/usuarios/**" ).permitAll();

                    // Para alem das requisicoes acima, deve estar pelo menos autenticado (se nao for user, nem admin)
                    authorize.anyRequest().authenticated(); // Qualquer Request pra essa API TEM que estar Autenticado

                    // Qualquer regra abaixo do anyRequest sera ignorada!!!
                })
                .oauth2Login(Customizer.withDefaults())
                .build();  // Para criar um SecurityFilterChain apartir do htpp, preciso chamar o *.build*
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // Gera um hash diferente para cada vez que criptografar (mesmo que seja a mesma senha)
        return new BCryptPasswordEncoder(10);  // numero de vezes que vai passar em cima do BCript
    }

    // Declaramos como Bean para ficar dentro da config do Spring Security
    @Bean
    // Gera instancia do UserDetails
    public UserDetailsService userDetailsService(UsuarioService usuarioService) {
        // Nao podemos salvar a senha hardEncoded, precisa de alguma codificacao na senha para seguranca e comparacao
//        UserDetails user1 = User.builder()
//                .username("usuario1")
//                .password(encoder.encode("123"))
//                .roles("USER")  // Roles geralmente em caixa alta
//                .build();
//
//        // Os dados do Usuario, Authentication eh o que fica no contexto do Spring Security
//        UserDetails user2 = User.builder()
//                .username("admin")
//                .password(encoder.encode("321"))
//                .roles("ADMIN")  // Roles geralmente em caixa alta
//                .build();
//
//        // Nao retorna a senha aleatoria pois agora temos um repositorio (EM MEMORIA) no qual ele busca os usuarios
//        return new InMemoryUserDetailsManager(user1, user2);

        return new CustomUserDetailsService(usuarioService);
    }
}
