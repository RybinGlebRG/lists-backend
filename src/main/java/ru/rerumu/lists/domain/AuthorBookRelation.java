package ru.rerumu.lists.domain;

import ru.rerumu.lists.domain.author.Author;
import ru.rerumu.lists.domain.book.Book;

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
