package io.github.cursodsousa.libraryapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.web.SecurityFilterChain;

import java.time.Duration;

// A configuração de um Authorization Server nada mais é do que a configuração de um SecurityFilterChain

@Configuration
@EnableWebSecurity  // Caso seja separado a aplicação de Authorization Server e Resource Server (API)
public class AuthorizationServerConfiguration {

    @Bean
    @Order(1) // O primeiro, o principal na cadeia de filtros do Spring Security (SecurityFilterChain)
    public SecurityFilterChain authServerSecurityFilterChain(HttpSecurity http) throws Exception{

        //Habilitado Authorization Server
        // Classe/Estrutura ambiente
        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);

        // Interface/Modifica ambiente
        http.getConfigurer(OAuth2AuthorizationServerConfigurer.class)
                // Plugin do OAuth2 que permite que com o Token, consigamos informações do Token (quem gerou, Usuario)
                .oidc(Customizer.withDefaults());

        // Configurando Authorization Server para validar os Tokens do Resource Server (API)
        http.oauth2ResourceServer(oauth2Rs -> oauth2Rs.jwt(Customizer.withDefaults()));

        // Forma de autenticação
        http.formLogin(configurer -> configurer.loginPage("/login"));

        return http.build();
    }

    @Bean
    // Ele que vai codificar e validar as senhas
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    @Bean
    public TokenSettings tokenSettings(){
        return TokenSettings.builder()
                .accessTokenFormat(OAuth2TokenFormat.SELF_CONTAINED)
                .accessTokenTimeToLive(Duration.ofMinutes(60))  // Tempo em minutos
                .build();
    }

    @Bean
    public ClientSettings clientSettings(){
        return ClientSettings.builder()
                .requireAuthorizationConsent(false)  // Sem tela de consentimento para acessar as informações
                .build();
    }
}
