package ru.rerumu.lists.model.books;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Search {

    private final List<SortItem> sortItemList;

    @JsonCreator
    public Search(@JsonProperty("sort") List<SortItem> sortItemList){
        this.sortItemList = sortItemList;
    }

    public List<SortItem> getSortItemList() {
        return sortItemList;
    }
}
