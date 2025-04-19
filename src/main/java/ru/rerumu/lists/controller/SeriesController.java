package ru.rerumu.lists.controller;

import org.springframework.beans.factory.annotation.Autowired;
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
import ru.rerumu.lists.crosscut.exception.EmptyMandatoryParameterException;
import ru.rerumu.lists.crosscut.exception.EntityHasChildrenException;
import ru.rerumu.lists.crosscut.exception.EntityNotFoundException;
import ru.rerumu.lists.crosscut.exception.UserIsNotOwnerException;
import ru.rerumu.lists.model.series.Series;
import ru.rerumu.lists.services.BookSeriesRelationService;
import ru.rerumu.lists.services.MonitoringService;
import ru.rerumu.lists.services.book.ReadListService;
import ru.rerumu.lists.services.series.SeriesService;
import ru.rerumu.lists.services.series.impl.SeriesServiceImpl;
import ru.rerumu.lists.services.user.UserService;
import ru.rerumu.lists.views.BookSeriesAddView;
import ru.rerumu.lists.views.BookSeriesView;
import ru.rerumu.lists.views.SeriesListView;
import ru.rerumu.lists.views.seriesupdate.SeriesUpdateView;

import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
public class SeriesController {


    private final ReadListService readListService;

    private final UserService userService;

    private final BookSeriesRelationService bookSeriesRelationService;

    private final SeriesServiceImpl seriesServiceImpl;

    private final MonitoringService monitoringService = MonitoringService.getServiceInstance();

    private final SeriesService seriesService;


    @Autowired
    public SeriesController(
            ReadListService readListService,
            @Qualifier("UserServiceProtectionProxy")  UserService userService,
            BookSeriesRelationService bookSeriesRelationService,
            SeriesServiceImpl seriesServiceImpl, SeriesService seriesService
    ) {
        this.readListService = readListService;
        this.userService = userService;
        this.bookSeriesRelationService = bookSeriesRelationService;
        this.seriesServiceImpl = seriesServiceImpl;
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
        Optional<Series> optionalSeries = seriesServiceImpl.getSeries(seriesId);
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
    ResponseEntity<String> getAll(@PathVariable Long readListId) throws Exception {

        List<Series> series = seriesService.getAll(readListId);
        SeriesListView.Builder builder = new SeriesListView.Builder()
                .seriesList(series);
        SeriesListView seriesListView = builder.build();
        seriesListView.sort();
        ResponseEntity<String> resEnt = new ResponseEntity<>(seriesListView.toString(), HttpStatus.OK);
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

        seriesServiceImpl.add(readListId, bookSeriesAddView);

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

        seriesServiceImpl.delete(seriesId);

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
            throws UserIsNotOwnerException, EntityNotFoundException, EmptyMandatoryParameterException {

        userService.checkOwnershipSeries(username, seriesId);

        seriesServiceImpl.updateSeries(seriesId,seriesUpdateView);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}

