package ru.rerumu.lists.controller.users.views.out;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

@EqualsAndHashCode
@ToString
@Getter
public class UserView {

    private final Long id;
    private final String name;
    private final String accessToken;
    private final String refreshToken;

    public UserView(
            @NonNull Long id,
            @NonNull String name,
            @NonNull String accessToken,
            @NonNull String refreshToken
    ) {
        this.id = id;
        this.name = name;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
