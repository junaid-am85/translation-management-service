package com.java.translation.security;

import com.java.translation.utility.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
public class SecurityConfig {

	private final JwtUtil jwtUtil;

	private static final String[] PUBLIC_URLS = { "/auth/**", "swagger-ui.html", "/v3/api-docs/**", "/h2-console/**" };

	public SecurityConfig(JwtUtil jwtUtil) {
		this.jwtUtil = jwtUtil;
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		http.csrf(AbstractHttpConfigurer::disable).sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
				.authorizeHttpRequests(auth -> auth.requestMatchers(PUBLIC_URLS).permitAll().anyRequest().authenticated())
				.addFilterBefore(new JwtAuthFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

	static class JwtAuthFilter extends OncePerRequestFilter {

		private final JwtUtil jwtUtil;

		public JwtAuthFilter(JwtUtil jwtUtil) {
			this.jwtUtil = jwtUtil;
		}

		@Override
		protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

			String authHeader = request.getHeader("Authorization");

			if (authHeader != null && authHeader.startsWith("Bearer ")) {
				String token = authHeader.substring(7);

				try {
					String username = jwtUtil.validateTokenAndGetUsername(token);
					var auth = new UsernamePasswordAuthenticationToken(username, null, Collections.emptyList());
					SecurityContextHolder.getContext().setAuthentication(auth);
				}
				catch (Exception ex) {
					response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
					return;
				}
			}

			filterChain.doFilter(request, response);
		}
	}
}
