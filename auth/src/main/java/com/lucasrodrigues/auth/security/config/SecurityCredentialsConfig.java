package com.lucasrodrigues.auth.security.config;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import com.lucasrodrigues.auth.security.filter.JwtUsernameAndPasswordAuthenticationFilter;
import com.lucasrodrigues.core.property.JwtConfiguration;

import lombok.RequiredArgsConstructor;

@EnableWebSecurity
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
/**
 * Essa classe vai dizer o que vai ser bloqueado, e como vai funcionar esse microservico
 * @author lucas.rodrigues
 *
 */
public class SecurityCredentialsConfig extends WebSecurityConfigurerAdapter {
	
	private final UserDetailsService userDetailsService;
	private final JwtConfiguration jwtConfiguration;
	
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
					.addFilter(new JwtUsernameAndPasswordAuthenticationFilter(authenticationManager(),jwtConfiguration))
				.authorizeRequests()
					.antMatchers(jwtConfiguration.getLoginUrl()).permitAll() //permite q a url do login seja acessada
					.antMatchers("/course/v1/admin/**").hasRole("ADMIN")
					.anyRequest().authenticated()	//qualquer outra requisição precisa esta autenticada
				.and()
					.formLogin().disable();
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
