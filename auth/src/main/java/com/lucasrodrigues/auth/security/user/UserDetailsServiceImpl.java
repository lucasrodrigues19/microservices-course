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

	private ApplicationUserRepository applicationUserRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		log.info("Procurando no BD o user por username'{}'", username);
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
			return AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_" + getUs_role());
		}

		@Override
		public String getPassword() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String getUsername() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public boolean isAccountNonExpired() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean isAccountNonLocked() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean isCredentialsNonExpired() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean isEnabled() {
			// TODO Auto-generated method stub
			return false;
		}

	}
}
