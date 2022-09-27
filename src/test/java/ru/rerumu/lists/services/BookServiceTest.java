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

    @Mock
    private BookSeriesService bookSeriesService;

    @Test
    void shouldAddBook() throws Exception {
        BookAddView bookAddView = new BookAddView(
                "Test", null, 2, null, null
        );

//        Author author = new Author(1L, 5L, "Author");
//        Series series = new Series(3L,5L,"Series");

        Date dt = new Date();

        Book shouldBook = new Book.Builder()
                .bookId(6L)
                .title("Test")
                .insertDate(dt)
                .lastUpdateDate(dt)
                .statusId(2)
                .bookStatus(BookStatus.COMPLETED)
//                .bookStatus(new BookStatus(2L,null))
//                .authorId(1L)
//                .lastChapter(1)
                .readListId(5L)
//                .seriesId(3L)
//                .seriesOrder(4L)
                .build()
                ;


        Mockito.when(bookRepository.getNextId()).thenReturn(6L);
        Mockito.when(dateFactory.getCurrentDate()).thenReturn(dt);
//        Mockito.when(authorsService.getAuthor(Mockito.anyLong(), Mockito.anyLong())).thenReturn(Optional.of(author));
//        Mockito.when(bookSeriesService.getSeries(Mockito.anyLong(), Mockito.anyLong())).thenReturn(Optional.of(series));
        Mockito.when(bookRepository.getOne(Mockito.anyLong(), Mockito.anyLong())).thenReturn(shouldBook);

        ReadListService readListService = new ReadListService(
                bookRepository,
                seriesRepository,
                authorsRepository,
                authorsService,
                authorsBooksRepository,
                seriesBooksRespository,
                dateFactory,
                bookSeriesService
        );

        readListService.addBook(5L, bookAddView);

        InOrder inOrder = Mockito.inOrder(bookRepository, authorsBooksRepository, seriesBooksRespository);


        inOrder.verify(bookRepository).addOne(shouldBook);
//        inOrder.verify(authorsBooksRepository).add(6L, 1L, 5L);
//        inOrder.verify(seriesBooksRespository).add(6L, 3L, 5L, 4L);

        Mockito.verify(authorsBooksRepository, Mockito.never()).add(Mockito.anyLong(), Mockito.anyLong(), Mockito.anyLong());
        Mockito.verify(seriesBooksRespository, Mockito.never()).add(Mockito.anyLong(), Mockito.anyLong(), Mockito.anyLong(), Mockito.anyLong());

    }


    @Test
    void shouldAddBookSeries() throws Exception {
        BookAddView bookAddView = new BookAddView(
                "Test", null, 2, 3L, 4L
        );

//        Author author = new Author(1L, 5L, "Author");
        Series series = new Series(3L,5L,"Series");

        Date dt = new Date();

        Book.Builder builder = new Book.Builder()
                .bookId(6L)
                .title("Test")
                .insertDate(dt)
                .lastUpdateDate(dt)
                .statusId(2)
                .bookStatus(BookStatus.COMPLETED)
//                .authorId(1L)
//                .lastChapter(1)
                .readListId(5L)
//                .seriesId(3L)
//                .seriesOrder(4L)
                ;

        Book shouldBook = builder.build();



        Mockito.when(bookRepository.getNextId()).thenReturn(6L);
        Mockito.when(dateFactory.getCurrentDate()).thenReturn(dt);
//        Mockito.when(authorsService.getAuthor(Mockito.anyLong(), Mockito.anyLong())).thenReturn(Optional.empty());
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
                bookSeriesService
        );

        readListService.addBook(5L, bookAddView);

        InOrder inOrder = Mockito.inOrder(bookRepository, authorsBooksRepository, seriesBooksRespository);


        inOrder.verify(bookRepository).addOne(shouldBook);
//        inOrder.verify(authorsBooksRepository, M).add(6L, 1L, 5L);
        inOrder.verify(seriesBooksRespository).add(6L, 3L, 5L, 4L);

        Mockito.verify(authorsBooksRepository, Mockito.never()).add(Mockito.anyLong(), Mockito.anyLong(), Mockito.anyLong());
    }

    @Test
    void shouldAddBookAuthor() throws Exception {
        BookAddView bookAddView = new BookAddView(
                "Test", 1L, 2, null, null
        );

        Author author = new Author(1L, 5L, "Author");
//        Series series = new Series(3L,5L,"Series");

        Date dt = new Date();

        Book.Builder builder = new Book.Builder()
                .bookId(6L)
                .title("Test")
                .insertDate(dt)
                .lastUpdateDate(dt)
                .statusId(2)
                .bookStatus(BookStatus.COMPLETED)
//                .authorId(1L)
//                .lastChapter(1)
                .readListId(5L)
//                .seriesId(3L)
//                .seriesOrder(4L)
                ;

        Book shouldBook = builder.build();



        Mockito.when(bookRepository.getNextId()).thenReturn(6L);
        Mockito.when(dateFactory.getCurrentDate()).thenReturn(dt);
        Mockito.when(authorsService.getAuthor(Mockito.anyLong(), Mockito.anyLong())).thenReturn(Optional.of(author));
//        Mockito.when(bookSeriesService.getSeries(Mockito.anyLong(), Mockito.anyLong())).thenReturn(Optional.of(series));
        Mockito.when(bookRepository.getOne(Mockito.anyLong(), Mockito.anyLong())).thenReturn(shouldBook);

        ReadListService readListService = new ReadListService(
                bookRepository,
                seriesRepository,
                authorsRepository,
                authorsService,
                authorsBooksRepository,
                seriesBooksRespository,
                dateFactory,
                bookSeriesService
        );

        readListService.addBook(5L, bookAddView);

        InOrder inOrder = Mockito.inOrder(bookRepository, authorsBooksRepository, seriesBooksRespository);


        inOrder.verify(bookRepository).addOne(shouldBook);
        inOrder.verify(authorsBooksRepository).add(6L, 1L, 5L);
//        inOrder.verify(seriesBooksRespository).add(6L, 3L, 5L, 4L);

        Mockito.verify(seriesBooksRespository, Mockito.never()).add(Mockito.anyLong(), Mockito.anyLong(), Mockito.anyLong(), Mockito.anyLong());
    }

    // TODO: Test update

}