package com.lucasrodrigues.core.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Entity
@Table(name = "tb_user")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)

public class ApplicationUser implements AbstractEntity{

	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Include
	private Long us_id;
	
	@NotNull(message = "The field 'username' is mandatory")
	@Column(nullable = false)
	private String us_username;
	
	@NotNull(message = "The field 'password' is mandatory")
	@Column(nullable = false)
	private String us_password;
	
	@NotNull(message = "The field 'role' is mandatory")
	@Column(nullable = false)
	private String us_role = "USER";
	
	public ApplicationUser(@NotNull ApplicationUser applicationUser) {
		this.us_id = applicationUser.getUs_id();
		this.us_username = applicationUser.getUs_username();
		this.us_password = applicationUser.getUs_password();
		this.us_role = 	applicationUser.getUs_role();
		}
	@Override
	public Long getIde() {
		// TODO Auto-generated method stub
		return null;
	}

}
