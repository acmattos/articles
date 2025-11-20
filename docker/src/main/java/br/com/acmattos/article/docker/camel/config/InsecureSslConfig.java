package br.com.acmattos.article.docker.camel.config;

import org.apache.camel.support.jsse.SSLContextParameters;
import org.apache.camel.support.jsse.TrustManagersParameters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.net.ssl.X509TrustManager;
import java.security.cert.X509Certificate;

@Configuration
public class InsecureSslConfig {
    @Bean("insecureSSLContextParameters")
    public SSLContextParameters insecureSSLContextParameters() {
        var params = new SSLContextParameters();
        var trust = new TrustManagersParameters();
        trust.setTrustManager(new X509TrustManager() {
            public void checkClientTrusted(X509Certificate[] certs, String authType) {}
            public void checkServerTrusted(X509Certificate[] certs, String authType) {}
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return new java.security.cert.X509Certificate[0];
            }
        });
        params.setTrustManagers(trust);
        return params;
    }
}