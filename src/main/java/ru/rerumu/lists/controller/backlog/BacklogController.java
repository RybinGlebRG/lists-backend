package ru.rerumu.lists.controller.backlog;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
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
import ru.rerumu.lists.controller.backlog.view.in.BacklogItemCreateView;
import ru.rerumu.lists.controller.backlog.view.in.BacklogItemUpdateView;
import ru.rerumu.lists.controller.backlog.view.out.BacklogItemOutView;
import ru.rerumu.lists.controller.backlog.view.out.BacklogOutView;
import ru.rerumu.lists.controller.backlog.view.out.BacklogViewFactory;
import ru.rerumu.lists.crosscut.exception.ServerException;
import ru.rerumu.lists.domain.backlog.BacklogItem;
import ru.rerumu.lists.services.backlog.BacklogService;

import java.util.List;

@CrossOrigin
@RestController
@Slf4j
public class BacklogController {

    private final BacklogService backlogService;
    private final ObjectMapper objectMapper;
    private final BacklogViewFactory backlogViewFactory;

    @Autowired
    public BacklogController(
            BacklogService backlogService,
            ObjectMapper objectMapper,
            BacklogViewFactory backlogViewFactory
    ) {
        this.backlogService = backlogService;
        this.objectMapper = objectMapper;
        this.backlogViewFactory = backlogViewFactory;
    }

    /**
     * Add item to backlog
     */
    @PostMapping(
            value = "/api/v1/users/{userId}/backlogItems",
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<String> addItemToBacklog(
            @PathVariable Long userId,
            @RequestBody BacklogItemCreateView backlogItemCreateView
    ) {
        try {
            BacklogItem backlogItem = backlogService.addItemToBacklog(userId, backlogItemCreateView);

            BacklogItemOutView backlogItemOutView = backlogViewFactory.build(backlogItem);
            String result = objectMapper.writeValueAsString(backlogItemOutView);

            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (JsonProcessingException e) {
            throw new ServerException(e.getMessage(), e);
        }
    }

    /**
     * Get backlog
     */
    @GetMapping(
            value = "/api/v1/users/{userId}/backlogItems",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<String> getBacklog(
            @PathVariable Long userId
    ){
        try {
            List<BacklogItem> backlogItems = backlogService.getBacklog(userId);
            BacklogOutView backlogOutView = backlogViewFactory.build(backlogItems);
            String result = objectMapper.writeValueAsString(backlogOutView);

            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (JsonProcessingException e) {
            throw new ServerException(e.getMessage(), e);
        }
    }

    /**
     * Update backlog item
     */
    @PutMapping(
            value = "/api/v1/users/{userId}/backlogItems/{backlogItemId}",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<String> updateBacklogItem(
            @PathVariable Long userId,
            @PathVariable Long backlogItemId,
            @RequestBody BacklogItemUpdateView backlogItemUpdateView
    ) {
        try {
            BacklogItem backlogItem = backlogService.updateBacklogItem(userId, backlogItemId, backlogItemUpdateView);

            BacklogItemOutView backlogItemOutView = backlogViewFactory.build(backlogItem);
            String result = objectMapper.writeValueAsString(backlogItemOutView);

            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (JsonProcessingException e) {
            throw new ServerException(e.getMessage(), e);
        }
    }

    /**
     * Delete backlog item
     */
    @DeleteMapping(
            value = "/api/v1/users/{userId}/backlogItems/{backlogItemId}"
    )
    ResponseEntity<String> deleteBacklogItem(
            @PathVariable Long userId,
            @PathVariable Long backlogItemId
    ){
        backlogService.deleteBacklogItem(userId, backlogItemId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
