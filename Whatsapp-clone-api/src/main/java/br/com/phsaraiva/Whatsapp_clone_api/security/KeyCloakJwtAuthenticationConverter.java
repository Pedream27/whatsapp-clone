package br.com.phsaraiva.Whatsapp_clone_api.security;

import lombok.NonNull;
import org.springframework.context.annotation.Bean;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class KeyCloakJwtAuthenticationConverter implements Converter<Jwt,  AbstractAuthenticationToken> {

    @Override
    public AbstractAuthenticationToken convert(@NonNull  Jwt source) {
        return new JwtAuthenticationToken(source,
                Stream.concat( new JwtGrantedAuthoritiesConverter().convert(source).stream(),
                        extractResourceRoles(source).stream()).collect(Collectors.toSet()));
    }

    private Collection<? extends GrantedAuthority> extractResourceRoles(@NonNull Jwt jwt) {
        var resourceAcess = new HashMap<>(jwt.getClaim("resource_access"));
        var eternal = (Map<String, List<String>>)resourceAcess.get("account");
        var roles = eternal.get("roles");
        return roles.stream().map(role -> new SimpleGrantedAuthority("ROLE_" + role.replace("-", "_")))
                .collect(Collectors.toSet());
    }

    @Bean
    public CorsFilter corsFilter() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        final CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.setAllowedOrigins(Collections.singletonList("http://localhost:4200"));
        config.setAllowedHeaders(Arrays.asList(
                HttpHeaders.ORIGIN,
                HttpHeaders.CONTENT_TYPE,
                HttpHeaders.ACCEPT,
                HttpHeaders.AUTHORIZATION
        ));

        config.setAllowedMethods(Arrays.asList(
                "GET",
                "POST",
                "PUT",
                "DELETE",
                "OPTIONS",
                "PATCH"
                ));
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);

    }
}
