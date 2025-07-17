package ru.rerumu.lists.controller;

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
import ru.rerumu.lists.crosscut.exception.EmptyMandatoryParameterException;
import ru.rerumu.lists.domain.TitlesList;
import ru.rerumu.lists.domain.title.Title;
import ru.rerumu.lists.services.WatchListService;
import ru.rerumu.lists.views.TitleCreateView;
import ru.rerumu.lists.views.TitleListView;

@CrossOrigin
@RestController
public class TitlesController {

    private final WatchListService watchListService;

    @Autowired
    public TitlesController(WatchListService watchListService) {
        this.watchListService = watchListService;
    }

    @GetMapping(value = "/api/v0.2/watchLists/{watchListId}/titles", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<String> getAll(@PathVariable long watchListId, @RequestAttribute("username") String username) {
        TitlesList titlesList = watchListService.getAll(watchListId);
        TitleListView titleListView = TitleListView.builder()
                .titlesList(titlesList)
                .build();
        ResponseEntity<String> resEnt = new ResponseEntity<>(titleListView.toString(), HttpStatus.OK);
        return resEnt;
    }

    @PostMapping(value = "/api/v0.2/watchLists/{watchListId}/titles", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<String> addOne(
            @PathVariable long watchListId,
            @RequestBody TitleCreateView newTitle,
            @RequestAttribute("username") String username) {
        ResponseEntity<String> resEnt;
        try {
            Title title = watchListService.addTitle(watchListId, newTitle);
            resEnt = new ResponseEntity<>(title.toString(), HttpStatus.CREATED);
        } catch (Exception e) {
            resEnt = new ResponseEntity<>(
                    "{\"errorMessage\":\"" + e.getMessage() + "\"}",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return resEnt;
    }

    @GetMapping(value = "/api/v0.2/watchLists/{watchListId}/titles/{titleId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<String> getOne(@PathVariable Long watchListId,
                                  @RequestAttribute("username") String username,
                                  @PathVariable Long titleId) {
        ResponseEntity<String> resEnt;
        try {
            Title title = watchListService.getOne(watchListId, titleId);
            resEnt = new ResponseEntity<>(title.toString(), HttpStatus.OK);
        } catch (Exception e) {
            resEnt = new ResponseEntity<>(
                    "{\"errorMessage\":\"" + e.getMessage() + "\"}",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return resEnt;
    }

    @PutMapping(value = "/api/v0.2/watchLists/{watchListId}/titles/{titleId}",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<String> updateOne(@PathVariable Long watchListId,
                                     @PathVariable Long titleId,
                                     @RequestBody Title newTitle,
                                     @RequestAttribute("username") String username) {
        ResponseEntity<String> resEnt;
        try {
            Title updatedTitle = watchListService.updateTitle(watchListId, titleId, newTitle);
            resEnt = new ResponseEntity<>(updatedTitle.toString(), HttpStatus.OK);
        } catch (EmptyMandatoryParameterException e) {
            resEnt = new ResponseEntity<>(
                    "{\"errorMessage\":\"" + e.getMessage() + "\"}",
                    HttpStatus.BAD_REQUEST);
        }
        return resEnt;
    }

    @DeleteMapping(value = "/api/v0.2/titles/{titleId}")
    ResponseEntity<String> updateOne(@PathVariable Long titleId,
                                     @RequestAttribute("username") String username) {
        // TODO: Check ownership

        watchListService.deleteOne(titleId);
        ResponseEntity<String> resEnt = new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return resEnt;
    }


}
