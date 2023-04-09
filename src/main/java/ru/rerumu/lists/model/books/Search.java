package ru.rerumu.lists.model.books;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Search {

    private final List<SortItem> sortItemList;
    private final Boolean isChainBySeries;

    @JsonCreator
    public Search(
            @JsonProperty("sort") List<SortItem> sortItemList,
            @JsonProperty("isChainBySeries") Boolean isChainBySeries
    ){
        this.sortItemList = sortItemList;
        this.isChainBySeries = isChainBySeries;
    }

    public List<SortItem> getSortItemList() {
        return sortItemList;
    }

    public Boolean getChainBySeries() {
        return isChainBySeries;
    }
}
