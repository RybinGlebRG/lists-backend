package ru.rerumu.lists.crosscut.exception;

public class IncorrectPasswordException extends Exception{

    private String message = "Incorrect password";

    public IncorrectPasswordException(){};

    public IncorrectPasswordException(String message){
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
