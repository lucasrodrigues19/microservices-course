package com.lucasrodrigues.core.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.lucasrodrigues.core.model.ApplicationUser;

public interface ApplicationUserRepository extends PagingAndSortingRepository<ApplicationUser, Long> {

	ApplicationUser findByUsername(String username);
}
