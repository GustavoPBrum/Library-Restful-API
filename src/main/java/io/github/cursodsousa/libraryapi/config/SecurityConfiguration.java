package io.github.cursodsousa.libraryapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
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
                .httpBasic(Customizer.withDefaults())
                .authorizeHttpRequests(authoize -> {  // Qualquer Request pra essa API TEM que estar Autenticado
                    authoize.anyRequest().authenticated();
                })
                .build();  // Para criar um SecurityFilterChain apartir do htpp, preciso chamar o *.build*
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // Gera um hash diferente para cada vez que criptografar (mesmo que seja a mesma senha)
        return new BCryptPasswordEncoder(10);  // numero de vezes que vai passar em cima do BCript
    }

    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder encoder) {
        // Nao podemos salvar a senha hardEncoded, precisa de alguma codificacao na senha para seguranca e comparacao
        UserDetails user1 = User.builder()
                .username("usuario1")
                .password(encoder.encode("123"))
                .roles("USER")  // Roles geralmente em caixa alta
                .build();

        UserDetails user2 = User.builder()
                .username("admin")
                .password(encoder.encode("321"))
                .roles("ADMIN")  // Roles geralmente em caixa alta
                .build();

        // Nao retorna a senha aleatoria pois agora temos um repositorio (EM MEMORIA) no qual ele busca os usuarios
        return new InMemoryUserDetailsManager(user1, user2);
    }
}
