package ru.rerumu.lists.controller.book;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
import ru.rerumu.lists.controller.book.view.in.BookAddView;
import ru.rerumu.lists.controller.book.view.in.BookUpdateView;
import ru.rerumu.lists.controller.book.view.out.BookListView;
import ru.rerumu.lists.controller.book.view.out.BookView;
import ru.rerumu.lists.controller.book.view.out.BookViewFactory;
import ru.rerumu.lists.crosscut.exception.EmptyMandatoryParameterException;
import ru.rerumu.lists.crosscut.exception.EntityNotFoundException;
import ru.rerumu.lists.crosscut.exception.ServerException;
import ru.rerumu.lists.crosscut.exception.UserIsNotOwnerException;
import ru.rerumu.lists.domain.book.Book;
import ru.rerumu.lists.domain.books.Search;
import ru.rerumu.lists.services.book.BookService;
import ru.rerumu.lists.services.series.SeriesService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@Slf4j
public class BooksController {

    private final SeriesService seriesService;
    private final BookViewFactory bookViewFactory;
    private final ObjectMapper objectMapper;
    private final BookService bookService;

    @Autowired
    public BooksController(
            SeriesService seriesService,
            BookViewFactory bookViewFactory,
            ObjectMapper objectMapper,
            BookService bookService
    ) {
        this.seriesService = seriesService;
        this.bookViewFactory = bookViewFactory;
        this.objectMapper = objectMapper;
        this.bookService = bookService;
    }

    /**
     * Update book
     */
    @PutMapping(value = "/api/v1/users/{userId}/books/{bookId}",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<String> updateOne(
            @PathVariable Long userId,
            @PathVariable Long bookId,
            @RequestBody BookUpdateView bookUpdateView
    ) throws EmptyMandatoryParameterException, JsonProcessingException {
        Book book = bookService.updateBook(bookId, userId, bookUpdateView);
        BookView bookView = bookViewFactory.buildBookView(book.toDTO());
        String result = objectMapper.writeValueAsString(bookView);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Get book
     */
    @GetMapping(value = "/api/v1/users/{userId}/books/{bookId}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<String> getOne(
            @PathVariable Long userId,
            @PathVariable Long bookId,
            @RequestAttribute("username") String username
    ) throws UserIsNotOwnerException, JsonProcessingException {

        Book book = bookService.getBook(bookId, userId);
//        List<Series> seriesList = seriesService.findByBook((BookImpl) book, userId);

        BookView bookView = bookViewFactory.buildBookView(book.toDTO());
        String result = objectMapper.writeValueAsString(bookView);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Create book
     */
    @PostMapping(value = "/api/v1/users/{userId}/books",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<String> addOne(
            @PathVariable Long userId,
            @RequestBody BookAddView bookAddView
    ) throws EmptyMandatoryParameterException, EntityNotFoundException {

        try {
            Book book = bookService.addBook(bookAddView, userId);
            BookView bookView = bookViewFactory.buildBookView(book.toDTO());

            return new ResponseEntity<>(objectMapper.writeValueAsString(bookView), HttpStatus.OK);
        } catch (JsonProcessingException e) {
            throw new ServerException(e.getMessage(), e);
        }

    }

    /**
     * Delete book
     */
    @DeleteMapping(value = "/api/v1/users/{userId}/books/{bookId}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<String> deleteOne(
            @PathVariable Long userId,
            @PathVariable Long bookId
    ){
        bookService.deleteBook(bookId, userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    /**
     * Search books
     */
    @PostMapping(value = "/api/v1/users/{userId}/books/search",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<String> searchBooks(
            @RequestBody Search search,
            @RequestAttribute("authUserId") Long authUserId

    ) throws JsonProcessingException {

        List<Book> books = bookService.getAllBooks(search, authUserId);
        BookListView bookListView = bookViewFactory.buildBookListView(
                books.stream()
                        .map(Book::toDTO)
                        .collect(Collectors.toCollection(ArrayList::new)),
                search
        );

        String result = objectMapper.writeValueAsString(bookListView);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}

