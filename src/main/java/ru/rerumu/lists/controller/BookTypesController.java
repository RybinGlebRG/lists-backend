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
import ru.rerumu.lists.factories.UserServiceProxyFactory;
import ru.rerumu.lists.model.*;
import ru.rerumu.lists.model.books.Search;
import ru.rerumu.lists.services.*;
import ru.rerumu.lists.views.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
public class BookTypesController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final BookTypesService bookTypesService;
    private final UserService userService;

    public BookTypesController(
            BookTypesService bookTypesService,
            @Qualifier("UserServiceProtectionProxy") UserService userService
    ) {
        this.bookTypesService = bookTypesService;
        this.userService = userService;
    }

    @GetMapping(value = "/api/v0.2/bookTypes",
            produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<String> getAll() {

        List<BookType> bookTypeList = bookTypesService.findAll();
        BookTypesListView bookTypesListView = new BookTypesListView(bookTypeList);

        return new ResponseEntity<>(bookTypesListView.toString(), HttpStatus.OK);
    }

}

