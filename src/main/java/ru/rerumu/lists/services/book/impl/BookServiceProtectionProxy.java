package ru.rerumu.lists.services.book.impl;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;
import org.springframework.web.context.request.RequestContextHolder;
import ru.rerumu.lists.controller.book.view.in.BookAddView;
import ru.rerumu.lists.controller.book.view.in.BookUpdateView;
import ru.rerumu.lists.crosscut.exception.EmptyMandatoryParameterException;
import ru.rerumu.lists.crosscut.exception.EntityNotFoundException;
import ru.rerumu.lists.crosscut.exception.UserPermissionException;
import ru.rerumu.lists.dao.user.UsersRepository;
import ru.rerumu.lists.domain.book.Book;
import ru.rerumu.lists.domain.user.User;
import ru.rerumu.lists.services.AuthUserParser;
import ru.rerumu.lists.services.book.BookService;
import ru.rerumu.lists.services.book.Search;
import ru.rerumu.lists.services.user.UserService;

import java.util.List;

@Slf4j
@Service("BookServiceProtectionProxy")
@Primary
@RequestScope
public class BookServiceProtectionProxy implements BookService {

    private final BookService bookService;
    private final User authUser;
    private final UsersRepository usersRepository;

    @Autowired
    public BookServiceProtectionProxy(
            @Qualifier("BookServiceImpl") BookService bookService,
            UsersRepository usersRepository,
            UserService userService
    ) {
        this.bookService = bookService;
        this.usersRepository = usersRepository;

        Long authUserId = AuthUserParser.getAuthUser(RequestContextHolder.currentRequestAttributes());
        authUser = userService.findById(authUserId);
        log.info(String.format("GOT USER %d", authUser.getId()));
    }

    @Override
    @NonNull
    public Book addBook(@NonNull BookAddView bookAddView, @NonNull Long userId) throws EmptyMandatoryParameterException, EntityNotFoundException {
        // Get passed user
        User user = usersRepository.findById(userId);
        log.debug("user: {}", user);


        // Check if actual user has access
        log.debug("authUser: {}", authUser);
        if (!user.equals(authUser)) {
            throw new UserPermissionException();
        }
        
        return bookService.addBook(bookAddView, userId);
    }

    @Override
    public Book getBook(@NonNull Long bookId, @NonNull Long userId) {
        // Get passed user
        User user = usersRepository.findById(userId);
        
        // Check if actual user has access
        if (!user.equals(authUser)) {
            throw new UserPermissionException();
        }
        
        return bookService.getBook(bookId, userId);
    }

    @Override
    public List<Book> getAllBooks(Search search, Long userId) {
        // Get passed user
        User user = usersRepository.findById(userId);

        // Check if actual user has access
        if (!user.equals(authUser)) {
            throw new UserPermissionException();
        }
        
        return bookService.getAllBooks(search, userId);
    }

    @Override
    public Book updateBook(@NonNull Long bookId, @NonNull Long userId, @NonNull BookUpdateView bookUpdateView) {
        // Get passed user
        User user = usersRepository.findById(userId);

        // Check if actual user has access
        if (!user.equals(authUser)) {
            throw new UserPermissionException();
        }

        return bookService.updateBook(bookId, userId, bookUpdateView);
    }

    @Override
    public void deleteBook(@NonNull Long bookId, @NonNull Long userId) throws EntityNotFoundException, EmptyMandatoryParameterException {
        // Get passed user
        User user = usersRepository.findById(userId);

        // Check if actual user has access
        if (!user.equals(authUser)) {
            throw new UserPermissionException();
        }
        
        bookService.deleteBook(bookId, userId);
    }
}
