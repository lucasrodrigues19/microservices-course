package com.lucasrodrigues.course.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.lucasrodrigues.core.property.JwtConfiguration;
import com.lucasrodrigues.token.security.config.SecurityTokenConfig;
import com.lucasrodrigues.token.security.filter.JwtTokenAuthorizationFilter;
import com.lucasrodrigues.token.security.token.converter.TokenConverter;

/**
 * Essa classe vai dizer o que vai ser bloqueado, e como vai funcionar esse
 * microservico
 * 
 * @author lucas.rodrigues
 *
 */
@EnableWebSecurity
public class SecurityCredentialsConfig extends SecurityTokenConfig {

	private final TokenConverter tokenConverter;

	@Autowired
	public SecurityCredentialsConfig(JwtConfiguration jwtConfiguration, TokenConverter tokenConverter) {
		super(jwtConfiguration);
		this.tokenConverter = tokenConverter;
	}

	/**
	 * Configuração do Http
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.addFilterAfter(new JwtTokenAuthorizationFilter(jwtConfiguration, tokenConverter),
						UsernamePasswordAuthenticationFilter.class);
		super.configure(http);
		//
	}

}
