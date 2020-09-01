package com.lucasrodrigues.auth.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.lucasrodrigues.auth.security.filter.JwtUsernameAndPasswordAuthenticationFilter;
import com.lucasrodrigues.core.property.JwtConfiguration;
import com.lucasrodrigues.token.security.config.SecurityTokenConfig;
import com.lucasrodrigues.token.security.filter.JwtTokenAuthorizationFilter;
import com.lucasrodrigues.token.security.token.converter.TokenConverter;
import com.lucasrodrigues.token.security.token.creator.TokenCreator;


/**
 * Essa classe vai dizer o que vai ser bloqueado, e como vai funcionar esse microservico
 * @author lucas.rodrigues
 *
 */
@EnableWebSecurity
public class SecurityCredentialsConfig extends SecurityTokenConfig {
	
	
	private final UserDetailsService userDetailsService;
	private final TokenCreator tokenCreator;
	private final TokenConverter tokenConverter;
	
	@Autowired
	public SecurityCredentialsConfig(JwtConfiguration jwtConfiguration,
			@Qualifier("userDetailsServiceImpl") UserDetailsService userDetailsService,
			TokenCreator tokenCreator,TokenConverter tokenConverter) {
		super(jwtConfiguration);
		this.userDetailsService = userDetailsService;
		this.tokenCreator = tokenCreator;
		this.tokenConverter = tokenConverter;
	}
	
	/**
	 * Configuração do Http
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
					.addFilter(new JwtUsernameAndPasswordAuthenticationFilter(authenticationManager(),jwtConfiguration,tokenCreator))
					.addFilterAfter(new JwtTokenAuthorizationFilter(jwtConfiguration, tokenConverter),UsernamePasswordAuthenticationFilter.class);
		super.configure(http);
		//
	}

	/**
	 * Vai fazer a autenticação chamando o metodo findByUsername
	 * So vai retornar o valor, quem vai checar se a autenticação foi feita com sucesso vai ser o spring
	 */
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		//vai fazer o autowired com o valor que tem dentro do pacote 
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	
}
