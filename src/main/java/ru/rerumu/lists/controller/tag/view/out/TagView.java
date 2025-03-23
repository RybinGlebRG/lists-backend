package ru.rerumu.lists.controller.tag.view.out;

import lombok.Getter;

@Getter
public class TagView {

    private final Long tagId;
    private final String name;

    public TagView(Long tagId, String name) {
        this.tagId = tagId;
        this.name = name;
    }
}
