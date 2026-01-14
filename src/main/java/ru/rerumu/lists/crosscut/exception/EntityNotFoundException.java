package ru.rerumu.lists.crosscut.exception;

public class EntityNotFoundException extends ClientException{

    public EntityNotFoundException() {
    }

    public EntityNotFoundException(String messageCode, Object... args) {
        super(messageCode, args);
    }
}
