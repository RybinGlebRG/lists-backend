package ru.rerumu.lists.controller.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.rerumu.lists.crosscut.exception.ServerException;
import ru.rerumu.lists.domain.user.User;

import java.io.IOException;

@Component
public class JWTFilter extends OncePerRequestFilter {

    private final AuthenticationManager authenticationManager;

    public JWTFilter(
            AuthenticationManager authenticationManager
    ) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest httpServletRequest,
            @NonNull HttpServletResponse httpServletResponse,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        final String header = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);
        try {
            if (header != null && header.startsWith("Bearer ")) {
                final String token = header.split(" ")[1].trim();

                Authentication authenticationRequest = new JwtAuthenticationToken(token);
                Authentication authenticationResponse = authenticationManager.authenticate(authenticationRequest);
                SecurityContext context = SecurityContextHolder.createEmptyContext();
                context.setAuthentication(authenticationResponse);
                SecurityContextHolder.setContext(context);

                Object principal = authenticationResponse.getPrincipal();
                if (principal instanceof User user) {
                    httpServletRequest.setAttribute("username", user.getName());
                    httpServletRequest.setAttribute("authUserId", user.getId());
                    httpServletRequest.setAttribute("authUser", user);
                } else {
                    throw new ServerException("Principal is not a user exception");
                }


            }
        } catch (Exception e) {
            SecurityContextHolder.clearContext();
            throw e;
        }

        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

}
