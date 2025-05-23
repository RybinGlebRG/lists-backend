package ru.rerumu.lists.controller.author;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.rerumu.lists.controller.author.out.AuthorView;
import ru.rerumu.lists.controller.author.out.AuthorsListView;
import ru.rerumu.lists.crosscut.exception.EntityNotFoundException;
import ru.rerumu.lists.model.author.Author;
import ru.rerumu.lists.model.user.User;
import ru.rerumu.lists.services.author.AuthorsService;
import ru.rerumu.lists.services.user.UserService;
import ru.rerumu.lists.views.AddAuthorView;

import java.util.List;

@CrossOrigin
@RestController
public class AuthorsController {

    private final AuthorsService authorsService;
    private final UserService userService;

    @Autowired
    public AuthorsController(
            AuthorsService authorsService,
            UserService userService
    ) {
        this.authorsService = authorsService;
        this.userService = userService;
    }

    @GetMapping(value = "/api/v1/authors/{authorId}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<String> getOne(
            @PathVariable Long authorId
    ) {
        Author author = authorsService.getAuthor(authorId);
        AuthorView authorView = new AuthorView(author);
        ResponseEntity<String> resEnt = new ResponseEntity<>(authorView.toString(), HttpStatus.OK);
        return resEnt;
    }

    @GetMapping(value = "/api/v1/users/{userId}/authors",
            produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<String> getAll(@PathVariable Long userId) {
        User user = userService.getOne(userId).orElseThrow(EntityNotFoundException::new);
        List<Author> authors = authorsService.getAuthors(user);
        AuthorsListView authorsListView = new AuthorsListView(authors);
        ResponseEntity<String> resEnt = new ResponseEntity<>(authorsListView.toString(), HttpStatus.OK);
        return resEnt;
    }

    @PostMapping(
            value = "/api/v1/users/{userId}/authors",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<String> addOne(
            @PathVariable Long userId,
            @RequestBody AddAuthorView addAuthorView
    ) {
        User user = userService.getOne(userId).orElseThrow(EntityNotFoundException::new);
        authorsService.addAuthor(addAuthorView, user);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(
            value = "/api/v1/authors/{authorId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<String> deleteOne( @PathVariable Long authorId) {
        authorsService.deleteAuthor(authorId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

