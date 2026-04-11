package com.personal.nexus.security.jwtutils;

import com.personal.nexus.security.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class AuthTokenFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Use getRequestURI() instead of getServletPath() to get the full path
        String path = request.getRequestURI();
        String method = request.getMethod();


        // Skip JWT validation for public auth endpoints
        if (path.startsWith("/auth/login") ||
                path.startsWith("/auth/signup") ||
                path.startsWith("/auth/csrf") ||
                path.startsWith("/public/")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String jwt = jwtUtils.getJwtFromHeader(request);
            if (jwt != null && jwtUtils.validateJwtToken(jwt)) {

                String username = jwtUtils.getUserNameFromJwtToken(jwt);
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );

                SecurityContextHolder.getContext().setAuthentication(authentication);
                MDC.put("username", username);
                logger.info("Authenticated user={} method={} path={}", username, method, path);
            } else {
                logger.warn("Missing or invalid JWT method={} path={}", method, path);
            }
        } catch (Exception e) {
            logger.error("JWT processing failed method={} path={} error={}", method, path, e.getMessage());
        }

        filterChain.doFilter(request, response);
    }
}