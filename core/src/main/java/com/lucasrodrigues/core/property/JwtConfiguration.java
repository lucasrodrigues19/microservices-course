package com.lucasrodrigues.core.property;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Configuration
@ConfigurationProperties(prefix = "jwt.config") // tudo que escrever aqui vai comecar com "jwt.config"
@Getter
@Setter
@ToString
/**
 * Propriedades do Token, que serão compartilhadas por todo o projeto, todas as
 * propriedades vão para o application.yml
 * 
 * @author lucas.rodrigues
 *
 */
public class JwtConfiguration {

	private String loginUrl = "/login/**";
	@NestedConfigurationProperty
	private Header header = new Header();
	private int expiration = 3600;
	private String privateKey = "EkMh8YAwKRyXdJVgo4Pjdy3iYbIdC9Z0";
	private String type = "encrypted";

	@Getter
	@Setter
	public static class Header {
		private String name = "Authorization";
		private String prefix = "Bearer ";
	}
}
