package ru.rerumu.lists.services.book;

import lombok.NonNull;
import ru.rerumu.lists.controller.book.view.in.BookAddView;
import ru.rerumu.lists.controller.book.view.in.BookUpdateView;
import ru.rerumu.lists.crosscut.exception.EmptyMandatoryParameterException;
import ru.rerumu.lists.crosscut.exception.EntityNotFoundException;
import ru.rerumu.lists.model.book.Book;
import ru.rerumu.lists.model.books.Search;

import java.util.List;

public interface BookService {

    /**
     * Create book for user
     */
    void addBook(@NonNull BookAddView bookAddView, @NonNull Long userId) throws EmptyMandatoryParameterException, EntityNotFoundException;

    /**
     * Get book of user
     */
    Book getBook(@NonNull Long bookId, @NonNull Long userId);

    /**
     * Get all books of user
     */
    List<Book> getAllBooks(Search search, Long userId);

    /**
     * Update book of user
     */
    void updateBook(@NonNull Long bookId, @NonNull Long userId, @NonNull BookUpdateView bookUpdateView);

    /**
     * Delete book of user
     */
    void deleteBook(@NonNull Long bookId, @NonNull Long userId) throws EntityNotFoundException, EmptyMandatoryParameterException;
}
