package io.github.cursodsousa.libraryapi.security;

import io.github.cursodsousa.libraryapi.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
// Parte que conecta com OAuth2
public class CustomRegisteredClientRepository implements RegisteredClientRepository {

    private final ClientService service;

    private final TokenSettings tokenSettings;
    private final ClientSettings clientSettings;

    @Override
    public void save(RegisteredClient registeredClient) {
    }

    @Override
    public RegisteredClient findById(String id) {
        return null;
    }

    @Override
    public RegisteredClient findByClientId(String clientId) {
        var client = service.obterPorClientId(clientId);

        if(client == null) {
            return null;
        }

        // Configurações de Token e Client para este Client
        return RegisteredClient
                .withId(client.getId().toString())
                .clientId(client.getClientId())
                .clientSecret(client.getClientSecret())
                .redirectUri(client.getRedirectURI())
                .scope(client.getScope())
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                .tokenSettings(tokenSettings)
                .clientSettings(clientSettings)
                .build();
    }
}
