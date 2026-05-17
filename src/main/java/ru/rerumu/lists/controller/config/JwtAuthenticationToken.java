package ru.rerumu.lists.controller.config;

import lombok.NonNull;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import ru.rerumu.lists.domain.user.User;

public final class JwtAuthenticationToken extends AbstractAuthenticationToken {

    private final User principal;

    private final String token;

    public JwtAuthenticationToken(@NonNull User principal) {
        super(null);
        this.principal = principal;
        this.token = null;
        this.setAuthenticated(true);
    }

    public JwtAuthenticationToken(@NonNull String token) {
        super(null);
        this.principal = null;
        this.token = token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }
}
