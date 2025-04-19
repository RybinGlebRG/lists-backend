package ru.rerumu.lists.services.book;

import ru.rerumu.lists.controller.book.view.in.BookUpdateView;

// TODO
public interface BookService {

    void updateBook(Long bookId, BookUpdateView bookUpdateView, Long userId) throws CloneNotSupportedException;
}
