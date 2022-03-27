package ru.rerumu.lists.model;

public class User {
    private Long userId;
    private String name;
    private String password;

    public User(Long userId, String name, String password){
        this.userId = userId;
        this.name = name;
        this.password = password;
    }

    public String getHashedPassword() {
        return password;
    }

    public String getName() {
        return name;
    }
}
