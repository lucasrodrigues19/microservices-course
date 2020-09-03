package com.lucasrodrigues.auth.docs;

import org.springframework.context.annotation.Configuration;

import com.lucasrodrigues.core.docs.BaseSwaggerConfig;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author lucas.rodrigues
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig extends BaseSwaggerConfig{

	public SwaggerConfig() {
		super("com.lucasrodrigues.auth.endpoint.controller");
	}

	
}
