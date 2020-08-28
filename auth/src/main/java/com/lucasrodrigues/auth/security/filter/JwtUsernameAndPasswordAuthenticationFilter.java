package com.lucasrodrigues.auth.security.filter;

import java.util.Collections;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lucasrodrigues.core.model.ApplicationUser;
import com.lucasrodrigues.core.property.JwtConfiguration;
import com.lucasrodrigues.token.security.token.creator.TokenCreator;
import com.nimbusds.jwt.SignedJWT;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;


/**
 * Esse filtro vai ser executado toda vez que for feita uma requisição
 * 
 * @author lucas.rodrigues
 *
 */
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class JwtUsernameAndPasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private final AuthenticationManager authenticationManager;
	private final JwtConfiguration jwtConfiguration;
	private final TokenCreator tokenCreator;
	/**
	 * vai tentar fazer a autenticação do usuario, deelgando a responsabilidade para
	 * o UserDetailsService
	 */
	@Override
	@SneakyThrows // vai encapsular qualquer exceção para uma runtime exception
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {

		log.info("Tentando fazer a autenticação. . .");
		// pegando o usuario que está vindo na requisição
		ApplicationUser applicationUser = new ObjectMapper().readValue(request.getInputStream(), ApplicationUser.class);

		if(applicationUser==null)
			throw new UsernameNotFoundException("Incapaz de recuperar o usernmae ou a senha");	
		
		log.info("Criando o objeto de autenticação para o usuario '{}' e chamando UserDetailsService loadByUsername",applicationUser.getUsername());
		
		UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = 	new UsernamePasswordAuthenticationToken(applicationUser.getUsername(), applicationUser.getPassword(),Collections.emptyList());
		
		//se tiver um valor autenticado, adiciona do details
		
		usernamePasswordAuthenticationToken.setDetails(applicationUser);
		
		
		return authenticationManager.authenticate(usernamePasswordAuthenticationToken);
	}

	

	/**
	 * Metodo vai tratar caso a autenticação seja bem sucedida
	 */
	@Override
	@SneakyThrows
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication auth){
		log.info("indo para o metodo successfulAuthentication. . .");
		log.info("Autenticação foi bem sucedida pelo usuario '{}', gerando JWE token", auth.getName());
		
		
		
		SignedJWT signedJWT = tokenCreator.createSignedJWT(auth);;
		String encryptedToken = tokenCreator.encryptToken(signedJWT);
		
		log.info("Token gerado com sucesso, adicionando para o cabecalho response");
		
		//habilitando o java script para pegar os valores
		
		response.addHeader("Access-Control-Expose-Headers","XSRF-TOKEN, "+jwtConfiguration.getHeader().getName());
		
		response.addHeader(jwtConfiguration.getHeader().getName(), jwtConfiguration.getHeader().getPrefix()+encryptedToken);
	}
	
	
	
	
	
}
