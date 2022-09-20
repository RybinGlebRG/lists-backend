package ru.rerumu.lists.model;

import ru.rerumu.lists.exception.EmptyMandatoryParameterException;

public final class AuthorBookRelation {

    private final Book book;
    private final Author author;

    public AuthorBookRelation(Book book, Author author) throws EmptyMandatoryParameterException {
        this.book = new Book.Builder(book).build();
        this.author = author;
    }

    // TODO: Make immutable
    public Author getAuthor() {
        return author;
    }

    public Book getBook() throws EmptyMandatoryParameterException {
        return new Book.Builder(book).build();
    }
}
