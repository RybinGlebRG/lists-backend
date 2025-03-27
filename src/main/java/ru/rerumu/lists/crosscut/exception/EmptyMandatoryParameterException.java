package ru.rerumu.lists.crosscut.exception;

public class EmptyMandatoryParameterException  extends ClientException{

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
