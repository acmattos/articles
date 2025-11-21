package br.com.acmattos.article.docker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class DockerApplication {
	public static void main(String[] args) {
		SpringApplication.run(DockerApplication.class, args);
	}
}
