package ru.rerumu.lists.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.rerumu.lists.factories.DateFactory;
import ru.rerumu.lists.model.Author;
import ru.rerumu.lists.model.Book;
import ru.rerumu.lists.repository.*;
import ru.rerumu.lists.views.BookAddView;

import java.util.Date;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;
    @Mock
    private SeriesRepository seriesRepository;
    @Mock
    private AuthorsRepository authorsRepository;
    @Mock
    private AuthorsService authorsService;
    @Mock
    private AuthorsBooksRepository authorsBooksRepository;
    @Mock
    private SeriesBooksRespository seriesBooksRespository;

    @Mock
    private DateFactory dateFactory;

    @Test
    void shouldAddBook() throws Exception {
        BookAddView bookAddView = new BookAddView(
                "Test", 1L, 2, 3L, 4L
        );

        Date dt = new Date();

        Book.Builder builder = new Book.Builder()
                .bookId(6L)
                .title("Test")
                .insertDate(dt)
                .lastUpdateDate(dt)
                .statusId(2)
                .authorId(1L)
                .lastChapter(1)
                .readListId(5L)
                .seriesId(3L)
                .seriesOrder(4L);

        Book shouldBook = builder.build();



        Mockito.when(bookRepository.getNextId()).thenReturn(6L);
        Mockito.when(dateFactory.getCurrentDate()).thenReturn(dt);

        ReadListService readListService = new ReadListService(
                bookRepository,
                seriesRepository,
                authorsRepository,
                authorsService,
                authorsBooksRepository,
                seriesBooksRespository,
                dateFactory
        );

        Book book = readListService.addBook(5L, bookAddView);

        InOrder inOrder = Mockito.inOrder(bookRepository, authorsBooksRepository, seriesBooksRespository);


        inOrder.verify(bookRepository).addOne(shouldBook);
        inOrder.verify(authorsBooksRepository).add(6L, 1L, 5L);
        inOrder.verify(seriesBooksRespository).add(6L, 3L, 5L, 4L);

//        Assertions.assertEquals(book.getBookId(),shouldBook.getBookId());
//        Assertions.assertEquals(book.getTitle(),shouldBook.getTitle());
//        Assertions.assertEquals(book.getStatusId(),shouldBook.getStatusId());
//        Assertions.assertEquals(book.getLastChapter(),shouldBook.getLastChapter());
//        Assertions.assertEquals(book.getReadListId(),shouldBook.getReadListId());

    }

}