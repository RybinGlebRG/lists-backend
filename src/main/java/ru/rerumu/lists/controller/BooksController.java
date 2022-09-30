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
import ru.rerumu.lists.model.AuthorBookRelation;
import ru.rerumu.lists.model.Book;
import ru.rerumu.lists.model.Series;
import ru.rerumu.lists.services.*;
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

    private final BookSeriesService bookSeriesService;

    private final AuthorsBooksService authorsBooksService;

    public BooksController(
            ReadListService readListService,
            UserService userService,
            AuthorsService authorsService,
            BookSeriesService bookSeriesService,
            AuthorsBooksService authorsBooksService
    ) {
        this.readListService = readListService;
        this.userService = userService;
        this.authorsService = authorsService;
        this.bookSeriesService = bookSeriesService;
        this.authorsBooksService = authorsBooksService;
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
            throws UserIsNotOwnerException, EntityNotFoundException {
        userService.checkOwnership(username, readListId);
        // TODO: Check book ownership

        Book book = readListService.getBook(readListId, bookId);

        if (book == null) {
            throw new EntityNotFoundException();
        }
        List<AuthorBookRelation> authorBookRelationList = authorsBooksService.getByBookId(book.getBookId());
//        Optional<Author> author = authorsService.getAuthor(readListId, book.getAuthorId());
        Optional<Series> series = bookSeriesService.getSeries(readListId, book.getSeriesId());

        BookView.Builder builder = new BookView.Builder()
                .book(book)
                .authorBookRelation(authorBookRelationList);
//        author.ifPresent(builder::author);
        series.ifPresent(builder::series);

        ResponseEntity<String> resEnt = new ResponseEntity<>(builder.build().toString(), HttpStatus.OK);
        return resEnt;
    }


    @GetMapping(value = "/api/v0.2/readLists/{readListId}/books",
            produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<String> getAll(@PathVariable Long readListId,
                                  @RequestAttribute("username") String username) {
        List<Book> books = readListService.getAllBooks(readListId);
        BookListView bookListView = new BookListView(books);
        bookListView.sort();

        ResponseEntity<String> resEnt = new ResponseEntity<>(bookListView.toString(), HttpStatus.OK);
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

