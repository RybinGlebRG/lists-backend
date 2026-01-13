package ru.rerumu.lists.controller.tag;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.rerumu.lists.controller.tag.view.in.TagAddView;
import ru.rerumu.lists.controller.tag.view.out.TagListView;
import ru.rerumu.lists.controller.tag.view.out.TagViewFactory;
import ru.rerumu.lists.crosscut.exception.ServerException;
import ru.rerumu.lists.domain.tag.Tag;
import ru.rerumu.lists.services.tag.TagService;

import java.util.List;

@RestController
@Slf4j
public class TagsController {

    private final TagService tagService;
    private final TagViewFactory tagViewFactory;
    private final ObjectMapper objectMapper;

    @Autowired
    public TagsController(TagService tagService, TagViewFactory tagViewFactory, ObjectMapper objectMapper) {
        this.tagService = tagService;
        this.tagViewFactory = tagViewFactory;
        this.objectMapper = objectMapper;
    }

    @PostMapping(
            value = "/api/v1/users/{userId}/tags",
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<String> addOne(
            @PathVariable Long userId,
            @RequestBody TagAddView tagAddView
    ) {
        tagService.addOne(tagAddView, userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping(
            value = "/api/v1/users/{userId}/tags",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<String> getAll(
            @PathVariable Long userId
    ) {
        try {
            List<Tag> tags = tagService.getAll(userId);
            TagListView tagListView = tagViewFactory.buildTagListView(tags);
            return new ResponseEntity<>(objectMapper.writeValueAsString(tagListView), HttpStatus.OK);
        } catch (JsonProcessingException e) {
            throw new ServerException(e.getMessage(), e);
        }
    }

    @DeleteMapping(
            value = "/api/v1/users/{userId}/tags/{tagId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<String> deleteOne(
            @PathVariable Long userId,
            @PathVariable Long tagId
    ) {
        tagService.deleteOne(tagId, userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
