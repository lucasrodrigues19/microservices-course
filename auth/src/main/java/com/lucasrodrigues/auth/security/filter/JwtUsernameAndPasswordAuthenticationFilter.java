package com.lucasrodrigues.auth.security.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class JwtUsernameAndPasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter{

}
