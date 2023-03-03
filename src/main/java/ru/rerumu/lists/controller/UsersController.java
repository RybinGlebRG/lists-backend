package ru.rerumu.lists.controller;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.rerumu.lists.model.TokenRequest;
import ru.rerumu.lists.services.UserServiceImpl;

@CrossOrigin
@RestController
public class UsersController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserServiceImpl userService;


    @PostMapping(
            value = "/api/v0.2/users/tokens",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<String> createToken(@RequestBody TokenRequest tokenRequest) {
        ResponseEntity<String> resEnt;
        try {
            logger.info(String.format("Got request %s",tokenRequest));
            String token = userService.createToken(tokenRequest);
            logger.info(String.format("Got token %s",token));
            JSONObject res = new JSONObject();
            res.put("token",token);
            resEnt = new ResponseEntity<>(res.toString(), HttpStatus.OK);
        } catch (Exception e){
            resEnt = new ResponseEntity<>(
                    "{\"errorMessage\":\"" + e.getMessage() + "\"}",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return resEnt;
    }
}

