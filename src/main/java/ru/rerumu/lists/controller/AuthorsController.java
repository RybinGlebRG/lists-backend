package ru.rerumu.lists.controller;

import liquibase.pro.packaged.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.rerumu.lists.model.Author;
import ru.rerumu.lists.model.Series;
import ru.rerumu.lists.services.ReadListService;
import ru.rerumu.lists.views.AuthorsListView;

import java.util.List;

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
        Author author = readListService.getAuthor(readListId, authorId);
        ResponseEntity<String> resEnt = new ResponseEntity<>(author.toString(), HttpStatus.OK);
        return resEnt;
    }

    @GetMapping(value = "/api/v0.2/readLists/{readListId}/authors",
            produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<String> getAll(@PathVariable Long readListId,
                                  @RequestAttribute("username") String username) {
        List<Author> authors = readListService.getAuthors(readListId);
        AuthorsListView authorsListView = new AuthorsListView(authors);
        ResponseEntity<String> resEnt = new ResponseEntity<>(authorsListView.toString(), HttpStatus.OK);
        return resEnt;
    }
}

