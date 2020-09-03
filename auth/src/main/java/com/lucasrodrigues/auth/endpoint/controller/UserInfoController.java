package com.lucasrodrigues.auth.endpoint.controller;

import java.security.Principal;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lucasrodrigues.core.model.ApplicationUser;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author lucas.rodrigues
 */

@RestController
@RequestMapping("user")
@Api(value = "Endpoints para informações dos usuarios")
public class UserInfoController {

	@GetMapping(path = "/info", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ApiOperation(value = "Vai recuperar as informações de usuario acessivel com o token", response = ApplicationUser.class)
	public ResponseEntity<ApplicationUser> getUserInfo(Principal principal){
		ApplicationUser applicationUser = (ApplicationUser) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
		return new ResponseEntity<>(applicationUser, HttpStatus.OK);
	}
	
}
