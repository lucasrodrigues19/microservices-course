package com.lucasrodrigues.course;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.lucasrodrigues.core.property.JwtConfiguration;

@SpringBootApplication
//para que o spring ache as dependencias e as enxergue como entidades
@EntityScan({"com.lucasrodrigues.core.model"})
@EnableJpaRepositories({"com.lucasrodrigues.core.repository"})
@EnableConfigurationProperties(value = JwtConfiguration.class)
@ComponentScan("com.lucasrodrigues")
public class CourseApplication {

	public static void main(String[] args) {
		SpringApplication.run(CourseApplication.class, args);
	}

}
