package ru.rerumu.lists.controller.users.views.in;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class RefreshTokenView {

    private final String refreshToken;

    @JsonCreator
    public RefreshTokenView(@JsonProperty("refreshToken") String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
