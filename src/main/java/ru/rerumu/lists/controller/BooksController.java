package ru.rerumu.lists.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.rerumu.lists.exception.EmptyMandatoryParameterException;
import ru.rerumu.lists.exception.EntityNotFoundException;
import ru.rerumu.lists.exception.UserIsNotOwnerException;
import ru.rerumu.lists.model.AuthorBookRelation;
import ru.rerumu.lists.model.Book;
import ru.rerumu.lists.model.Series;
import ru.rerumu.lists.model.SeriesBookRelation;
import ru.rerumu.lists.model.books.Filter;
import ru.rerumu.lists.model.books.Search;
import ru.rerumu.lists.services.*;
import ru.rerumu.lists.views.*;

import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
public class BooksController {
    private final Logger logger = LoggerFactory.getLogger(BooksController.class);
    private final ReadListService readListService;
    private final UserService userService;

    private final AuthorsService authorsService;

    private final SeriesService seriesService;

    private final AuthorsBooksRelationService authorsBooksRelationService;

    private final BookSeriesRelationService bookSeriesRelationService;

    public BooksController(
            ReadListService readListService,
            @Qualifier("UserServiceProtectionProxy") UserService userService,
            AuthorsService authorsService,
            SeriesService seriesService,
            AuthorsBooksRelationService authorsBooksRelationService,
            BookSeriesRelationService bookSeriesRelationService
    ) {
        this.readListService = readListService;
        this.userService = userService;
        this.authorsService = authorsService;
        this.seriesService = seriesService;
        this.authorsBooksRelationService = authorsBooksRelationService;
        this.bookSeriesRelationService = bookSeriesRelationService;
    }

    @PutMapping(value = "/api/v0.2/books/{bookId}",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<String> updateOne(@PathVariable Long bookId,
                                     @RequestBody BookUpdateView bookUpdateView,
                                     @RequestAttribute("username") String username)
            throws EmptyMandatoryParameterException, CloneNotSupportedException {

        readListService.updateBook(bookId, bookUpdateView);
        ResponseEntity<String> resEnt = new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return resEnt;
    }


    @GetMapping(value = "/api/v0.2/readLists/{readListId}/books/{bookId}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<String> getOne(@PathVariable Long readListId,
                                  @PathVariable Long bookId,
                                  @RequestAttribute("username") String username)
            throws UserIsNotOwnerException, EntityNotFoundException {
        userService.checkOwnershipList(username, readListId);
        // TODO: Check book ownership

        Book book = readListService.getBook(readListId, bookId);

        if (book == null) {
            throw new EntityNotFoundException();
        }
        List<AuthorBookRelation> authorBookRelationList = authorsBooksRelationService.getByBookId(book.getBookId(), readListId);
        List<SeriesBookRelation> seriesBookRelationList = bookSeriesRelationService.getByBookId(book.getBookId(), readListId);
        List<Series> seriesList = seriesService.findByBook(book);

        BookView.Builder builder = new BookView.Builder()
                .bookStatus(book)
                .authorBookRelation(authorBookRelationList)
                .seriesBookRelation(seriesBookRelationList)
                .seriesList(seriesList);
        ResponseEntity<String> resEnt = new ResponseEntity<>(builder.build().toString(), HttpStatus.OK);
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

        userService.checkOwnershipList(username, readListId);
        userService.checkOwnership(username,bookAddView);

        readListService.addBook(readListId, bookAddView);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(value = "/api/v0.2/books/{bookId}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<String> deleteOne(
            @PathVariable Long bookId,
            @RequestAttribute("username") String username
    )
            throws UserIsNotOwnerException,
            EntityNotFoundException {

        userService.checkOwnershipBook(username, bookId);

        readListService.deleteBook(bookId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    @PostMapping(value = "/api/v0.2/readLists/{readListId}/books/search",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<String> searchBooks(
            @PathVariable Long readListId,
            @RequestBody Search search,
            @RequestAttribute("username") String username,
            @RequestAttribute("authUserId") Long authUserId

    ) throws UserIsNotOwnerException {
        // TODO: rewrite
//        userService.checkOwnershipList(username, readListId);

        List<Book> books = readListService.getAllBooks(readListId, search);
//        Map<Book,List<Series>> bookSeriesMap = seriesService.findByBook(books);
        BookListView bookListView = new BookListView.Builder()
                .bookList(books)
                .bookSeriesMap(null)
                .isChainBySeries(search.getChainBySeries())
                .sort(search.getSortItemList())
                .search(search)
                .build();
        bookListView.sort(search.getSortItemList());

        return new ResponseEntity<>(bookListView.toString(), HttpStatus.OK);
    }

}

