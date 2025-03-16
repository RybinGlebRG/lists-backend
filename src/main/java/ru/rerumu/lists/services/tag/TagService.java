package ru.rerumu.lists.services.tag;

import ru.rerumu.lists.controller.views.TagAddView;
import ru.rerumu.lists.model.tag.Tag;

import java.util.List;

public interface TagService {

    void addOne(TagAddView tagAddView, Long userId);
    void deleteOne(Long tagId, Long userId);
    List<Tag> getAll(Long userId);
}
