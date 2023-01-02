package ru.rerumu.lists.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.rerumu.lists.factories.DateFactory;
import ru.rerumu.lists.model.*;
import ru.rerumu.lists.repository.*;
import ru.rerumu.lists.views.BookAddView;

import java.util.Date;
import java.util.Optional;

import static org.mockito.Mockito.verify;

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
    private SeriesService seriesService;

    @Mock
    private BookSeriesRelationService bookSeriesRelationService;

    @Mock
    private  AuthorsBooksRelationService authorsBooksRelationService;

    @Mock
    private BookTypesService bookTypesService;

    @Test
    void shouldAddBook() throws Exception {
        BookAddView bookAddView = new BookAddView(
                "Test", 1L, 2, 3L, 4L, 7, null
        );

        Author author = new Author(1L, 5L, "Author");
        Series series = new Series(3L,5L,"Series");

        Date dt = new Date();

        Book shouldBook = new Book.Builder()
                .bookId(6L)
                .title("Test")
                .insertDate(dt)
                .lastUpdateDate(dt)
                .bookStatus(BookStatus.COMPLETED)
                .lastChapter(7)
                .readListId(5L)
                .build()
                ;


        Mockito.when(bookRepository.getNextId()).thenReturn(6L);
        Mockito.when(dateFactory.getCurrentDate()).thenReturn(dt);
        Mockito.when(authorsService.getAuthor(Mockito.anyLong(), Mockito.anyLong())).thenReturn(Optional.of(author));
        Mockito.when(seriesService.getSeries(Mockito.anyLong(), Mockito.anyLong())).thenReturn(Optional.of(series));
        Mockito.when(bookRepository.getOne(Mockito.anyLong(), Mockito.anyLong())).thenReturn(shouldBook);

        ReadListService readListService = new ReadListService(
                bookRepository,
                seriesRepository,
                authorsRepository,
                authorsService,
                authorsBooksRepository,
                seriesBooksRespository,
                dateFactory,
                seriesService,
                bookSeriesRelationService,
                authorsBooksRelationService,
                bookTypesService
        );

        readListService.addBook(5L, bookAddView);

        InOrder inOrder = Mockito.inOrder(bookRepository, authorsBooksRepository, seriesBooksRespository);


        inOrder.verify(bookRepository).addOne(shouldBook);
        inOrder.verify(authorsBooksRepository).add(6L, 1L, 5L);
        inOrder.verify(seriesBooksRespository).add(6L, 3L, 5L, 4L);
    }

    @Test
    void shouldAddBookSimplest() throws Exception {
        BookAddView bookAddView = new BookAddView(
                "Test", null, 2, null, null, null, null
        );


        Date dt = new Date();

        Book shouldBook = new Book.Builder()
                .bookId(6L)
                .title("Test")
                .insertDate(dt)
                .lastUpdateDate(dt)
                .bookStatus(BookStatus.COMPLETED)
                .readListId(5L)
                .build()
                ;


        Mockito.when(bookRepository.getNextId()).thenReturn(6L);
        Mockito.when(dateFactory.getCurrentDate()).thenReturn(dt);
        Mockito.when(bookRepository.getOne(Mockito.anyLong(), Mockito.anyLong())).thenReturn(shouldBook);

        ReadListService readListService = new ReadListService(
                bookRepository,
                seriesRepository,
                authorsRepository,
                authorsService,
                authorsBooksRepository,
                seriesBooksRespository,
                dateFactory,
                seriesService,
                bookSeriesRelationService,
                authorsBooksRelationService,
                bookTypesService
        );

        readListService.addBook(5L, bookAddView);

        verify(bookRepository).addOne(shouldBook);
    }

    @Test
    void shouldAddBookNoChapter() throws Exception {
        BookAddView bookAddView = new BookAddView(
                "Test", 1L, 2, 3L, 4L, null, null
        );

        Author author = new Author(1L, 5L, "Author");
        Series series = new Series(3L,5L,"Series");

        Date dt = new Date();

        Book shouldBook = new Book.Builder()
                .bookId(6L)
                .title("Test")
                .insertDate(dt)
                .lastUpdateDate(dt)
                .bookStatus(BookStatus.COMPLETED)
                .readListId(5L)
                .build()
                ;


        Mockito.when(bookRepository.getNextId()).thenReturn(6L);
        Mockito.when(dateFactory.getCurrentDate()).thenReturn(dt);
        Mockito.when(authorsService.getAuthor(Mockito.anyLong(), Mockito.anyLong())).thenReturn(Optional.of(author));
        Mockito.when(seriesService.getSeries(Mockito.anyLong(), Mockito.anyLong())).thenReturn(Optional.of(series));
        Mockito.when(bookRepository.getOne(Mockito.anyLong(), Mockito.anyLong())).thenReturn(shouldBook);

        ReadListService readListService = new ReadListService(
                bookRepository,
                seriesRepository,
                authorsRepository,
                authorsService,
                authorsBooksRepository,
                seriesBooksRespository,
                dateFactory,
                seriesService,
                bookSeriesRelationService,
                authorsBooksRelationService,
                bookTypesService
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
                "Test", null, 2, 3L, 4L, 7, null
        );

        Series series = new Series(3L,5L,"Series");

        Date dt = new Date();

        Book shouldBook = new Book.Builder()
                .bookId(6L)
                .title("Test")
                .insertDate(dt)
                .lastUpdateDate(dt)
                .bookStatus(BookStatus.COMPLETED)
                .lastChapter(7)
                .readListId(5L)
                .build()
                ;


        Mockito.when(bookRepository.getNextId()).thenReturn(6L);
        Mockito.when(dateFactory.getCurrentDate()).thenReturn(dt);
        Mockito.when(seriesService.getSeries(Mockito.anyLong(), Mockito.anyLong())).thenReturn(Optional.of(series));
        Mockito.when(bookRepository.getOne(Mockito.anyLong(), Mockito.anyLong())).thenReturn(shouldBook);

        ReadListService readListService = new ReadListService(
                bookRepository,
                seriesRepository,
                authorsRepository,
                authorsService,
                authorsBooksRepository,
                seriesBooksRespository,
                dateFactory,
                seriesService,
                bookSeriesRelationService,
                authorsBooksRelationService,
                bookTypesService
        );

        readListService.addBook(5L, bookAddView);

        InOrder inOrder = Mockito.inOrder(bookRepository, authorsBooksRepository, seriesBooksRespository);


        inOrder.verify(bookRepository).addOne(shouldBook);
        inOrder.verify(seriesBooksRespository).add(6L, 3L, 5L, 4L);

        verify(authorsBooksRepository, Mockito.never()).add(Mockito.anyLong(), Mockito.anyLong(), Mockito.anyLong());
    }

    @Test
    void shouldAddBookNoSeries() throws Exception {
        BookAddView bookAddView = new BookAddView(
                "Test", 1L, 2, null, null, 7, null
        );

        Author author = new Author(1L, 5L, "Author");

        Date dt = new Date();

        Book shouldBook = new Book.Builder()
                .bookId(6L)
                .title("Test")
                .insertDate(dt)
                .lastUpdateDate(dt)
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
                seriesService,
                bookSeriesRelationService,
                authorsBooksRelationService,
                bookTypesService
        );

        readListService.addBook(5L, bookAddView);

        InOrder inOrder = Mockito.inOrder(bookRepository, authorsBooksRepository, seriesBooksRespository);


        inOrder.verify(bookRepository).addOne(shouldBook);
        inOrder.verify(authorsBooksRepository).add(6L, 1L, 5L);

        verify(seriesBooksRespository, Mockito.never()).add(Mockito.anyLong(), Mockito.anyLong(), Mockito.anyLong(), Mockito.anyLong());

    }

    @Test
    void shouldAddBookWithType() throws Exception {
        BookAddView bookAddView = new BookAddView(
                "Test", null, 2, null, null, null, 3
        );


        Date dt = new Date();

        Book shouldBook = new Book.Builder()
                .bookId(6L)
                .title("Test")
                .insertDate(dt)
                .lastUpdateDate(dt)
                .bookStatus(BookStatus.COMPLETED)
                .readListId(5L)
                .bookType(new BookType(3,"Webtoon"))
                .build()
                ;


        Mockito.when(bookRepository.getNextId()).thenReturn(6L);
        Mockito.when(dateFactory.getCurrentDate()).thenReturn(dt);
        Mockito.when(bookRepository.getOne(Mockito.anyLong(), Mockito.anyLong())).thenReturn(shouldBook);
        Mockito.when(bookTypesService.findById(Mockito.anyInt())).thenReturn(Optional.of(new BookType(3,"Webtoon")));

        ReadListService readListService = new ReadListService(
                bookRepository,
                seriesRepository,
                authorsRepository,
                authorsService,
                authorsBooksRepository,
                seriesBooksRespository,
                dateFactory,
                seriesService,
                bookSeriesRelationService,
                authorsBooksRelationService,
                bookTypesService
        );

        readListService.addBook(5L, bookAddView);

        verify(bookRepository).addOne(shouldBook);
    }

}