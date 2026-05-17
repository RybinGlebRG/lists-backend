package ru.rerumu.lists.controller.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import ru.rerumu.lists.domain.user.User;
import ru.rerumu.lists.services.user.UserService;

@Component
public class JwtAuthenticationProvider implements AuthenticationProvider {

    private final UserService userService;

    public JwtAuthenticationProvider(
            @Qualifier("UserService") UserService userService
    ) {
        this.userService = userService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Object credentials = authentication.getCredentials();
        if (credentials instanceof String token) {
            User user = userService.findByToken(token);
            return new JwtAuthenticationToken(user);
        }
        throw new IncorrectTokenException();
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(JwtAuthenticationToken.class);
    }
}
