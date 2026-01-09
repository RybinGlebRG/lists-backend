package ru.rerumu.lists.domain.user;

public interface User {
    char[] getHashedPassword();
    String getPassword();
    String getName();
    Long getId();
    UserDTO toDTO();
}
