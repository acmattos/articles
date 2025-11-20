package br.com.acmattos.article.docker.camel.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "couchdb")
public record DbServerConfig(
    String host,
    String resource,
    Integer size,
    boolean secure,
    boolean sslInsecure,
    String username,
    String password
) implements ServerConfig {}
