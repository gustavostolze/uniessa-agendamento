package com.uniessa.agendamento.config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;
import java.net.URI;
import java.util.Arrays;
@Configuration
public class CorsConfig implements WebMvcConfigurer {
    private final String[] origins;
    public CorsConfig(@Value("${app.cors.allowed-origins}") String value) {
        origins=Arrays.stream(value.split(",")).map(String::trim).toArray(String[]::new);
        for (String origin: origins) {
            URI uri=URI.create(origin);
            if (!("http".equals(uri.getScheme()) || "https".equals(uri.getScheme()))
                || uri.getHost()==null || uri.getUserInfo()!=null || uri.getQuery()!=null
                || uri.getFragment()!=null || (uri.getPath()!=null && !uri.getPath().isEmpty()))
                throw new IllegalArgumentException("Configure origens CORS explícitas, sem caminho ou barra final.");
        }
    }
    @Override public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/agendamento").allowedOrigins(origins)
            .allowedMethods("GET", "POST", "OPTIONS").allowedHeaders("Content-Type", "Accept")
            .allowCredentials(false).maxAge(600);
    }
}
