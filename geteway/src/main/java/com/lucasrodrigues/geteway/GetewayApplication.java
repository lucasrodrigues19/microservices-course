package com.lucasrodrigues.geteway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableZuulProxy
@EnableEurekaClient
@ComponentScan("com.lucasrodrigues")
@EntityScan({"com.lucasrodrigues.core.model"})
public class GetewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(GetewayApplication.class, args);
	}

}
