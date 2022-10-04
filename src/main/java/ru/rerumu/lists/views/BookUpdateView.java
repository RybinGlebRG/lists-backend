package ru.rerumu.lists.views;

import java.time.LocalDateTime;
import java.util.Objects;

public class BookUpdateView {
    private final Long readListId;
    private final String title;
    private final Long authorId;
    private final int status;
    private final Long seriesId;
    private final Long order;
    private final Integer lastChapter;
    private final LocalDateTime insertDateUTC;

    public BookUpdateView(
            Long readListId,
            String title,
            Long authorId,
            int status,
            Long seriesId,
            Long order,
            Integer lastChapter,
            LocalDateTime insertDateUTC
    ){
        this.readListId = readListId;
        this.title = title;
        this.status = status;
        this.authorId = authorId;
        this.seriesId = seriesId;
        this.order = order;
        this.lastChapter = lastChapter;
        this.insertDateUTC = insertDateUTC;
    }

    public Long getReadListId() {
        return readListId;
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

    public LocalDateTime getInsertDateUTC() {
        return insertDateUTC;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookUpdateView that = (BookUpdateView) o;
        return status == that.status && Objects.equals(readListId, that.readListId) && Objects.equals(title, that.title) && Objects.equals(authorId, that.authorId) && Objects.equals(seriesId, that.seriesId) && Objects.equals(order, that.order);
    }

    @Override
    public int hashCode() {
        return Objects.hash(readListId, title, authorId, status, seriesId, order);
    }
}
