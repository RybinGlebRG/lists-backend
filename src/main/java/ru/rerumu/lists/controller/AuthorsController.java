package ru.rerumu.lists.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.rerumu.lists.model.Author;
import ru.rerumu.lists.model.Series;
import ru.rerumu.lists.services.ReadListService;

@CrossOrigin
@RestController
public class AuthorsController {

    @Autowired
    private ReadListService readListService;


    @GetMapping(value = "/api/v0.2/readLists/{readListId}/authors/{authorId}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<String> getOne(@PathVariable Long readListId,
                                  @PathVariable Long authorId,
                                  @RequestAttribute("username") String username) {
        ResponseEntity<String> resEnt;
        try {
            Author author = readListService.getAuthor(readListId,authorId);
            resEnt = new ResponseEntity<>(author.toString(), HttpStatus.OK);
        } catch (Exception e){
            resEnt = new ResponseEntity<>(
                    "{\"errorMessage\":\"" + e.getMessage() + "\"}",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return resEnt;
    }
}

