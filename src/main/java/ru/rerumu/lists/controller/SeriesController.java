package ru.rerumu.lists.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.rerumu.lists.exception.EmptyMandatoryParameterException;
import ru.rerumu.lists.model.Book;
import ru.rerumu.lists.model.Series;
import ru.rerumu.lists.services.ReadListService;
import ru.rerumu.lists.views.SeriesListView;

import java.util.List;

@CrossOrigin
@RestController
public class SeriesController {

    @Autowired
    private ReadListService readListService;


    @GetMapping(value = "/api/v0.2/readLists/{readListId}/series/{seriesId}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<String> getOne(@PathVariable Long readListId,
                                  @PathVariable Long seriesId,
                                  @RequestAttribute("username") String username) {
        ResponseEntity<String> resEnt;
        try {
            Series series = readListService.getSeries(readListId, seriesId);
            resEnt = new ResponseEntity<>(series.toString(), HttpStatus.OK);
        } catch (Exception e){
            resEnt = new ResponseEntity<>(
                    "{\"errorMessage\":\"" + e.getMessage() + "\"}",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return resEnt;
    }

    @GetMapping(value = "/api/v0.2/readLists/{readListId}/series",
            produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<String> getAll(@PathVariable Long readListId,
                                  @RequestAttribute("username") String username) {
        ResponseEntity<String> resEnt;
        try {
            List<Series> series = readListService.getAllSeries(readListId);
            SeriesListView seriesListView = new SeriesListView(series);
            seriesListView.sort();
            resEnt = new ResponseEntity<>(seriesListView.toString(), HttpStatus.OK);
        } catch (Exception e){
            resEnt = new ResponseEntity<>(
                    "{\"errorMessage\":\"" + e.getMessage() + "\"}",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return resEnt;
    }
}

