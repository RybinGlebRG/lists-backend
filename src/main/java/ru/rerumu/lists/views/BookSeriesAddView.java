package ru.rerumu.lists.views;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class BookSeriesAddView {

    private final String title;

    public BookSeriesAddView(@JsonProperty("title") String title){
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookSeriesAddView that = (BookSeriesAddView) o;
        return Objects.equals(title, that.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title);
    }
}
