package ru.rerumu.lists.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.rerumu.lists.exception.EmptyMandatoryParameterException;
import ru.rerumu.lists.exception.EntityNotFoundException;
import ru.rerumu.lists.exception.UserIsNotOwnerException;
import ru.rerumu.lists.model.Author;
import ru.rerumu.lists.model.Book;
import ru.rerumu.lists.services.AuthorsService;
import ru.rerumu.lists.services.ReadListService;
import ru.rerumu.lists.services.UserService;
import ru.rerumu.lists.views.BookAddView;
import ru.rerumu.lists.views.BookListView;
import ru.rerumu.lists.views.BookUpdateView;
import ru.rerumu.lists.views.BookView;

import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
public class BooksController {
    private final Logger logger = LoggerFactory.getLogger(BooksController.class);
    private final ReadListService readListService;
    private final UserService userService;

    private final AuthorsService authorsService;

    public BooksController(
            ReadListService readListService,
            UserService userService,
            AuthorsService authorsService
    ) {
        this.readListService = readListService;
        this.userService = userService;
        this.authorsService = authorsService;
    }

//    @PutMapping(value = "/api/v0.2/readLists/{readListId}/books/{bookId}",
//            produces = MediaType.APPLICATION_JSON_VALUE,
//            consumes = MediaType.APPLICATION_JSON_VALUE)
//    ResponseEntity<String> updateOne(@PathVariable Long readListId,
//                                     @PathVariable Long bookId,
//                                     @RequestBody Book newBook,
//                                     @RequestAttribute("username") String username) {
//        ResponseEntity<String> resEnt;
//        try {
//            Book updatedBook = readListService.updateBook(readListId, bookId, newBook);
//            resEnt = new ResponseEntity<>(updatedBook.toString(), HttpStatus.OK);
//        } catch (EmptyMandatoryParameterException e) {
//            resEnt = new ResponseEntity<>(
//                    "{\"errorMessage\":\"" + e.getMessage() + "\"}",
//                    HttpStatus.BAD_REQUEST);
//        }
//        return resEnt;
//    }


    @PutMapping(value = "/api/v0.2/books/{bookId}",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<String> updateOne(@PathVariable Long bookId,
                                     @RequestBody BookUpdateView bookUpdateView,
                                     @RequestAttribute("username") String username)
            throws EmptyMandatoryParameterException {

        readListService.updateBook(bookUpdateView);
        ResponseEntity<String> resEnt = new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return resEnt;
    }


    @GetMapping(value = "/api/v0.2/readLists/{readListId}/books/{bookId}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<String> getOne(@PathVariable Long readListId,
                                  @PathVariable Long bookId,
                                  @RequestAttribute("username") String username)
            throws UserIsNotOwnerException {
        userService.checkOwnership(username, readListId);
        // TODO: Check book ownership

        Book book = readListService.getBook(readListId, bookId);
        Optional<Author> author = authorsService.getAuthor(readListId, book.getAuthorId());

        BookView.Builder builder = new BookView.Builder()
                .book(book);
        author.ifPresent(builder::author);
        // TODO: write status, series

        ResponseEntity<String> resEnt = new ResponseEntity<>(builder.build().toString(), HttpStatus.OK);
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
        } catch (Exception e) {
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
            @RequestBody BookAddView bookAddView,
            @RequestAttribute("username") String username
    ) throws UserIsNotOwnerException, EmptyMandatoryParameterException, EntityNotFoundException {

        // Checking ownership
        userService.checkOwnership(username, readListId);
        if (bookAddView.getAuthorId() != null) {
            userService.checkOwnershipAuthor(username, bookAddView.getAuthorId());
        }
        if (bookAddView.getSeriesId() != null) {
            // TODO: Check ownership
        }

        Book book = readListService.addBook(readListId, bookAddView);

        BookView.Builder builder = new BookView.Builder()
                .book(book);

        ResponseEntity<String> resEnt = new ResponseEntity<>(builder.build().toString(), HttpStatus.OK);
        return resEnt;
    }

}

