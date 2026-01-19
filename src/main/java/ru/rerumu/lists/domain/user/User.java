package ru.rerumu.lists.domain.user;

public interface User {
    String getPassword();
    String getName();
    Long getId();

    String getRefreshTokenId();

    void setRefreshTokenId(String refreshTokenId);

    boolean isValidPassword(char[] password);
}
