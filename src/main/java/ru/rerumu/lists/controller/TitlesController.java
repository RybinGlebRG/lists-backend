package ru.rerumu.lists.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.rerumu.lists.model.Book;
import ru.rerumu.lists.model.Title;
import ru.rerumu.lists.model.TitlesList;
import ru.rerumu.lists.repository.TitlesRepository;
import ru.rerumu.lists.exception.EmptyMandatoryParameterException;
import ru.rerumu.lists.services.WatchListService;
import ru.rerumu.lists.views.TitleCreateView;

import java.util.List;

@CrossOrigin
@RestController
public class TitlesController{

    @Autowired
    private TitlesRepository repository;

    @Autowired
    private WatchListService watchListService;

    @GetMapping(value = "/api/v0.2/watchLists/{watchListId}/titles", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<String> getAll(@PathVariable long watchListId, @RequestAttribute("username") String username){
//        List<Title> titles = this.repository.getAll(watchListId);
//        TitlesList titlesList = new TitlesList(titles);
//        titlesList.sort();
//        ResponseEntity<String> resEnt = new ResponseEntity<String>(titlesList.toString(), HttpStatus.OK);
//        return resEnt;
        ResponseEntity<String> resEnt;
        try {
            TitlesList titlesList = watchListService.getAll(watchListId);
            resEnt = new ResponseEntity<>(titlesList.toString(), HttpStatus.OK);
        } catch (Exception e){
            resEnt = new ResponseEntity<>(
                    "{\"errorMessage\":\"" + e.getMessage() + "\"}",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return resEnt;


    }

    @PostMapping(value = "/api/v0.2/watchLists/{watchListId}/titles", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<String> addOne(
            @PathVariable long watchListId,
            @RequestBody TitleCreateView newTitle,
            @RequestAttribute("username") String username){
        ResponseEntity<String> resEnt;
        try {
            Title title = watchListService.addTitle(watchListId,newTitle);
            resEnt = new ResponseEntity<>(title.toString(), HttpStatus.CREATED);
        } catch (Exception e){
            resEnt = new ResponseEntity<>(
                    "{\"errorMessage\":\"" + e.getMessage() + "\"}",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return resEnt;
    }

    @GetMapping(value = "/api/v0.2/watchLists/{watchListId}/titles/{titleId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<String> getOne(@PathVariable Long watchListId,
                                  @RequestAttribute("username") String username,
                                  @PathVariable Long titleId){
        ResponseEntity<String> resEnt;
        try {
            Title title = watchListService.getOne(watchListId, titleId);
            resEnt = new ResponseEntity<>(title.toString(), HttpStatus.OK);
        } catch (Exception e){
            resEnt = new ResponseEntity<>(
                    "{\"errorMessage\":\"" + e.getMessage() + "\"}",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return resEnt;
    }

    @PutMapping(value="/api/v0.2/watchLists/{watchListId}/titles/{titleId}",
                produces = MediaType.APPLICATION_JSON_VALUE,
                consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<String> updateOne(@PathVariable Long watchListId,
                                     @PathVariable Long titleId,
                                     @RequestBody Title newTitle,
                                     @RequestAttribute("username") String username){
        ResponseEntity<String> resEnt;
        try {
            Title updatedTitle = watchListService.updateTitle(watchListId,titleId,newTitle);
            resEnt = new ResponseEntity<>(updatedTitle.toString(), HttpStatus.OK);
        }
        catch (EmptyMandatoryParameterException e){
            resEnt = new ResponseEntity<>(
                    "{\"errorMessage\":\"" + e.getMessage() + "\"}",
                    HttpStatus.BAD_REQUEST);
        }
        return resEnt;
    }


}
