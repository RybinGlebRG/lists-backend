package ru.rerumu.lists.model.books;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SortItem {

    private final String sortField;

    private final SearchOrder searchOrder;

    @JsonCreator
    private SortItem(@JsonProperty("field") String sortField, @JsonProperty("ordering")  SearchOrder searchOrder){
        this.sortField = sortField;
        this.searchOrder = searchOrder;
    }

    public SearchOrder getSearchOrder() {
        return searchOrder;
    }

    public String getSortField() {
        return sortField;
    }
}
