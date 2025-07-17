package ru.rerumu.lists.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.rerumu.lists.domain.book.type.BookType;
import ru.rerumu.lists.services.book.type.BookTypesService;
import ru.rerumu.lists.services.user.UserService;
import ru.rerumu.lists.views.BookTypesListView;

import java.util.List;

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

