package ru.rerumu.lists.views;

import ru.rerumu.lists.exception.EmptyMandatoryParameterException;
import ru.rerumu.lists.model.Author;
import ru.rerumu.lists.model.Book;
import ru.rerumu.lists.model.BookStatus;
import ru.rerumu.lists.model.Series;

import java.util.Objects;
import java.util.Optional;

public class BookAddView {
    private final String title;
    private final Long authorId;
    private final int status;
    private final Long seriesId;
    private final Long order;
    private final Integer lastChapter;

    private final Integer bookTypeId;

    public BookAddView(
            String title,
            Long authorId,
            int status,
            Long seriesId,
            Long order,
            Integer lastChapter,
            Integer bookTypeId
    ){
        this.title = title;
        this.status = status;
        this.authorId = authorId;
        this.seriesId = seriesId;
        this.order = order;
        this.lastChapter = lastChapter;
        this.bookTypeId = bookTypeId;
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookAddView that = (BookAddView) o;
        return status == that.status && Objects.equals(title, that.title) && Objects.equals(authorId, that.authorId) && Objects.equals(seriesId, that.seriesId) && Objects.equals(order, that.order) && Objects.equals(lastChapter, that.lastChapter) && Objects.equals(bookTypeId, that.bookTypeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, authorId, status, seriesId, order, lastChapter, bookTypeId);
    }
}
