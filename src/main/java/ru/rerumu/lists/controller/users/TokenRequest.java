package ru.rerumu.lists.controller.users;

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
