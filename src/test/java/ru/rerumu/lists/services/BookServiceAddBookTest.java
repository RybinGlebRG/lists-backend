package ru.rerumu.lists.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.rerumu.lists.factories.DateFactory;
import ru.rerumu.lists.model.Author;
import ru.rerumu.lists.model.Book;
import ru.rerumu.lists.model.BookStatus;
import ru.rerumu.lists.model.Series;
import ru.rerumu.lists.repository.*;
import ru.rerumu.lists.views.BookAddView;

import java.util.Date;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class BookServiceAddBookTest {

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

    @Mock
    private BookSeriesService bookSeriesService;

    @Mock
    private BookSeriesRelationService bookSeriesRelationService;

    @Mock
    private  AuthorsBooksRelationService authorsBooksRelationService;

    @Test
    void shouldAddBook() throws Exception {
        BookAddView bookAddView = new BookAddView(
                "Test", 1L, 2, 3L, 4L, 7
        );

        Author author = new Author(1L, 5L, "Author");
        Series series = new Series(3L,5L,"Series");

        Date dt = new Date();

        Book shouldBook = new Book.Builder()
                .bookId(6L)
                .title("Test")
                .insertDate(dt)
                .lastUpdateDate(dt)
                .statusId(2)
                .bookStatus(BookStatus.COMPLETED)
                .lastChapter(7)
                .readListId(5L)
                .build()
                ;


        Mockito.when(bookRepository.getNextId()).thenReturn(6L);
        Mockito.when(dateFactory.getCurrentDate()).thenReturn(dt);
        Mockito.when(authorsService.getAuthor(Mockito.anyLong(), Mockito.anyLong())).thenReturn(Optional.of(author));
        Mockito.when(bookSeriesService.getSeries(Mockito.anyLong(), Mockito.anyLong())).thenReturn(Optional.of(series));
        Mockito.when(bookRepository.getOne(Mockito.anyLong(), Mockito.anyLong())).thenReturn(shouldBook);

        ReadListService readListService = new ReadListService(
                bookRepository,
                seriesRepository,
                authorsRepository,
                authorsService,
                authorsBooksRepository,
                seriesBooksRespository,
                dateFactory,
                bookSeriesService,
                bookSeriesRelationService,
                authorsBooksRelationService
        );

        readListService.addBook(5L, bookAddView);

        InOrder inOrder = Mockito.inOrder(bookRepository, authorsBooksRepository, seriesBooksRespository);


        inOrder.verify(bookRepository).addOne(shouldBook);
        inOrder.verify(authorsBooksRepository).add(6L, 1L, 5L);
        inOrder.verify(seriesBooksRespository).add(6L, 3L, 5L, 4L);
    }

    @Test
    void shouldAddBookNoChapter() throws Exception {
        BookAddView bookAddView = new BookAddView(
                "Test", 1L, 2, 3L, 4L, null
        );

        Author author = new Author(1L, 5L, "Author");
        Series series = new Series(3L,5L,"Series");

        Date dt = new Date();

        Book shouldBook = new Book.Builder()
                .bookId(6L)
                .title("Test")
                .insertDate(dt)
                .lastUpdateDate(dt)
                .statusId(2)
                .bookStatus(BookStatus.COMPLETED)
                .readListId(5L)
                .build()
                ;


        Mockito.when(bookRepository.getNextId()).thenReturn(6L);
        Mockito.when(dateFactory.getCurrentDate()).thenReturn(dt);
        Mockito.when(authorsService.getAuthor(Mockito.anyLong(), Mockito.anyLong())).thenReturn(Optional.of(author));
        Mockito.when(bookSeriesService.getSeries(Mockito.anyLong(), Mockito.anyLong())).thenReturn(Optional.of(series));
        Mockito.when(bookRepository.getOne(Mockito.anyLong(), Mockito.anyLong())).thenReturn(shouldBook);

        ReadListService readListService = new ReadListService(
                bookRepository,
                seriesRepository,
                authorsRepository,
                authorsService,
                authorsBooksRepository,
                seriesBooksRespository,
                dateFactory,
                bookSeriesService,
                bookSeriesRelationService,
                authorsBooksRelationService
        );

        readListService.addBook(5L, bookAddView);

        InOrder inOrder = Mockito.inOrder(bookRepository, authorsBooksRepository, seriesBooksRespository);


        inOrder.verify(bookRepository).addOne(shouldBook);
        inOrder.verify(authorsBooksRepository).add(6L, 1L, 5L);
        inOrder.verify(seriesBooksRespository).add(6L, 3L, 5L, 4L);
    }

    @Test
    void shouldAddBookNoAuthor() throws Exception {
        BookAddView bookAddView = new BookAddView(
                "Test", null, 2, 3L, 4L, 7
        );

        Series series = new Series(3L,5L,"Series");

        Date dt = new Date();

        Book shouldBook = new Book.Builder()
                .bookId(6L)
                .title("Test")
                .insertDate(dt)
                .lastUpdateDate(dt)
                .statusId(2)
                .bookStatus(BookStatus.COMPLETED)
                .lastChapter(7)
                .readListId(5L)
                .build()
                ;


        Mockito.when(bookRepository.getNextId()).thenReturn(6L);
        Mockito.when(dateFactory.getCurrentDate()).thenReturn(dt);
        Mockito.when(bookSeriesService.getSeries(Mockito.anyLong(), Mockito.anyLong())).thenReturn(Optional.of(series));
        Mockito.when(bookRepository.getOne(Mockito.anyLong(), Mockito.anyLong())).thenReturn(shouldBook);

        ReadListService readListService = new ReadListService(
                bookRepository,
                seriesRepository,
                authorsRepository,
                authorsService,
                authorsBooksRepository,
                seriesBooksRespository,
                dateFactory,
                bookSeriesService,
                bookSeriesRelationService,
                authorsBooksRelationService
        );

        readListService.addBook(5L, bookAddView);

        InOrder inOrder = Mockito.inOrder(bookRepository, authorsBooksRepository, seriesBooksRespository);


        inOrder.verify(bookRepository).addOne(shouldBook);
        inOrder.verify(seriesBooksRespository).add(6L, 3L, 5L, 4L);

        Mockito.verify(authorsBooksRepository, Mockito.never()).add(Mockito.anyLong(), Mockito.anyLong(), Mockito.anyLong());
    }

    @Test
    void shouldAddBookNoSeries() throws Exception {
        BookAddView bookAddView = new BookAddView(
                "Test", 1L, 2, null, null, 7
        );

        Author author = new Author(1L, 5L, "Author");

        Date dt = new Date();

        Book shouldBook = new Book.Builder()
                .bookId(6L)
                .title("Test")
                .insertDate(dt)
                .lastUpdateDate(dt)
                .statusId(2)
                .bookStatus(BookStatus.COMPLETED)
                .lastChapter(7)
                .readListId(5L)
                .build()
                ;


        Mockito.when(bookRepository.getNextId()).thenReturn(6L);
        Mockito.when(dateFactory.getCurrentDate()).thenReturn(dt);
        Mockito.when(authorsService.getAuthor(Mockito.anyLong(), Mockito.anyLong())).thenReturn(Optional.of(author));
        Mockito.when(bookRepository.getOne(Mockito.anyLong(), Mockito.anyLong())).thenReturn(shouldBook);

        ReadListService readListService = new ReadListService(
                bookRepository,
                seriesRepository,
                authorsRepository,
                authorsService,
                authorsBooksRepository,
                seriesBooksRespository,
                dateFactory,
                bookSeriesService,
                bookSeriesRelationService,
                authorsBooksRelationService
        );

        readListService.addBook(5L, bookAddView);

        InOrder inOrder = Mockito.inOrder(bookRepository, authorsBooksRepository, seriesBooksRespository);


        inOrder.verify(bookRepository).addOne(shouldBook);
        inOrder.verify(authorsBooksRepository).add(6L, 1L, 5L);

        Mockito.verify(seriesBooksRespository, Mockito.never()).add(Mockito.anyLong(), Mockito.anyLong(), Mockito.anyLong(), Mockito.anyLong());

    }

}