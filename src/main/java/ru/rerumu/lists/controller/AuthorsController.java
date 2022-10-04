package ru.rerumu.lists.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.rerumu.lists.exception.EntityNotFoundException;
import ru.rerumu.lists.exception.UserIsNotOwnerException;
import ru.rerumu.lists.model.Author;
import ru.rerumu.lists.services.AuthorsService;
import ru.rerumu.lists.services.ReadListService;
import ru.rerumu.lists.services.UserService;
import ru.rerumu.lists.views.AddAuthorView;
import ru.rerumu.lists.views.AuthorsListView;

import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
public class AuthorsController {

    @Autowired
    private ReadListService readListService;

    @Autowired
    private AuthorsService authorsService;

    @Autowired
    private UserService userService;


    @GetMapping(value = "/api/v0.2/readLists/{readListId}/authors/{authorId}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<String> getOne(@PathVariable Long readListId,
                                  @PathVariable Long authorId,
                                  @RequestAttribute("username") String username) throws UserIsNotOwnerException {
        userService.checkOwnership(username, readListId);
        Optional<Author> author = authorsService.getAuthor(readListId, authorId);
        if (author.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            ResponseEntity<String> resEnt = new ResponseEntity<>(author.get().toString(), HttpStatus.OK);
            return resEnt;
        }
    }

    @GetMapping(value = "/api/v0.2/readLists/{readListId}/authors",
            produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<String> getAll(@PathVariable Long readListId,
                                  @RequestAttribute("username") String username) throws UserIsNotOwnerException {
        userService.checkOwnership(username, readListId);
        List<Author> authors = readListService.getAuthors(readListId);
        AuthorsListView authorsListView = new AuthorsListView(authors);
        ResponseEntity<String> resEnt = new ResponseEntity<>(authorsListView.toString(), HttpStatus.OK);
        return resEnt;
    }

    @PostMapping(
            value = "/api/v0.2/readLists/{readListId}/authors",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<String> addOne(@PathVariable Long readListId,
                                  @RequestAttribute("username") String username,
                                  @RequestBody AddAuthorView addAuthorView)
            throws UserIsNotOwnerException, EntityNotFoundException {
        userService.checkOwnership(username, readListId);
        Author author = authorsService.addAuthor(readListId, addAuthorView);
        return new ResponseEntity<>(author.toString(), HttpStatus.CREATED);
    }

    @DeleteMapping(
            value = "/api/v0.2/authors/{authorId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<String> deleteOne(
            @PathVariable Long authorId,
            @RequestAttribute("username") String username
    ) throws UserIsNotOwnerException {
        userService.checkOwnershipAuthor(username, authorId);
        authorsService.deleteAuthor(authorId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

