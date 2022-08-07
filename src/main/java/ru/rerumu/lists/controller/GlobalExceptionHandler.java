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

import java.io.PrintWriter;
import java.io.StringWriter;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<String> handle(Exception e, WebRequest request){
        logger.error(e.getMessage(),e);

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);

        JSONObject obj = new JSONObject();
        obj.put("error",sw.toString().strip());
        obj.put("errorMessage", e.getMessage() != null ? e.getMessage() : JSONObject.NULL);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<String> resEnt = new ResponseEntity<>(
                obj.toString(),
                httpHeaders,
                HttpStatus.INTERNAL_SERVER_ERROR);
        return resEnt;
    }
}
