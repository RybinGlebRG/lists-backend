//package ru.rerumu.lists.services;
//
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import ru.rerumu.lists.utils.DateFactory;
//import ru.rerumu.lists.model.*;
//import ru.rerumu.lists.model.book.impl.BookFactory;
//import ru.rerumu.lists.model.book.impl.BookImpl;
//import ru.rerumu.lists.repository.*;
//import ru.rerumu.lists.views.BookUpdateView;
//
//import java.time.LocalDateTime;
//import java.time.ZoneOffset;
//import java.util.Date;
//import java.util.List;
//import java.util.Optional;
//
//import static org.mockito.ArgumentMatchers.anyLong;
//import static org.mockito.Mockito.*;
//
//// TODO: Check tests
//@ExtendWith(MockitoExtension.class)
//class BookServiceUpdateBookUpdateAuthorTest {
//
//    @Mock
//    private BookRepository bookRepository;
//    @Mock
//    private AuthorsService authorsService;
//    @Mock
//    private AuthorsBooksRepository authorsBooksRepository;
//    @Mock
//    private SeriesBooksRespository seriesBooksRespository;
//    @Mock
//    private DateFactory dateFactory;
//    @Mock
//    private BookSeriesRelationService bookSeriesRelationService;
//
//    @Mock
//    private  AuthorsBooksRelationService authorsBooksRelationService;
//
//    @Mock
//    private BookTypesService bookTypesService;
//
//    @Mock
//    BookStatusesService bookStatusesService;
//    @Mock
//    FuzzyMatchingService fuzzyMatchingService;
//    @Mock
//    ReadingRecordService readingRecordService;
//    @Mock
//    BookFactory bookFactory;
//
//
//    @Test
//    void shouldChangeAuthor() throws Exception{
//        BookUpdateView bookUpdateView = new BookUpdateView(
//                3L,
//                "Title",
//                4L,
//                1,
//                6L,
//                7L,
//                4,
//                LocalDateTime.of(2000, 10, 1, 0, 0, 0),
//                null,
//                null
//        );
////        Series series = new Series(5L,3L,"Series");
////        Series shouldSeries = new Series(6L,3L,"SeriesNew");
//        Author author = new Author(2L, 3L,"Author");
//        Author shouldAuthor = new Author(4L, 3L,"AuthorNew");
//        BookImpl book = new BookImpl.Builder()
//                .bookId(8L)
//                .title("Title")
//                .insertDate(Date.from(LocalDateTime.of(2000, 10, 1, 0, 0, 0).toInstant(ZoneOffset.UTC)))
//                .lastUpdateDate(Date.from(LocalDateTime.of(2000, 10, 1, 0, 0, 0).toInstant(ZoneOffset.UTC)))
//                .bookStatus(new BookStatusRecord(1,"In Progress"))
//                .lastChapter(4)
//                .readListId(3L)
//                .build();
//
//        when(bookRepository.getOne(anyLong(),anyLong())).thenReturn(book);
//        when(authorsBooksRepository.getByBookId(anyLong(),anyLong())).thenReturn(
//                List.of(new AuthorBookRelation(book,author))
//        );
//        when(authorsService.getAuthor(anyLong(),anyLong())).thenReturn(Optional.of(shouldAuthor));
//        when(bookStatusesService.findById(anyInt()))
//                .thenReturn(Optional.of(new BookStatusRecord(1,"In Progress")));
//
//        ReadListService readListService = new ReadListService(
//                bookRepository,
//                authorsService,
//                authorsBooksRepository,
//                seriesBooksRespository,
//                dateFactory,
//                bookSeriesRelationService,
//                authorsBooksRelationService,
//                bookTypesService,
//                bookStatusesService,
//                fuzzyMatchingService,
//                readingRecordService,
//                bookFactory
//        );
//
//        readListService.updateBook(8L,bookUpdateView);
//
//
//        verify(authorsBooksRelationService).delete(8L,2L,3L);
//        verify(authorsBooksRepository).add(8L,4L,3L);
//    }
//
//    @Test
//    void shouldAddAuthor() throws Exception{
//        BookUpdateView bookUpdateView = new BookUpdateView(
//                3L,
//                "Title",
//                4L,
//                1,
//                6L,
//                7L,
//                4,
//                LocalDateTime.of(2000, 10, 1, 0, 0, 0),
//                null,
//                null
//        );
////        Series series = new Series(5L,3L,"Series");
////        Series shouldSeries = new Series(6L,3L,"SeriesNew");
////        Author author = new Author(2L, 3L,"Author");
//        Author shouldAuthor = new Author(4L, 3L,"AuthorNew");
//        BookImpl book = new BookImpl.Builder()
//                .bookId(8L)
//                .title("Title")
//                .insertDate(Date.from(LocalDateTime.of(2000, 10, 1, 0, 0, 0).toInstant(ZoneOffset.UTC)))
//                .lastUpdateDate(Date.from(LocalDateTime.of(2000, 10, 1, 0, 0, 0).toInstant(ZoneOffset.UTC)))
//                .bookStatus(new BookStatusRecord(1,"In Progress"))
//                .lastChapter(4)
//                .readListId(3L)
//                .build();
//
//        when(bookRepository.getOne(anyLong(),anyLong())).thenReturn(book);
//        when(authorsBooksRepository.getByBookId(anyLong(), anyLong())).thenReturn(
//                List.of()
//        );
//        when(authorsService.getAuthor(anyLong(),anyLong())).thenReturn(Optional.of(shouldAuthor));
//        when(bookStatusesService.findById(anyInt()))
//                .thenReturn(Optional.of(new BookStatusRecord(1,"In Progress")));
//
//        ReadListService readListService = new ReadListService(
//                bookRepository,
//                authorsService,
//                authorsBooksRepository,
//                seriesBooksRespository,
//                dateFactory,
//                bookSeriesRelationService,
//                authorsBooksRelationService,
//                bookTypesService,
//                bookStatusesService,
//                fuzzyMatchingService,
//                readingRecordService,
//                bookFactory
//        );
//
//        readListService.updateBook(8L,bookUpdateView);
//
//
//        verify(authorsBooksRelationService, never()).delete(anyLong(),anyLong(),anyLong());
//        verify(authorsBooksRepository).add(8L,4L,3L);
//    }
//
//    @Test
//    void shouldRemoveAuthor() throws Exception{
//        BookUpdateView bookUpdateView = new BookUpdateView(
//                3L,
//                "Title",
//                null,
//                1,
//                6L,
//                7L,
//                4,
//                LocalDateTime.of(2000, 10, 1, 0, 0, 0),
//                null,
//                null
//        );
////        Series series = new Series(5L,3L,"Series");
////        Series shouldSeries = new Series(6L,3L,"SeriesNew");
//        Author author = new Author(2L, 3L,"Author");
////        Author shouldAuthor = new Author(4L, 3L,"AuthorNew");
//        BookImpl book = new BookImpl.Builder()
//                .bookId(8L)
//                .title("Title")
//                .insertDate(Date.from(LocalDateTime.of(2000, 10, 1, 0, 0, 0).toInstant(ZoneOffset.UTC)))
//                .lastUpdateDate(Date.from(LocalDateTime.of(2000, 10, 1, 0, 0, 0).toInstant(ZoneOffset.UTC)))
//                .bookStatus(new BookStatusRecord(1,"In Progress"))
//                .lastChapter(4)
//                .readListId(3L)
//                .build();
//
//        when(bookRepository.getOne(anyLong(),anyLong())).thenReturn(book);
//        when(authorsBooksRepository.getByBookId(anyLong(), anyLong())).thenReturn(
//                List.of(new AuthorBookRelation(book,author))
//        );
////        when(authorsService.getAuthor(anyLong(),anyLong())).thenReturn(Optional.empty());
//        when(bookStatusesService.findById(anyInt()))
//                .thenReturn(Optional.of(new BookStatusRecord(1,"In Progress")));
//
//        ReadListService readListService = new ReadListService(
//                bookRepository,
//                authorsService,
//                authorsBooksRepository,
//                seriesBooksRespository,
//                dateFactory,
//                bookSeriesRelationService,
//                authorsBooksRelationService,
//                bookTypesService,
//                bookStatusesService,
//                fuzzyMatchingService,
//                readingRecordService,
//                bookFactory
//        );
//
//        readListService.updateBook(8L,bookUpdateView);
//
//
//        verify(authorsBooksRelationService).delete(8L,2L,3L);
//        verify(authorsBooksRepository, never()).add(anyLong(),anyLong(),anyLong());
//    }
//
//}