package com.lucasrodrigues.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.lucasrodrigues.core.property.JwtConfiguration;

@SpringBootApplication
@EnableConfigurationProperties(value = JwtConfiguration.class)
//para que o spring ache as dependencias e as enxergue como entidades
@EntityScan({"com.lucasrodrigues.core.model"})
@EnableJpaRepositories({"com.lucasrodrigues.core.repository"})
@EnableEurekaClient
@ComponentScan("com.lucasrodrigues")
public class AuthApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthApplication.class, args);
	}

}
