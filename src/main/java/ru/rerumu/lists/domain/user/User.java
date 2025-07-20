package ru.rerumu.lists.domain.user;

import java.util.Objects;

public record User(Long userId, String name, String password) {


    public String getHashedPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public UserDTO toDTO() {
        return new UserDTO(userId, name, null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(userId, user.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(userId);
    }
}
