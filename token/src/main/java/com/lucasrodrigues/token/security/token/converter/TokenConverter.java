package com.lucasrodrigues.token.security.token.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import com.lucasrodrigues.core.property.JwtConfiguration;
import com.nimbusds.jose.JWEObject;
import com.nimbusds.jose.crypto.DirectDecrypter;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jwt.SignedJWT;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
/**
 * @author lucas.rodrigues
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TokenConverter {

	private final JwtConfiguration jwtConfiguration ;
	
	@SneakyThrows
	public String decryptToken(String encryptedToken) {
		
		log.info("Descriptografar token");
		
		JWEObject jweObject = JWEObject.parse(encryptedToken);
		DirectDecrypter directDecrypter = new DirectDecrypter(jwtConfiguration.getPrivateKey().getBytes());
		jweObject.decrypt(directDecrypter);
		
		log.info("Token descriptografado, retornando token assinado");
		
		return jweObject.getPayload().toSignedJWT().serialize();
	}
	
	@SneakyThrows
	public void validateTokenSignature(String signedToken) {
		log.info("Começando metodo para validar assinatura token. . .");
		
		SignedJWT signedJWT = SignedJWT.parse(signedToken);
		
		log.info("Token analisado! Recuperando a chave publica para token assinado");
		
		RSAKey publicKey = RSAKey.parse(signedJWT.getHeader().getJWK().toJSONObject());
		
		log.info("Chave publica recuperada, validando assinatura");
		
		if(!signedJWT.verify(new RSASSAVerifier(publicKey)))
			throw new AccessDeniedException("Assinatura de token invalida");
	}
}
