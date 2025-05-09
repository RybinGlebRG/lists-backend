package ru.rerumu.lists.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.rerumu.lists.crosscut.exception.EntityNotFoundException;
import ru.rerumu.lists.crosscut.exception.UserIsNotOwnerException;
import ru.rerumu.lists.model.Author;
import ru.rerumu.lists.services.author.AuthorsService;
import ru.rerumu.lists.services.book.ReadListService;
import ru.rerumu.lists.services.user.UserService;
import ru.rerumu.lists.views.AddAuthorView;
import ru.rerumu.lists.views.AuthorsListView;

import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
public class AuthorsController {

    private final ReadListService readListService;
    private final AuthorsService authorsService;
    private final UserService userService;

    @Autowired
    public AuthorsController(
            ReadListService readListService,
            AuthorsService authorsService,
            @Qualifier("UserServiceProtectionProxy") UserService userService
    ) {
        this.readListService = readListService;
        this.authorsService = authorsService;
        this.userService = userService;
    }

    @GetMapping(value = "/api/v0.2/readLists/{readListId}/authors/{authorId}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<String> getOne(@PathVariable Long readListId,
                                  @PathVariable Long authorId,
                                  @RequestAttribute("username") String username) throws UserIsNotOwnerException {
        userService.checkOwnershipList(username, readListId);
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
        userService.checkOwnershipList(username, readListId);
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
        userService.checkOwnershipList(username, readListId);
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

