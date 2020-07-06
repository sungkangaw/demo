package com.example.demo.context;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;

@Configuration
@EnableAuthorizationServer
public class OAuth2Config extends AuthorizationServerConfigurerAdapter {


    @Value("${demo.oauth2.client_id}")
    private String CLIENT_ID;

    @Value("${demo.oauth2.client_secret}")
    private String CLIENT_SECRET;

    @Value("${demo.oauth2.grant_type}")
    private String GRANT_TYPE;

    private TokenStore tokenStore;

    private AuthenticationManager authenticationManager;

    private BCryptPasswordEncoder encoder;

    public OAuth2Config(TokenStore tokenStore,
                        AuthenticationManager authenticationManager,
                        BCryptPasswordEncoder encoder) {
        this.tokenStore = tokenStore;
        this.authenticationManager = authenticationManager;
        this.encoder = encoder;
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer configurer) throws Exception {

        configurer
                .inMemory()
                .withClient(CLIENT_ID)
                .secret(encoder.encode(CLIENT_SECRET))
                .authorizedGrantTypes(GRANT_TYPE, "authorization_code", "refresh_token", "implicit" )
                .scopes("read","write","trust")
                .accessTokenValiditySeconds(60*60).
                refreshTokenValiditySeconds(6*60*60);
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.tokenStore(tokenStore)
                .authenticationManager(authenticationManager);
    }
}
