package ru.rerumu.lists.controller.book.view.in;

import java.time.LocalDateTime;

public record BookAddView(
        String title,
        Long authorId,
        int status,
        Long seriesId,
        Long order,
        Integer lastChapter,
        Integer bookTypeId,
        LocalDateTime insertDate,

        String note,
        String URL
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
