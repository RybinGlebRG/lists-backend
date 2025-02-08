package ru.rerumu.lists.views;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

public record BookUpdateView(
        Long readListId,
        String title,
        Long authorId,
        int status,
        Long seriesId,
        Long order,
        Integer lastChapter,
        LocalDateTime insertDateUTC,
        Integer bookTypeId,
        String note,
        String URL
) {
//    private final Long readListId;
//    private final String title;
//    private final Long authorId;
//    private final int status;
////    private final Long seriesId;
//    private final Long order;
//    private final Integer lastChapter;
//    private final LocalDateTime insertDateUTC;
//
//    private final Integer bookTypeId;

//    public BookUpdateView(
//            Long readListId,
//            String title,
//            Long authorId,
//            int status,
//            Long seriesId,
//            Long order,
//            Integer lastChapter,
//            LocalDateTime insertDateUTC,
//            Integer bookTypeId
//    ){
//        this.readListId = readListId;
//        this.title = title;
//        this.status = status;
//        this.authorId = authorId;
////        this.seriesId = seriesId;
//        this.order = order;
//        this.lastChapter = lastChapter;
//        this.insertDateUTC = insertDateUTC;
//        this.bookTypeId = bookTypeId;
//    }

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

//    public Long getSeriesId() {
//        return seriesId;
//    }

    public String getTitle() {
        return title;
    }

    public Optional<Integer> getLastChapter() {
        return Optional.ofNullable(lastChapter);
    }

    public LocalDateTime getInsertDateUTC() {
        return insertDateUTC;
    }

    public Integer getBookTypeId() {
        return bookTypeId;
    }

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        BookUpdateView that = (BookUpdateView) o;
//        return status == that.status && Objects.equals(readListId, that.readListId) && Objects.equals(title, that.title) && Objects.equals(authorId, that.authorId) && Objects.equals(order, that.order) && Objects.equals(lastChapter, that.lastChapter) && Objects.equals(insertDateUTC, that.insertDateUTC) && Objects.equals(bookTypeId, that.bookTypeId);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(readListId, title, authorId, status, order, lastChapter, insertDateUTC, bookTypeId);
//    }
}
