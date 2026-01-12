package ru.rerumu.lists.crosscut.exception;

import lombok.ToString;

@ToString(callSuper = true)
public class UserPermissionException extends ClientException{

    public UserPermissionException() {
    }

    public UserPermissionException(String messageCode, Object... args) {
        super(messageCode, args);
    }
}
