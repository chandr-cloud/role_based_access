package com.nt.security;

import java.io.IOException;
import java.util.logging.Logger;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.nt.auth.UserRole;
import com.nt.repository.UserRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

	private static final Logger log = Logger.getLogger(JwtAuthFilter.class.getName());

	private final UserRepository userRepository;
	private final AuthUtil authUtil;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		try {
			log.info("incoming request: " + request.getRequestURI());

			final String requestTokenHeader = request.getHeader("Authorization");
			if (requestTokenHeader == null || !requestTokenHeader.startsWith("Bearer")) {
				filterChain.doFilter(request, response);
				return;
			}

			String token = requestTokenHeader.split("Bearer ")[1].trim();
			String username = authUtil.getUsernameFromToken(token);

			if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
				UserRole user = userRepository.findByEmail(username).orElse(null);
				if (user != null) {
					UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
							user, null, user.getAuthorities());
					SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
				}
			}
			filterChain.doFilter(request, response);
		} catch (Exception ex) {
			log.severe("JWT Filter Error: " + ex.getMessage());
			filterChain.doFilter(request, response);
		}
	}
}
