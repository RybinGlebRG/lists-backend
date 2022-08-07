package ru.rerumu.lists.views;

import ru.rerumu.lists.model.Book;

public class AddBookView {
    private String title;
    private String author;
    private int status;
    private String series;
    private Long order;

    public AddBookView(
            String title,
            String author,
            int status,
            String series,
            Long order
    ){

    }

    public Book getBook(){
        Book book = new Book(
                null,
                null,
                title,
                status,
                null,
                null,
                null,
                null,
                null,
                order
        );
        return book;
    }
}
