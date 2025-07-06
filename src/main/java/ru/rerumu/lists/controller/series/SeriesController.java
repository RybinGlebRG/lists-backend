package ru.rerumu.lists.controller.series;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.rerumu.lists.controller.series.view.out.SeriesListView;
import ru.rerumu.lists.controller.series.view.out.SeriesView;
import ru.rerumu.lists.controller.series.view.out.SeriesViewFactory;
import ru.rerumu.lists.model.series.Series;
import ru.rerumu.lists.services.series.SeriesService;
import ru.rerumu.lists.views.BookSeriesAddView;
import ru.rerumu.lists.controller.series.view.in.SeriesUpdateView;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
public class SeriesController {

    private final SeriesService seriesService;
    private final SeriesViewFactory seriesViewFactory;
    private final ObjectMapper objectMapper;

    @Autowired
    public SeriesController(
            SeriesService seriesService,
            SeriesViewFactory seriesViewFactory,
            ObjectMapper objectMapper
    ) {
        this.seriesService = seriesService;
        this.seriesViewFactory = seriesViewFactory;
        this.objectMapper = objectMapper;
    }

    @GetMapping(
            value = "/api/v1/users/{userId}/series/{seriesId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<String> getOne(
            @PathVariable Long userId,
            @PathVariable Long seriesId

    ) throws JsonProcessingException {
        Series series = seriesService.findById(seriesId, userId);
        SeriesView seriesView = seriesViewFactory.buildSeriesView(series.toDTO());
        String res = objectMapper.writeValueAsString(seriesView);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping(
            value = "/api/v1/users/{userId}/series",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<String> getAll(@PathVariable Long userId) throws JsonProcessingException {
        List<Series> series = seriesService.findAll(userId);

        SeriesListView seriesListView = seriesViewFactory.buildSeriesListView(
                series.stream()
                        .map(Series::toDTO)
                        .collect(Collectors.toCollection(ArrayList::new))
        );
        String res = objectMapper.writeValueAsString(seriesListView);
        ResponseEntity<String> resEnt = new ResponseEntity<>(res, HttpStatus.OK);
        return resEnt;
    }


    @PostMapping(
            value = "/api/v1/users/{userId}/series",
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<String> add(
            @PathVariable Long userId,
            @RequestBody BookSeriesAddView bookSeriesAddView
    ) {
        seriesService.add(userId, bookSeriesAddView);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    @DeleteMapping(
            value = "/api/v1/users/{userId}/bookSeries/{seriesId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<String> delete(
            @PathVariable Long userId,
            @PathVariable Long seriesId
    ) {
        seriesService.delete(seriesId, userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping(
            value = "/api/v1/users/{userId}/series/{seriesId}",
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<String> update(
            @PathVariable Long userId,
            @PathVariable Long seriesId,
            @RequestBody SeriesUpdateView seriesUpdateView
    ) {
        seriesService.updateSeries(seriesId, userId, seriesUpdateView);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}

