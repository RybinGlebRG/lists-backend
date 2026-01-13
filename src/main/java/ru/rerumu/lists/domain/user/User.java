package ru.rerumu.lists.domain.user;

public interface User {
    String getPassword();
    String getName();
    Long getId();

    boolean isValidPassword(char[] password);
}
