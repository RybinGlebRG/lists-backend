package ru.rerumu.lists.domain.book.impl;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.rerumu.lists.crosscut.utils.DateFactory;
import ru.rerumu.lists.dao.book.AuthorsBooksRepository;
import ru.rerumu.lists.dao.book.BookRepository;
import ru.rerumu.lists.domain.book.BookFactory;
import ru.rerumu.lists.domain.readingrecords.impl.ReadingRecordFactory;
import ru.rerumu.lists.domain.user.UserFactory;

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
    private UserFactory userFactory;

    @Mock
    private AuthorsBooksRepository authorsBooksRepository;


//    @BeforeEach
//    public void beforeEach() {
//        bookFactory = new BookFactoryImpl(
//                dateFactory,
//                bookRepository,
//                readingRecordFactory,
//                bookTypeFactory,
//                userFactory,
//                tagFactory,
//                statusFactory,
//                authorsBooksRepository,
//                authorFactory
//        );
//    }

//    @Test
//    public void shouldGetBook(TestInfo testInfo) {
//        log.info("Test: {}", testInfo.getDisplayName());
//
//        /*
//        Given
//         */
//        Long bookId = 1L;
//
//        Date insertDate = new Date();
//        Date lastUpdateDate = new Date();
//
//        UserDtoDao userDtoDao = new UserDtoDao(1L, "Test user", "Test password");
//
//        BookDtoDao bookDtoDao = new BookDtoDao(
//                1L,
//                2L,
//                "Test book",
//                1,
//                insertDate,
//                lastUpdateDate,
//                123,
//                1,
//                "Test note",
//                new BookTypeDTO(1, "Test type"),
//                new BookStatusRecord(1, "Test status"),
//                new ArrayList<>(),
//                new ArrayList<>(),
//                "Test URL",
//                1L,
//                new ArrayList<>(),
//                userDtoDao,
//                new ArrayList<>()
//        );
//
//        User user = mock(User.class);
//
//        when(bookRepository.findByIdAndUser(bookId))
//                .thenReturn(bookDtoDao);
//
//        when(userFactory.fromDTO(userDtoDao))
//                .thenReturn(user);
//
//
//        /*
//        When
//         */
//        Book book = bookFactory.getBook(bookId);
//
//
//        /*
//        Then
//         */
//        Assertions.assertInstanceOf(BookImpl.class, book, "Incorrect class");
//    }
}
