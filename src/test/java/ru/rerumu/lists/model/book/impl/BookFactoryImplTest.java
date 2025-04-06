package ru.rerumu.lists.model.book.impl;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.rerumu.lists.crosscut.utils.DateFactory;
import ru.rerumu.lists.dao.book.BookDtoDao;
import ru.rerumu.lists.dao.book.BookRepository;
import ru.rerumu.lists.dao.user.UserDtoDao;
import ru.rerumu.lists.model.book.Book;
import ru.rerumu.lists.model.book.BookFactory;
import ru.rerumu.lists.model.book.readingrecords.impl.ReadingRecordFactory;
import ru.rerumu.lists.model.book.readingrecords.status.BookStatusRecord;
import ru.rerumu.lists.model.book.readingrecords.status.StatusFactory;
import ru.rerumu.lists.model.book.type.BookTypeDTO;
import ru.rerumu.lists.model.book.type.BookTypeFactory;
import ru.rerumu.lists.model.tag.TagFactory;
import ru.rerumu.lists.model.user.User;
import ru.rerumu.lists.model.user.UserFactory;

import java.util.ArrayList;
import java.util.Date;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class BookFactoryImplTest {

    private BookFactory bookFactory;


    @Mock
    private DateFactory dateFactory;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private ReadingRecordFactory readingRecordFactory;

    @Mock
    private BookTypeFactory bookTypeFactory;

    @Mock
    private UserFactory userFactory;

    @Mock
    private TagFactory tagFactory;

    @Mock
    private StatusFactory statusFactory;


    @BeforeEach
    public void beforeEach() {
        bookFactory = new BookFactoryImpl(
                dateFactory,
                bookRepository,
                readingRecordFactory,
                bookTypeFactory,
                userFactory,
                tagFactory,
                statusFactory
        );
    }

    @Test
    public void shouldGetBook(TestInfo testInfo) {
        log.info("Test: {}", testInfo.getDisplayName());

        /*
        Given
         */
        Long bookId = 1L;

        Date insertDate = new Date();
        Date lastUpdateDate = new Date();

        UserDtoDao userDtoDao = new UserDtoDao(1L, "Test user", "Test password");

        BookDtoDao bookDtoDao = new BookDtoDao(
                1L,
                2L,
                "Test book",
                1,
                insertDate,
                lastUpdateDate,
                123,
                1,
                "Test note",
                new BookTypeDTO(1, "Test type"),
                new BookStatusRecord(1, "Test status"),
                new ArrayList<>(),
                new ArrayList<>(),
                "Test URL",
                1L,
                new ArrayList<>(),
                userDtoDao
        );

        User user = mock(User.class);

        when(bookRepository.findById(bookId))
                .thenReturn(bookDtoDao);

        when(userFactory.fromDTO(userDtoDao))
                .thenReturn(user);


        /*
        When
         */
        Book book = bookFactory.getBook(bookId);


        /*
        Then
         */
        Assertions.assertInstanceOf(BookImpl.class, book, "Incorrect class");
    }
}
