package com.tata.ws.exchange.infrastructure.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtService;
    private final UserDetailsService userDetailsService;
    private final HandlerExceptionResolver handlerExceptionResolver;

    @Autowired
    public JwtAuthenticationFilter(
            JwtUtil jwtService,
            UserDetailsService userDetailsService,
            @Qualifier("handlerExceptionResolver") HandlerExceptionResolver handlerExceptionResolver) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
        this.handlerExceptionResolver = handlerExceptionResolver;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String jwt = authHeader.substring(7);
        String userEmail = null;

        try {
            userEmail = jwtService.extractUsername(jwt); // Extrae el email/username del token
        } catch (ExpiredJwtException e) {
            // Manejamos el token expirado y detenemos el flujo
            handlerExceptionResolver.resolveException(request, response, null, e);
            return;
        } catch (MalformedJwtException e) {
            // Manejamos el JWT malformado y detenemos el flujo
            handlerExceptionResolver.resolveException(request, response, null, e);
            return;
        } catch (Exception e) {
            // Capturamos otras excepciones relacionadas con JWT y las manejamos adecuadamente
            handlerExceptionResolver.resolveException(request, response, null, e);
            return;
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (userEmail != null && authentication == null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

            if (jwtService.isTokenValid(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}
