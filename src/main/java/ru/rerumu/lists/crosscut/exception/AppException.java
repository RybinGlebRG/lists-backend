package ru.rerumu.lists.crosscut.exception;

import lombok.Getter;
import lombok.ToString;

@ToString(callSuper = true)
public class AppException extends RuntimeException{

    @Getter
    private String messageCode;

    @Getter
    private Object[] args;

    public AppException() {
    }

    public AppException(String message) {
        super(message);
    }

    public AppException(String message, Throwable cause) {
        super(message, cause);
    }

    public AppException(Throwable cause) {
        super(cause);
    }

    public AppException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public AppException(String messageCode, Object... args) {
        super(messageCode);
        this.messageCode = messageCode;
        this.args = args;
    }
}
