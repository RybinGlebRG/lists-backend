package ru.rerumu.lists.controller;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import ru.rerumu.lists.exception.EntityNotFoundException;
import ru.rerumu.lists.exception.UserIsNotOwnerException;

import java.io.PrintWriter;
import java.io.StringWriter;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private String prepareAnswer(Exception e){
        logger.error(e.getMessage(),e);

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);

        JSONObject obj = new JSONObject();
        obj.put("error",sw.toString().strip());
        obj.put("errorMessage", e.getMessage() != null ? e.getMessage() : JSONObject.NULL);

        return obj.toString();
    }

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<String> handle(Exception e, WebRequest request){

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        String answer = prepareAnswer(e);

        logger.error(answer);

        ResponseEntity<String> resEnt = new ResponseEntity<>(
                answer,
                httpHeaders,
                HttpStatus.INTERNAL_SERVER_ERROR);
        return resEnt;
    }



    @ExceptionHandler(value = {UserIsNotOwnerException.class})
    public ResponseEntity<String> handleForbidden(Exception e, WebRequest request){

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<String> resEnt = new ResponseEntity<>(
                prepareAnswer(e),
                httpHeaders,
                HttpStatus.FORBIDDEN);
        return resEnt;
    }

    @ExceptionHandler(value = {EntityNotFoundException.class})
    public ResponseEntity<String> handleNotFound(Exception e, WebRequest request){

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<String> resEnt = new ResponseEntity<>(
                prepareAnswer(e),
                httpHeaders,
                HttpStatus.NOT_FOUND);
        return resEnt;
    }


}
