package ru.rerumu.lists.domain.books;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public record Search(List<SortItem> sortItemList, List<Filter> filters, Boolean isChainBySeries) {

    @JsonCreator
    public Search(
            @JsonProperty("sort") List<SortItem> sortItemList,
            @JsonProperty("filters")  List<Filter> filters,
            @JsonProperty("isChainBySeries") Boolean isChainBySeries
    ){
        this.sortItemList = sortItemList != null ? new ArrayList<>(sortItemList) : null;
        this.isChainBySeries = isChainBySeries != null ? isChainBySeries : false;
        this.filters = filters != null ? new ArrayList<>(filters) : new ArrayList<>();
    }

    public List<SortItem> getSortItemList() {
        return sortItemList;
    }

    public Boolean getChainBySeries() {
        return isChainBySeries;
    }
}
