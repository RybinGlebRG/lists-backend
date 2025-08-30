package ru.rerumu.lists.services.book.impl;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import ru.rerumu.lists.controller.book.view.in.BookAddView;
import ru.rerumu.lists.controller.book.view.in.BookUpdateView;
import ru.rerumu.lists.crosscut.exception.EmptyMandatoryParameterException;
import ru.rerumu.lists.crosscut.exception.EntityNotFoundException;
import ru.rerumu.lists.crosscut.exception.UserPermissionException;
import ru.rerumu.lists.domain.book.Book;
import ru.rerumu.lists.domain.books.Search;
import ru.rerumu.lists.domain.user.User;
import ru.rerumu.lists.domain.user.UserFactory;
import ru.rerumu.lists.services.book.BookService;

import java.util.List;

@Slf4j
public class BookServiceProtectionProxy implements BookService {

    private final ReadListService readListService;
    private final User authUser;
    private final UserFactory userFactory;

    public BookServiceProtectionProxy(ReadListService readListService, User authUser, UserFactory userFactory) {
        this.readListService = readListService;
        this.authUser = authUser;
        this.userFactory = userFactory;
    }

    @Override
    @NonNull
    public Book addBook(@NonNull BookAddView bookAddView, @NonNull Long userId) throws EmptyMandatoryParameterException, EntityNotFoundException {
        // Get passed user
        User user = userFactory.findById(userId);
        log.debug("user: {}", user);


        // Check if actual user has access
        log.debug("authUser: {}", authUser);
        if (!user.equals(authUser)) {
            throw new UserPermissionException();
        }
        
        return readListService.addBook(bookAddView, userId);
    }

    @Override
    public Book getBook(@NonNull Long bookId, @NonNull Long userId) {
        // Get passed user
        User user = userFactory.findById(userId);
        
        // Check if actual user has access
        if (!user.equals(authUser)) {
            throw new UserPermissionException();
        }
        
        return readListService.getBook(bookId, userId);
    }

    @Override
    public List<Book> getAllBooks(Search search, Long userId) {
        // Get passed user
        User user = userFactory.findById(userId);

        // Check if actual user has access
        if (!user.equals(authUser)) {
            throw new UserPermissionException();
        }
        
        return readListService.getAllBooks(search, userId);
    }

    @Override
    public Book updateBook(@NonNull Long bookId, @NonNull Long userId, @NonNull BookUpdateView bookUpdateView) {
        // Get passed user
        User user = userFactory.findById(userId);

        // Check if actual user has access
        if (!user.equals(authUser)) {
            throw new UserPermissionException();
        }

        return readListService.updateBook(bookId, userId, bookUpdateView);
    }

    @Override
    public void deleteBook(@NonNull Long bookId, @NonNull Long userId) throws EntityNotFoundException, EmptyMandatoryParameterException {
        // Get passed user
        User user = userFactory.findById(userId);

        // Check if actual user has access
        if (!user.equals(authUser)) {
            throw new UserPermissionException();
        }
        
        readListService.deleteBook(bookId, userId);
    }
}
