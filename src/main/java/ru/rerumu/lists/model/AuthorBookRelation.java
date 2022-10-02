package ru.rerumu.lists.model;

import ru.rerumu.lists.exception.EmptyMandatoryParameterException;

public final class AuthorBookRelation {

    private final Book book;
    private final Author author;

    public AuthorBookRelation(Book book, Author author)  {
        this.book = book.clone();
        this.author = author.clone();
    }

    public Author getAuthor() {
        return author.clone();
    }

    public Book getBook() {
        return book.clone();
    }
}
