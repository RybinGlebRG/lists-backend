package ru.rerumu.lists.controller.tag.view.out;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

import java.util.List;

@EqualsAndHashCode
@ToString
public class TagListView {

    @Getter
    private final List<TagView> items;

    public TagListView(@NonNull List<TagView> items) {
        this.items = items;
    }
}
