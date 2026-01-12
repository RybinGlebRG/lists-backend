package ru.rerumu.lists.controller.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.rerumu.lists.domain.user.User;
import ru.rerumu.lists.services.user.UserService;

import java.io.IOException;
import java.util.ArrayList;

@Component
public class JWTFilter extends OncePerRequestFilter {

    private final UserService userService;

    public JWTFilter(@Qualifier("UserService") UserService userService) {
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse,
            FilterChain filterChain
    ) throws ServletException, IOException {

        final String header = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);
        if (header != null && header.startsWith("Bearer ")) {
            final String token = header.split(" ")[1].trim();
            User user = userService.findByToken(token);

            httpServletRequest.setAttribute("username",user.getName());
            httpServletRequest.setAttribute("authUserId",user.getId());
            httpServletRequest.setAttribute("authUser", user);

            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(
                    user.getName(),
                    null,
                    new ArrayList<>()
            ));
        }

        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

}
