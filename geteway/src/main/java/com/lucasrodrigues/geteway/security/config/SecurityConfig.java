package com.lucasrodrigues.geteway.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.lucasrodrigues.core.property.JwtConfiguration;
import com.lucasrodrigues.geteway.security.filter.GetewayJwtTokenAuthorizationFilter;
import com.lucasrodrigues.token.security.config.SecurityTokenConfig;
import com.lucasrodrigues.token.security.token.converter.TokenConverter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author lucas.rodrigues
 *
 */
@Slf4j
@EnableWebSecurity
public class SecurityConfig extends SecurityTokenConfig {

	private final TokenConverter tokenConverter;
	
	public SecurityConfig(JwtConfiguration jwtConfiguration,TokenConverter tokenConverter) {
		super(jwtConfiguration);
		this.tokenConverter = tokenConverter;
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.addFilterAfter(new GetewayJwtTokenAuthorizationFilter(jwtConfiguration, tokenConverter), UsernamePasswordAuthenticationFilter.class);
		super.configure(http);
	}
	
	
	


}
