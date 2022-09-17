package ru.rerumu.lists.views;

import ru.rerumu.lists.model.Author;
import ru.rerumu.lists.model.Book;
import ru.rerumu.lists.model.Series;

import java.util.Optional;

public class AddBookView {
    private final String title;
    private final Long authorId;
    private final int status;
    private final Long seriesId;
    private final Long order;

    public AddBookView(
            String title,
            Long authorId,
            int status,
            Long seriesId,
            Long order
    ){
        this.title = title;
        this.status = status;
        this.authorId = authorId;
        this.seriesId = seriesId;
        this.order = order;
    }

    public Book getBook(){

        return new Book.Builder()
                .title(title)
                .authorId(authorId)
                .statusId(status)
                .seriesId(seriesId)
                .seriesOrder(order)
                .build();
    }

    public Optional<Author> getAuthor(){
        if (authorId != null) {
            return Optional.of(
                    new Author.Builder()
                            .build()
            );
        } else {
            return Optional.empty();
        }
    }

    public Optional<Series> getSeries(){
        if (seriesId != null){
            return Optional.of(
                new Series(seriesId,null,null)
            );
        } else {
            return Optional.empty();
        }
    }
}
