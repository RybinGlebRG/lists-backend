package ru.rerumu.lists.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.rerumu.lists.exception.EntityHasChildrenException;
import ru.rerumu.lists.exception.EntityNotFoundException;
import ru.rerumu.lists.exception.UserIsNotOwnerException;
import ru.rerumu.lists.model.Series;
import ru.rerumu.lists.services.BookSeriesRelationService;
import ru.rerumu.lists.services.SeriesService;
import ru.rerumu.lists.services.ReadListService;
import ru.rerumu.lists.services.UserService;
import ru.rerumu.lists.views.BookSeriesAddView;
import ru.rerumu.lists.views.BookSeriesView;
import ru.rerumu.lists.views.SeriesListView;
import ru.rerumu.lists.views.series_update.SeriesUpdateView;

import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
public class SeriesController {


    private final ReadListService readListService;

    private final UserService userService;

    private final BookSeriesRelationService bookSeriesRelationService;

    private final SeriesService seriesService;


    public SeriesController(
            ReadListService readListService,
            UserService userService,
            BookSeriesRelationService bookSeriesRelationService,
            SeriesService seriesService
    ) {
        this.readListService = readListService;
        this.userService = userService;
        this.bookSeriesRelationService = bookSeriesRelationService;
        this.seriesService = seriesService;
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
        Optional<Series> optionalSeries = seriesService.getSeries(seriesId);
        if (optionalSeries.isEmpty()){
            return new ResponseEntity<>( HttpStatus.NOT_FOUND);
        } else {
            BookSeriesView.Builder builder = new BookSeriesView.Builder(optionalSeries.get());
            BookSeriesView bookSeriesView = builder.build();
            return new ResponseEntity<>(bookSeriesView.toString(), HttpStatus.OK);
        }
    }

    @GetMapping(value = "/api/v0.2/readLists/{readListId}/series",
            produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<String> getAll(@PathVariable Long readListId,
                                  @RequestAttribute("username") String username) throws UserIsNotOwnerException {
        userService.checkOwnershipList(username, readListId);

        ResponseEntity<String> resEnt;
        List<Series> series = seriesService.getAll(readListId);
        var seriesRelations = bookSeriesRelationService.get(series);
        SeriesListView.Builder builder = new SeriesListView.Builder()
                .seriesList(series)
                .seriesRelations(seriesRelations);
        SeriesListView seriesListView = builder.build();
        seriesListView.sort();
        resEnt = new ResponseEntity<>(seriesListView.toString(), HttpStatus.OK);
        return resEnt;
    }


    @PostMapping(
            value = "/api/v0.2/readLists/{readListId}/series",
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<String> add(
            @PathVariable Long readListId,
            @RequestBody BookSeriesAddView bookSeriesAddView,
            @RequestAttribute("username") String username
    ) throws UserIsNotOwnerException {
        userService.checkOwnershipList(username, readListId);

        seriesService.add(readListId, bookSeriesAddView);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    @DeleteMapping(value = "/api/v0.2/bookSeries/{seriesId}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<String> delete(
            @PathVariable long seriesId,
            @RequestAttribute("username") String username
    )
            throws UserIsNotOwnerException, EntityHasChildrenException, EntityNotFoundException {

        userService.checkOwnershipSeries(username, seriesId);

        seriesService.delete(seriesId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping(
            value = "/api/v0.2/series/{seriesId}",
            consumes = MediaType.APPLICATION_JSON_VALUE
    )   ResponseEntity<String> update(
            @PathVariable long seriesId,
            @RequestBody SeriesUpdateView seriesUpdateView,
            @RequestAttribute("username") String username
    )
            throws UserIsNotOwnerException, EntityNotFoundException {

        userService.checkOwnershipSeries(username, seriesId);

        seriesService.updateSeries(seriesId,seriesUpdateView);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}

