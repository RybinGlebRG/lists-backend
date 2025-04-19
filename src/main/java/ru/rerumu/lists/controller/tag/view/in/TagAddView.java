package ru.rerumu.lists.controller.tag.view.in;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NonNull;

public class TagAddView {

    @Getter
    private final String name;

    @JsonCreator
    public TagAddView(
            @NonNull @JsonProperty("name") String name
    ) {
        this.name = name;
    }
}
