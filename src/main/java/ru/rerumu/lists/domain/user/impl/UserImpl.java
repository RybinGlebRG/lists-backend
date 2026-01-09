package ru.rerumu.lists.domain.user.impl;

import lombok.Getter;
import ru.rerumu.lists.domain.user.User;
import ru.rerumu.lists.domain.user.UserDTO;

import java.util.Objects;

public class UserImpl implements User {

    @Getter
    private final Long userId;

    @Getter
    private final String name;

    @Getter
    private final String password;

    public UserImpl(Long userId, String name, String password) {
        this.userId = userId;
        this.name = name;
        this.password = password;
    }

    public char[] getHashedPassword() {
        return password.toCharArray();
    }

    public String getName() {
        return name;
    }

    @Override
    public Long getId() {
        return userId;
    }

    public UserDTO toDTO() {
        return new UserDTO(userId, name, null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(userId, user.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(userId);
    }

}
