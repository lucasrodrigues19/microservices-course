package com.lucasrodrigues.token.security.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.web.filter.OncePerRequestFilter;

import com.lucasrodrigues.core.property.JwtConfiguration;
import com.lucasrodrigues.token.security.token.converter.TokenConverter;
import com.lucasrodrigues.token.security.util.SecurityContextUtil;
import com.nimbusds.jwt.SignedJWT;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * Filtro ultilizado para fazer a validação da assinatura token, para todas as
 * requisições
 * 
 * @author lucas.rodrigues
 *
 */
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class JwtTokenAuthorizationFilter extends OncePerRequestFilter {// extends,garante que sera executado apenas uma
																		// vez por requisição

	protected final JwtConfiguration jwtConfiguration;
	protected final TokenConverter tokenConverter;

	@Override
	protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain chain)
			throws ServletException, IOException {
		log.info("Começando o metodo doFilterInternal token");
		String header = request.getHeader(jwtConfiguration.getHeader().getName());

		// Esse filtro pode estar indo para requisições que não precisam ser
		// autenticadas,
		// Ou seja, não faz sentido ter um header
		if (header == null || !header.startsWith(jwtConfiguration.getHeader().getPrefix())) {
			chain.doFilter(request, response);
			return;
		}

		String token = header.replace(jwtConfiguration.getHeader().getPrefix(), "").trim();
		SecurityContextUtil
				.setSecurityContext(StringUtils.equalsIgnoreCase("signed", jwtConfiguration.getType()) ? validate(token)
						: decryptValidating(token));
		chain.doFilter(request, response);
	}

	/**
	 * Metodo responsavel por enviar o token criptografado para todos os
	 * microservices
	 * 
	 * @param encryptedToken
	 * @return
	 */
	@SneakyThrows
	private SignedJWT decryptValidating(String encryptedToken) {
		log.info("Começando o metodo decryptValidating"
				+ ": JwtTokenAuthorizationFilter");
		String signedToken = tokenConverter.decryptToken(encryptedToken);
		tokenConverter.validateTokenSignature(signedToken);

		return SignedJWT.parse(signedToken);

	}

	/**
	 * Metodo responsavel por enviar o token para os down trim service, tudo aquilo
	 * que estiver abaixo do geteway assinado
	 * 
	 * @param signedToken
	 * @return
	 */
	@SneakyThrows
	private SignedJWT validate(String signedToken) {
		log.info("Começando o validate");
		tokenConverter.validateTokenSignature(signedToken);
		return SignedJWT.parse(signedToken);
	}
}
