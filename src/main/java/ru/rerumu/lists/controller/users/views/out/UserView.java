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
    private final String token;

    public UserView(@NonNull Long id, @NonNull String name, @NonNull String token) {
        this.id = id;
        this.name = name;
        this.token = token;
    }
}
