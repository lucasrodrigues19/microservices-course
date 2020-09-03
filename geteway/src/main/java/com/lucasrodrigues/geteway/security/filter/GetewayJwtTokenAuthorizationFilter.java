package com.lucasrodrigues.geteway.security.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;

import com.lucasrodrigues.core.property.JwtConfiguration;
import com.lucasrodrigues.token.security.filter.JwtTokenAuthorizationFilter;
import com.lucasrodrigues.token.security.token.converter.TokenConverter;
import com.lucasrodrigues.token.security.util.SecurityContextUtil;
import com.netflix.zuul.context.RequestContext;
import com.nimbusds.jwt.SignedJWT;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GetewayJwtTokenAuthorizationFilter extends JwtTokenAuthorizationFilter {

	public GetewayJwtTokenAuthorizationFilter(JwtConfiguration jwtConfiguration, TokenConverter tokenConverter) {
		super(jwtConfiguration, tokenConverter);
	}

	@Override
	@SneakyThrows
	@SuppressWarnings("Dupicates")
	protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
			@NonNull FilterChain chain) throws ServletException, IOException {

		log.info("Começando o metodo doFilterInternal do geteway");
		String header = request.getHeader(jwtConfiguration.getHeader().getName());

		// Esse filtro pode estar indo para requisições que não precisam ser
		// autenticadas,
		// Ou seja, não faz sentido ter um header
		if (header == null || !header.startsWith(jwtConfiguration.getHeader().getPrefix())) {
			chain.doFilter(request, response);
			return;
		}

		String token = header.replace(jwtConfiguration.getHeader().getPrefix(), "").trim();

		
		String signedToken = tokenConverter.decryptToken(token);
		tokenConverter.validateTokenSignature(signedToken);
		SecurityContextUtil.setSecurityContext(SignedJWT.parse(signedToken));

		// Se ja tiver uma propriedade do tipo assinada, vou dar um replace no
		// Authorization Header, do token criptografado para o assinado
		if (jwtConfiguration.getType().equalsIgnoreCase("signed"))
			RequestContext.getCurrentContext().addZuulRequestHeader("Authorization",
					jwtConfiguration.getHeader().getPrefix() + signedToken);

		chain.doFilter(request, response);
	}
}
