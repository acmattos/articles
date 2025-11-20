package br.com.acmattos.article.docker.camel.config;

public interface ServerConfig {
    char SLASH = '/';
    String AUTHENTICATION_BASIC =
        "authenticationPreemptive=true&authMethodPriority=Basic&authMethod=Basic";
    String AUTH_USERNAME = "&authUsername=";
    String AUTH_PASSWORD = "&authPassword=";
    String SSL_INSECURE_PARAMETERS =
        "&sslContextParameters=#insecureSSLContextParameters";

    String host();
    String resource();
    Integer size();
    boolean sslInsecure();
    boolean secure();
    String username();
    String password();

    default String toUrl(String uri) {
        StringBuilder builder = new StringBuilder();
        builder
            .append(host())
            .append(SLASH)
            .append(resource())
            .append(SLASH)
            .append(uri);
        if(secure()) {
            builder
                .append(AUTHENTICATION_BASIC)
                .append(AUTH_USERNAME).append(username())
                .append(AUTH_PASSWORD).append(password())
                .append("&bridgeEndpoint=true");
        }
        if(secure() && sslInsecure()) {
            builder.append(SSL_INSECURE_PARAMETERS);
        }
        return builder.toString();
    }
}
