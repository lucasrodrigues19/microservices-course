package com.lucasrodrigues.auth.security.user;

import java.util.Collection;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.lucasrodrigues.core.model.ApplicationUser;
import com.lucasrodrigues.core.repository.ApplicationUserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private ApplicationUserRepository applicationUserRepository;

	@Override
	public UserDetails loadUserByUsername(String username) {
		log.info("Procurando no BD o user por username'{}'", username);
		
		if(applicationUserRepository == null)
			throw new IllegalArgumentException("applicationUserRepository esta null");
		
		ApplicationUser applicationUser = applicationUserRepository.findByUsername(username);
		log.info("ApplicationUser found'{}'", applicationUser);

		if (applicationUser == null)
			throw new UsernameNotFoundException(String.format("Application user '%s' not found", username));

		return new CustomUserDetails(applicationUser);
	}

	private static final class CustomUserDetails extends ApplicationUser implements UserDetails {

		private static final long serialVersionUID = 1L;

		CustomUserDetails(@NotNull ApplicationUser applicationUser) {
			super(applicationUser);

		}

		@Override
		public Collection<? extends GrantedAuthority> getAuthorities() {
			// TODO Auto-generated method stub
			return AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_" + getRole());
		}


		//todos esses metodos abaixo precisam retornar 'true', se não a autenticação não funciona
		@Override
		public boolean isAccountNonExpired() {
			// TODO Auto-generated method stub
			return true;
		}

		@Override
		public boolean isAccountNonLocked() {
			// TODO Auto-generated method stub
			return true;
		}

		@Override
		public boolean isCredentialsNonExpired() {
			// TODO Auto-generated method stub
			return true;
		}

		@Override
		public boolean isEnabled() {
			// TODO Auto-generated method stub
			return true;
		}

	}
}
