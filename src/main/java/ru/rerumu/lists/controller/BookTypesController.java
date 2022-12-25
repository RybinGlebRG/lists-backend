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
import ru.rerumu.lists.model.AuthorBookRelation;
import ru.rerumu.lists.model.Book;
import ru.rerumu.lists.model.BookType;
import ru.rerumu.lists.model.SeriesBookRelation;
import ru.rerumu.lists.model.books.Search;
import ru.rerumu.lists.services.*;
import ru.rerumu.lists.views.*;

import java.util.List;

@CrossOrigin
@RestController
public class BookTypesController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final BookTypesService bookTypesService;

    public BookTypesController(
            BookTypesService bookTypesService
    ) {
        this.bookTypesService = bookTypesService;
    }

    @GetMapping(value = "/api/v0.2/bookTypes",
            produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<String> getAll(@RequestAttribute("username") String username) {
        List<BookType> bookTypeList = bookTypesService.findAll();
        BookTypesListView bookTypesListView = new BookTypesListView(bookTypeList);

        return new ResponseEntity<>(bookTypesListView.toString(), HttpStatus.OK);
    }

}

