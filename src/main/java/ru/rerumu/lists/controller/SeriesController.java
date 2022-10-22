package ru.rerumu.lists.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.rerumu.lists.exception.EmptyMandatoryParameterException;
import ru.rerumu.lists.exception.EntityNotFoundException;
import ru.rerumu.lists.exception.UserIsNotOwnerException;
import ru.rerumu.lists.model.Book;
import ru.rerumu.lists.model.Series;
import ru.rerumu.lists.model.SeriesBookRelation;
import ru.rerumu.lists.services.BookSeriesRelationService;
import ru.rerumu.lists.services.ReadListService;
import ru.rerumu.lists.services.UserService;
import ru.rerumu.lists.views.BookSeriesView;
import ru.rerumu.lists.views.SeriesListView;

import java.util.List;

@CrossOrigin
@RestController
public class SeriesController {


    private final ReadListService readListService;

    private final UserService userService;

    private final BookSeriesRelationService bookSeriesRelationService;


    public SeriesController(
            ReadListService readListService,
            UserService userService,
            BookSeriesRelationService bookSeriesRelationService
    ) {
        this.readListService = readListService;
        this.userService = userService;
        this.bookSeriesRelationService = bookSeriesRelationService;
    }

    @GetMapping(value = "/api/v0.2/readLists/{readListId}/series/{seriesId}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<String> getOne(@PathVariable Long readListId,
                                  @PathVariable Long seriesId,
                                  @RequestAttribute("username") String username
    ) throws UserIsNotOwnerException, EntityNotFoundException {

        userService.checkOwnershipSeries(username, seriesId);
        userService.checkOwnershipList(username, readListId);

        ResponseEntity<String> resEnt;
        Series series = readListService.getSeries(readListId, seriesId);
        List<SeriesBookRelation> seriesBookRelationList = bookSeriesRelationService.getBySeriesId(seriesId);

        BookSeriesView.Builder builder = new BookSeriesView.Builder(series)
                .seriesBookRelationList(seriesBookRelationList);

        resEnt = new ResponseEntity<>(builder.build().toString(), HttpStatus.OK);
        return resEnt;
    }

    @GetMapping(value = "/api/v0.2/readLists/{readListId}/series",
            produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<String> getAll(@PathVariable Long readListId,
                                  @RequestAttribute("username") String username) {
        ResponseEntity<String> resEnt;
        List<Series> series = readListService.getAllSeries(readListId);
        SeriesListView seriesListView = new SeriesListView(series);
        seriesListView.sort();
        resEnt = new ResponseEntity<>(seriesListView.toString(), HttpStatus.OK);
        return resEnt;
    }
}

