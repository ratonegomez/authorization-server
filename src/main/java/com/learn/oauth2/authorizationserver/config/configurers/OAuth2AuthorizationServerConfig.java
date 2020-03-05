package com.learn.oauth2.authorizationserver.config.configurers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.InMemoryAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;

@Configuration
@EnableAuthorizationServer
public class OAuth2AuthorizationServerConfig implements AuthorizationServerConfigurer {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private InMemoryTokenStore inMemoryTokenStore;

    //  That will switch on the password grants type
    @Autowired
    private AuthenticationManager authenticationManager;

    //  Authorization code switch on
    @Autowired
    private UserDetailsService userDetailsService;

    //  Refresh token grant switch on
    @Autowired
    private AuthorizationCodeServices  authorizationCodeServices;

    @Override
    public void configure(AuthorizationServerSecurityConfigurer authorizationServerSecurityConfigurer) throws Exception {
        authorizationServerSecurityConfigurer.tokenKeyAccess("permitAll()").checkTokenAccess("isAuthenticated()");
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clientDetailsServiceConfigurer) throws Exception {
        clientDetailsServiceConfigurer.inMemory()
                .withClient("clientId")
                .secret(passwordEncoder.encode("password"))
                .scopes("read","write")
                .authorizedGrantTypes("password","refresh_token","authorization_code") ;
    }

    /***
     * Defines the authorization and token endpoints and the token services
     * AuthorizationEndpoint Default URL: /oauth/authorize
     * TokenEndpoint Default URL: /oauth/token
     * @param authorizationServerEndpointsConfigurer
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer authorizationServerEndpointsConfigurer) throws Exception {
        authorizationServerEndpointsConfigurer.tokenStore(inMemoryTokenStore)
                .userDetailsService(userDetailsService)
                .authorizationCodeServices(authorizationCodeServices)
                .authenticationManager(authenticationManager);
    }

    @Bean
    public AuthorizationCodeServices  authorizationCodeServices(){
        return new InMemoryAuthorizationCodeServices();
    }
}
