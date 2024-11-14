package com.cluting.clutingbackend.util.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;

    public JwtAuthenticationFilter(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        String token = jwtProvider.resolveToken(request);
        if (StringUtils.hasText(token)) {
            try {
                if (jwtProvider.validateToken(token)) {
                    Authentication auth = jwtProvider.getAuthentication(token);
                    if (auth != null) {
                        log.debug("Authentication created successfully: {}", auth.getPrincipal());
                        SecurityContextHolder.getContext().setAuthentication(auth);
                    } else {
                        log.warn("Authentication is null. Failed to create authentication.");
                    }
                }
            } catch (Exception e) {
                SecurityContextHolder.clearContext();
                request.setAttribute("exception", e);
                log.error("Authentication error: {}", e.getMessage());
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
                return;
            }
        }
        chain.doFilter(request, response);
    }
}
