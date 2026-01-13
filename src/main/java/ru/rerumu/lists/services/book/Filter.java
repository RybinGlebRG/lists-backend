package ru.rerumu.lists.services.book;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public record Filter(String field, List<String> values) {
    @JsonCreator
    public Filter(@JsonProperty("field") String field,  @JsonProperty("values") List<String> values) {
        this.field = field;
        this.values = new ArrayList<>(values);
    }
}
