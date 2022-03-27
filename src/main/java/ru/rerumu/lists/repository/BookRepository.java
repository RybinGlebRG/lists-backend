package ru.rerumu.lists.repository;

import ru.rerumu.lists.model.Book;

import java.util.List;

public interface BookRepository {

    Book update(Book book);

    Book getOne(Long readListId, Long bookId);
    List<Book> getAll(Long readListId);
}
