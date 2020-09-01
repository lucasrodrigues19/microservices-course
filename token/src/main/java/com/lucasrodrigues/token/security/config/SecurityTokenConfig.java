package com.lucasrodrigues.token.security.config;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.web.cors.CorsConfiguration;

import com.lucasrodrigues.core.property.JwtConfiguration;

import lombok.RequiredArgsConstructor;


/**
 * Essa classe vai dizer o que vai ser bloqueado, e como vai funcionar esse microservico
 * @author lucas.rodrigues
 *
 */
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SecurityTokenConfig extends WebSecurityConfigurerAdapter {
	
	protected final JwtConfiguration jwtConfiguration;
	
	/**
	 * Configuração do Http
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
				.csrf().disable()
				.cors().configurationSource(request -> new CorsConfiguration().applyPermitDefaultValues())
				.and()
					.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)//nao vou manter nem um tipo de sessao
				.and()
					.exceptionHandling().authenticationEntryPoint((req,resp,e)->resp.sendError(HttpServletResponse.SC_UNAUTHORIZED)) //tratar as execoes relacionadas ao authentication entrypoint	
				.and()
				.authorizeRequests()
					.antMatchers(jwtConfiguration.getLoginUrl()).permitAll() //permite q a url do login seja acessada
					.antMatchers("/course/v1/admin/**").hasRole("ADMIN")
					.antMatchers("/auth/user/**").hasAnyRole("ADMIN","USER")
					.anyRequest().authenticated();	//qualquer outra requisição precisa esta autenticada
				
		//
	}

}
