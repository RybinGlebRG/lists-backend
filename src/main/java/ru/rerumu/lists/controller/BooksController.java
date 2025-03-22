package ru.rerumu.lists.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.rerumu.lists.exception.EmptyMandatoryParameterException;
import ru.rerumu.lists.exception.EntityNotFoundException;
import ru.rerumu.lists.exception.UserIsNotOwnerException;
import ru.rerumu.lists.model.AuthorBookRelation;
import ru.rerumu.lists.model.SeriesBookRelation;
import ru.rerumu.lists.model.book.Book;
import ru.rerumu.lists.model.book.BookDTO;
import ru.rerumu.lists.model.book.impl.BookImpl;
import ru.rerumu.lists.model.books.Search;
import ru.rerumu.lists.model.series.Series;
import ru.rerumu.lists.model.series.item.SeriesItemType;
import ru.rerumu.lists.model.user.User;
import ru.rerumu.lists.services.AuthorsBooksRelationService;
import ru.rerumu.lists.services.BookSeriesRelationService;
import ru.rerumu.lists.services.author.AuthorsService;
import ru.rerumu.lists.services.book.ReadListService;
import ru.rerumu.lists.services.series.impl.SeriesServiceImpl;
import ru.rerumu.lists.services.user.UserService;
import ru.rerumu.lists.views.BookAddView;
import ru.rerumu.lists.views.BookListView;
import ru.rerumu.lists.views.BookUpdateView;
import ru.rerumu.lists.views.BookView;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
public class BooksController {
    private final Logger logger = LoggerFactory.getLogger(BooksController.class);
    private final ReadListService readListService;
    private final UserService userService;

    private final AuthorsService authorsService;

    private final SeriesServiceImpl seriesService;

    private final AuthorsBooksRelationService authorsBooksRelationService;

    private final BookSeriesRelationService bookSeriesRelationService;

    public BooksController(
            ReadListService readListService,
            @Qualifier("UserServiceProtectionProxy") UserService userService,
            AuthorsService authorsService,
            SeriesServiceImpl seriesService,
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
            throws UserIsNotOwnerException, EntityNotFoundException, EmptyMandatoryParameterException {
        userService.checkOwnershipList(username, readListId);
        // TODO: Check book ownership

        Book book = readListService.getBook(bookId);

        if (book == null) {
            throw new EntityNotFoundException();
        }
        List<AuthorBookRelation> authorBookRelationList = authorsBooksRelationService.getByBookId(book.getId(), readListId);
        List<SeriesBookRelation> seriesBookRelationList = bookSeriesRelationService.getByBookId(book.getId(), readListId);
        List<Series> seriesList = seriesService.findByBook((BookImpl) book);

        BookView.Builder builder = new BookView.Builder()
                .bookStatus((BookImpl) book)
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
            @RequestAttribute("username") String username,
            @RequestAttribute("authUserId") Long authUserId
    ) throws UserIsNotOwnerException, EmptyMandatoryParameterException, EntityNotFoundException {

        userService.checkOwnershipList(username, readListId);
        userService.checkOwnership(username,bookAddView);

        User user = userService.getOne(authUserId).orElseThrow(EntityNotFoundException::new);

        readListService.addBook(readListId, bookAddView, user);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(value = "/api/v0.2/books/{bookId}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<String> deleteOne(
            @PathVariable Long bookId,
            @RequestAttribute("username") String username
    )
            throws UserIsNotOwnerException,
            EntityNotFoundException, EmptyMandatoryParameterException {

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
//        User user = userService.getOne(authUserId).orElseThrow(EntityNotFoundException::new);

        List<Book> books = readListService.getAllBooks(readListId, search);


        List<ru.rerumu.lists.controller.book.view.out.BookView> bookViews = books.stream()
                .map(Book::toDTO)
                .map(ru.rerumu.lists.controller.book.view.out.BookView::new)
                .collect(Collectors.toCollection(ArrayList::new));

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

