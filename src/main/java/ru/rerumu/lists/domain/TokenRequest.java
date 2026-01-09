package ru.rerumu.lists.domain;

import lombok.Getter;

@Getter
public class TokenRequest {
    private final String password;
    private final String username;

    public TokenRequest(String password, String username){
        this.password = password;
        this.username = username;
    }

}
