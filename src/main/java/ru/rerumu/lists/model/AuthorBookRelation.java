package ru.rerumu.lists.model;

import ru.rerumu.lists.model.author.Author;
import ru.rerumu.lists.model.book.Book;

public final class AuthorBookRelation {

    private final Book book;
    private final Author author;

    public AuthorBookRelation(Book book, Author author)  {
        this.book = book;
        this.author = author;
    }

    public Author getAuthor() {
        return author;
    }

    public Book getBook() {
        return book;
    }
}
