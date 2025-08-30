package ru.rerumu.lists.domain.user;

public record UserDTO(
        Long userId,
        String name,
        String password
) {}
