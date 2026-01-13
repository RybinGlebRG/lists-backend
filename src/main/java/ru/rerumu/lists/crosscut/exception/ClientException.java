package ru.rerumu.lists.crosscut.exception;

import lombok.ToString;

@ToString(callSuper = true)
public class ClientException extends AppException{

    public ClientException() {
    }

    public ClientException(String messageCode, Object... args) {
        super(messageCode, args);
    }
}
