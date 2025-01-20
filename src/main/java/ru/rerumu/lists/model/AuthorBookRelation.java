package ru.rerumu.lists.model;

import ru.rerumu.lists.model.book.BookImpl;

public final class AuthorBookRelation {

    private final BookImpl book;
    private final Author author;

    public AuthorBookRelation(BookImpl book, Author author)  {
        this.book = book.clone();
        this.author = author.clone();
    }

    public Author getAuthor() {
        return author.clone();
    }

    public BookImpl getBook() {
        return book.clone();
    }
}
