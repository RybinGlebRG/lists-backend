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

    public BookAddView(
            String title,
            Long authorId,
            int status,
            Long seriesId,
            Long order,
            Integer lastChapter
    ){
        this.title = title;
        this.status = status;
        this.authorId = authorId;
        this.seriesId = seriesId;
        this.order = order;
        this.lastChapter = lastChapter;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookAddView that = (BookAddView) o;
        return status == that.status && Objects.equals(title, that.title) && Objects.equals(authorId, that.authorId) && Objects.equals(seriesId, that.seriesId) && Objects.equals(order, that.order) && Objects.equals(lastChapter, that.lastChapter);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, authorId, status, seriesId, order, lastChapter);
    }

    //    public Book getBook() throws EmptyMandatoryParameterException {
//
//        return new Book.Builder()
//                .title(title)
//                .authorId(authorId)
//                .statusId(status)
//                .seriesId(seriesId)
//                .seriesOrder(order)
//                .build();
//    }

//    public Optional<Author> getAuthor(){
//        if (authorId != null) {
//            return Optional.of(
//                    new Author.Builder()
//                            .build()
//            );
//        } else {
//            return Optional.empty();
//        }
//    }

//    public Optional<Series> getSeries(){
//        if (seriesId != null){
//            return Optional.of(
//                new Series(seriesId,null,null)
//            );
//        } else {
//            return Optional.empty();
//        }
//    }
}
