package ru.rerumu.lists.domain.user;

public interface UserFactory {

    User build(
            Long userId,
            String name,
            char[] password
    );
}
