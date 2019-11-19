package com.learn.oauth2.authorizationserver.config.beans;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;

@Configuration
public class TokenBeanConfig {

    @Bean
    @Primary
    public DefaultTokenServices tokenServices(){
        final var defaultTokenServices=new DefaultTokenServices();
        defaultTokenServices.setTokenStore(tokenStore());
        return defaultTokenServices;
    }

    @Bean
    @Primary
    public InMemoryTokenStore tokenStore(){
        return new InMemoryTokenStore();
    }
}
