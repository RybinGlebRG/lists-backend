package ru.rerumu.lists.model;

import ru.rerumu.lists.model.book.Book;

public final class AuthorBookRelation {

    private final Book book;
    private final Author author;

    public AuthorBookRelation(Book book, Author author)  {
        this.book = book;
        this.author = author.clone();
    }

    public Author getAuthor() {
        return author.clone();
    }

    public Book getBook() {
        return book;
    }
}
