package com.lucasrodrigues.token.security.token.creator;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPublicKey;
import java.util.Date;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import com.lucasrodrigues.core.model.ApplicationUser;
import com.lucasrodrigues.core.property.JwtConfiguration;
import com.nimbusds.jose.EncryptionMethod;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JOSEObjectType;
import com.nimbusds.jose.JWEAlgorithm;
import com.nimbusds.jose.JWEHeader;
import com.nimbusds.jose.JWEObject;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.DirectEncrypter;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TokenCreator {
	
	private final JwtConfiguration jwtConfiguration;
	
	/**
	 * Metodo responsavel por criar um token assinado
	 */
	@SneakyThrows
	public SignedJWT createSignedJWT(Authentication auth) {
		log.info("Começando a criar a JWT assinada");
		ApplicationUser applicationUser = (ApplicationUser) auth.getPrincipal();

		// pega as claims
		JWTClaimsSet jwtClaimsSet = createJWTClaimSet(auth, applicationUser);

		// gera a chave
		KeyPair rsaKeys = generateKeyPair();

		log.info("Construindo JWK para a chave RSA");

		JWK jwk = new RSAKey.Builder((RSAPublicKey) rsaKeys.getPublic()).keyID(UUID.randomUUID().toString()).build();

		// criando o token
		SignedJWT signedJWT = new SignedJWT(new JWSHeader.Builder(JWSAlgorithm.RS256).jwk(jwk)// essa chave publica vai
																								// ser usada para
																								// validar toda vez que
																								// o microserviço
																								// adiquirir esse token
				.type(JOSEObjectType.JWT).build(), jwtClaimsSet);

		log.info("Assinando o token com a  RSA chave privada");

		RSASSASigner signer = new RSASSASigner(rsaKeys.getPrivate());

		signedJWT.sign(signer);

		log.info("Serialized token '{}'", signedJWT.serialize());
		return signedJWT;
	}

	private JWTClaimsSet createJWTClaimSet(Authentication auth, ApplicationUser applicationUser) {

		log.info("Criando o objeto JWTClaimSet para '{}", applicationUser);
		return new JWTClaimsSet.Builder().subject(applicationUser.getUsername())
				.claim("authorities",
						auth.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
				.issuer("http://com.lucasrodrigues").issueTime(new Date())
				.expirationTime(new Date(System.currentTimeMillis() + (jwtConfiguration.getExpiration() * 1000)))
				.build();
	}

	@SneakyThrows
	private KeyPair generateKeyPair() {
		log.info("gerando RSA 2048 bits Keys");

		KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");

		generator.initialize(2048);

		return generator.generateKeyPair();
	}

	/**
	 * Método responsavel por fazer a criptografia
	 * 
	 * @throws JOSEException
	 * 
	 */

	public String encryptToken(SignedJWT signedJWT) throws JOSEException {
		log.info("Começando o método encryptToken");
		DirectEncrypter directEncrypter = new DirectEncrypter(jwtConfiguration.getPrivateKey().getBytes());

		JWEObject jweObject = new JWEObject(
				new JWEHeader.Builder(JWEAlgorithm.DIR, EncryptionMethod.A128CBC_HS256).contentType("JWT").build(),
				new Payload(signedJWT));
		log.info("Criptografando o token com sistemas chave privada");

		jweObject.encrypt(directEncrypter);

		log.info("Token criptografaddo");

		return jweObject.serialize();

	}
}
