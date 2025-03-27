package ru.rerumu.lists.crosscut.exception;

public class NoJWTException extends Exception{

    private String message = "No JWT in query";

    public NoJWTException(){};

    public NoJWTException(String message){
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
