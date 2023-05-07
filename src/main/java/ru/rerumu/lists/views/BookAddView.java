package ru.rerumu.lists.views;

import ru.rerumu.lists.exception.EmptyMandatoryParameterException;
import ru.rerumu.lists.model.Author;
import ru.rerumu.lists.model.Book;
import ru.rerumu.lists.model.BookStatus;
import ru.rerumu.lists.model.Series;

import java.util.Objects;
import java.util.Optional;

public record BookAddView(
        String title,
        Long authorId,
        int status,
        Long seriesId,
        Long order,
        Integer lastChapter,
        Integer bookTypeId
) {

    public int getStatus() {
        return status;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public Long getOrder() {
        return order;
    }

    public Long getSeriesId() {
        return seriesId;
    }

    public String getTitle() {
        return title;
    }

    public Integer getLastChapter() {
        return lastChapter;
    }

    public Integer getBookTypeId() {
        return bookTypeId;
    }
}
