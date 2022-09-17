package ru.rerumu.lists.controller;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.rerumu.lists.exception.EmptyMandatoryParameterException;
import ru.rerumu.lists.exception.UserIsNotOwnerException;
import ru.rerumu.lists.model.Author;
import ru.rerumu.lists.model.Book;
import ru.rerumu.lists.model.Series;
import ru.rerumu.lists.services.AuthorsService;
import ru.rerumu.lists.services.ReadListService;
import ru.rerumu.lists.services.UserService;
import ru.rerumu.lists.views.AddBookView;
import ru.rerumu.lists.views.BookListView;

import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
public class BooksController {
    private final Logger logger = LoggerFactory.getLogger(BooksController.class);
    @Autowired
    private ReadListService readListService;

    @Autowired
    private AuthorsService authorsService;

    @Autowired
    private UserService userService;

    @PutMapping(value = "/api/v0.2/readLists/{readListId}/books/{bookId}",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<String> updateOne(@PathVariable Long readListId,
                                     @PathVariable Long bookId,
                                     @RequestBody Book newBook,
                                     @RequestAttribute("username") String username) {
        ResponseEntity<String> resEnt;
        try {
            Book updatedBook = readListService.updateBook(readListId, bookId, newBook);
            resEnt = new ResponseEntity<>(updatedBook.toString(), HttpStatus.OK);
        } catch (EmptyMandatoryParameterException e) {
            resEnt = new ResponseEntity<>(
                    "{\"errorMessage\":\"" + e.getMessage() + "\"}",
                    HttpStatus.BAD_REQUEST);
        }
        return resEnt;
    }


    @GetMapping(value = "/api/v0.2/readLists/{readListId}/books/{bookId}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<String> getOne(@PathVariable Long readListId,
                                  @PathVariable Long bookId,
                                  @RequestAttribute("username") String username) {
        Book book = readListService.getBook(readListId, bookId);
        ResponseEntity<String> resEnt = new ResponseEntity<>(book.toString(), HttpStatus.OK);
        return resEnt;
    }


    @GetMapping(value = "/api/v0.2/readLists/{readListId}/books",
            produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<String> getAll(@PathVariable Long readListId,
                                  @RequestAttribute("username") String username) {
        ResponseEntity<String> resEnt;
        try {
            List<Book> books = readListService.getAllBooks(readListId);
            BookListView bookListView = new BookListView(books);
            bookListView.sort();

            resEnt = new ResponseEntity<>(bookListView.toString(), HttpStatus.OK);
        } catch (Exception e){
            resEnt = new ResponseEntity<>(
                    "{\"errorMessage\":\"" + e.getMessage() + "\"}",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return resEnt;
    }

    @PostMapping(value = "/api/v0.2/readLists/{readListId}/books",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<String> addOne(
            @PathVariable Long readListId,
            @RequestBody AddBookView addBookView,
            @RequestAttribute("username") String username
    ) throws UserIsNotOwnerException {

        // Checking ownership
        userService.checkOwnership(username,readListId);
        if (addBookView.getAuthor().isPresent()){
            userService.checkOwnershipAuthor(username,addBookView.getAuthor().get().getAuthorId());
        }
        if (addBookView.getSeries().isPresent()){
            // TODO: Check ownership
        }

        Book book = readListService.addBook(readListId,addBookView);

        ResponseEntity<String> resEnt = new ResponseEntity<>(book.toString(), HttpStatus.OK);
        return resEnt;
    }

}

