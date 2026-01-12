package ru.rerumu.lists.controller;

import com.jcabi.aspects.Loggable;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import ru.rerumu.lists.crosscut.exception.EntityNotFoundException;
import ru.rerumu.lists.crosscut.exception.UserIsNotOwnerException;
import ru.rerumu.lists.crosscut.exception.UserPermissionException;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Locale;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    private final MessageSource messageSource;

    @Autowired
    public GlobalExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    private String prepareAnswer(Exception e){
        log.error(e.getMessage(),e);

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

        log.error(answer);

        ResponseEntity<String> resEnt = new ResponseEntity<>(
                answer,
                httpHeaders,
                HttpStatus.INTERNAL_SERVER_ERROR);
        return resEnt;
    }

    @ExceptionHandler(value = {UserPermissionException.class})
    @Loggable(value = Loggable.TRACE, prepend = true, trim = false)
    public ResponseEntity<String> handleUserPermissionException(UserPermissionException e, WebRequest request){

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);

        String errorMessage = JSONObject.NULL.toString();
        if (e.getMessage() != null) {
            errorMessage = messageSource.getMessage(e.getMessageCode(), e.getArgs(), Locale.ENGLISH);
            log.trace("Resolved '{}' to '{}'", e.getMessageCode(), errorMessage);
        }

        JSONObject obj = new JSONObject();
        obj.put("error",sw.toString().strip());
        obj.put("errorMessage", errorMessage);

        return new ResponseEntity<>(
                obj.toString(),
                httpHeaders,
                HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(value = {IllegalCallerException.class, UserIsNotOwnerException.class})
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
