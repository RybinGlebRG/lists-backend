package ru.rerumu.lists.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RestController;
import ru.rerumu.lists.exception.EntityNotFoundException;
import ru.rerumu.lists.model.BookStatusRecord;
import ru.rerumu.lists.model.BookType;
import ru.rerumu.lists.model.User;
import ru.rerumu.lists.services.BookStatusesService;
import ru.rerumu.lists.services.BookTypesService;
import ru.rerumu.lists.services.UserService;
import ru.rerumu.lists.views.BookStatusesView;
import ru.rerumu.lists.views.BookTypesListView;

import java.util.List;

@CrossOrigin
@RestController
public class BookStatusesController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final BookStatusesService bookStatusesService;
    private final UserService userService;

    public BookStatusesController(BookStatusesService bookStatusesService, UserService userService) {
        this.bookStatusesService = bookStatusesService;
        this.userService = userService;
    }

    @GetMapping(value = "/api/v0.2/bookStatuses",
            produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<String> getAll() {

        List<BookStatusRecord> bookStatusRecordList = bookStatusesService.findAll();
        BookStatusesView bookStatusesView = new BookStatusesView(bookStatusRecordList);

        return new ResponseEntity<>(bookStatusesView.toString(), HttpStatus.OK);
    }

}

