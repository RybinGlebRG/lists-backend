package ru.rerumu.lists.model.user;

public record UserDTO(
        Long userId,
        String name,
        String password
) {}
