package ru.rerumu.lists.model;

public record User(Long userId, String name, String password) {


    public String getHashedPassword() {
        return password;
    }

    public String getName() {
        return name;
    }
}
