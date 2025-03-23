package ru.rerumu.lists.services.book.impl;

import ru.rerumu.lists.exception.UserPermissionException;
import ru.rerumu.lists.model.user.User;
import ru.rerumu.lists.model.user.UserFactory;
import ru.rerumu.lists.services.book.BookService;
import ru.rerumu.lists.services.book.ReadListService;
import ru.rerumu.lists.controller.book.view.in.BookUpdateView;

public class BookServiceProtectionProxy implements BookService {

    private final ReadListService readListService;
    private final User authUser;
    private final UserFactory userFactory;

    public BookServiceProtectionProxy(ReadListService readListService, User authUser, UserFactory userFactory) {
        this.readListService = readListService;
        this.authUser = authUser;
        this.userFactory = userFactory;
    }

    public void updateBook(Long bookId, BookUpdateView bookUpdateView, Long userId) throws CloneNotSupportedException {
        User user = userFactory.findById(userId);

        if (user.equals(authUser)) {
            throw new UserPermissionException();
        }

        readListService.updateBook(bookId, bookUpdateView);
    }
}
