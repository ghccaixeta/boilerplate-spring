package com.example.demo.config;

import java.util.Optional;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.web.SecurityFilterChain;

import com.example.demo.entities.OAuthClient;
import com.example.demo.repository.OauthClientRepository;

@Configuration
@EnableWebSecurity
public class AuthorizationServerConfig {

  @Bean
  PasswordEncoder passwordEncoder() {
    return new PasswordEncoder() {
      @Override
      public String encode(CharSequence rawPassword) {
        return rawPassword.toString();
      }

      @Override
      public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return rawPassword.toString().equals(encodedPassword);
      }
    };
  }

  @Bean
  SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http) throws Exception {
    OAuth2AuthorizationServerConfigurer authorizationServerConfigurer = new OAuth2AuthorizationServerConfigurer();

    http
        .securityMatcher("/oauth2/**")
        .authorizeHttpRequests(authorize -> authorize
            .requestMatchers(HttpMethod.POST, "/oauth2/token").permitAll()
            .anyRequest().authenticated())
        .csrf(csrf -> csrf.disable())
        .with(authorizationServerConfigurer, Customizer.withDefaults())
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

    return http.build();
  }

  @Bean
  RegisteredClientRepository registeredClientRepository(OauthClientRepository repository) {
    return new RegisteredClientRepository() {

      @Override
      public void save(RegisteredClient registeredClient) {
        throw new UnsupportedOperationException("Unimplemented method 'save'");
      }

      @Override
      public RegisteredClient findById(String id) {
        throw new UnsupportedOperationException("Unimplemented method 'findById'");
      }

      @Override
      public RegisteredClient findByClientId(String clientId) {
        Optional<OAuthClient> result = repository.findByClientId(clientId);

        System.out.println("clientId: " + clientId);

        if (result.isEmpty()) {
          return null;
        }

        OAuthClient client = result.get();

        return RegisteredClient.withId(client.getId().toString())
            .clientId(client.getClientId())
            .clientName(client.getClientName())
            .clientSecret(client.getClientSecret())
            .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
            .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST)
            .build();
      }
    };
  }
}
