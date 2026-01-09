package ru.rerumu.lists.domain.user;

public interface User {
    String getPassword();
    String getName();
    Long getId();
    UserDTO toDTO();

    boolean isValidPassword(char[] password);
}
