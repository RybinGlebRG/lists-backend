package ru.rerumu.lists.controller;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.rerumu.lists.model.Author;
import ru.rerumu.lists.model.Title;
import ru.rerumu.lists.model.TokenRequest;
import ru.rerumu.lists.services.ReadListService;
import ru.rerumu.lists.services.UserService;

@CrossOrigin
@RestController
public class UsersController {

    @Autowired
    private UserService userService;


    @PostMapping(
            value = "/api/v0.2/users/tokens",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<String> createToken(@RequestBody TokenRequest tokenRequest) {
        ResponseEntity<String> resEnt;
        try {
            String token = userService.createToken(tokenRequest);
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

