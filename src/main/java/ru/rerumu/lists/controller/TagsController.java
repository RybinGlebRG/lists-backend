package ru.rerumu.lists.controller;

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
import ru.rerumu.lists.controller.views.TagAddView;
import ru.rerumu.lists.model.tag.Tag;
import ru.rerumu.lists.services.tag.TagService;
import ru.rerumu.lists.views.tag.TagListView;

import java.util.List;

@RestController
@Slf4j
public class TagsController {

    private final TagService tagService;

    @Autowired
    public TagsController(TagService tagService) {
        this.tagService = tagService;
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
        List<Tag> tags = tagService.getAll(userId);
        TagListView tagListView = new TagListView(tags);

        return new ResponseEntity<>(tagListView.toString(), HttpStatus.OK);
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
