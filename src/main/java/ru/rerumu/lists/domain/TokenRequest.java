package ru.rerumu.lists.domain;

public class TokenRequest {
    private final String password;
    private final String username;

    public TokenRequest(String password, String username){
        this.password = password;
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
