package ru.rerumu.lists.exception;

public class EmptyMandatoryParameterException  extends Exception{

    private String message = "Mandatory parameter is null or empty";

    public EmptyMandatoryParameterException(){};

    public EmptyMandatoryParameterException(String message){
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
