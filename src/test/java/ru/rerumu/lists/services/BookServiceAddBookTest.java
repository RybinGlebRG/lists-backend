//package ru.rerumu.lists.services;
//
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InOrder;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.junit.jupiter.MockitoExtension;
//import ru.rerumu.lists.utils.DateFactory;
//import ru.rerumu.lists.model.*;
//import ru.rerumu.lists.model.book.impl.BookFactory;
//import ru.rerumu.lists.model.book.impl.BookImpl;
//import ru.rerumu.lists.repository.*;
//import ru.rerumu.lists.controller.book.view.in.BookAddView;
//
//import java.time.LocalDateTime;
//import java.time.ZoneOffset;
//import java.util.Date;
//import java.util.Optional;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyInt;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//@ExtendWith(MockitoExtension.class)
//class BookServiceAddBookTest {
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
//    @Test
//    void shouldAddBook() throws Exception {
//        BookAddView bookAddView = new BookAddView(
//                "Test", 1L, 2, 3L, 4L, 7, null, null, "test note"
//        );
//
//        Author author = new Author(1L, 5L, "Author");
//        Series series = new Series.Builder().seriesId(3L).readListId(5L).title("Series").build();
//
//
//        LocalDateTime dtl = LocalDateTime.now();
//        Date dt = Date.from(dtl.toInstant(ZoneOffset.UTC));
//
//        BookImpl shouldBook = new BookImpl.Builder()
//                .bookId(6L)
//                .title("Test")
//                .insertDate(dt)
//                .lastUpdateDate(dt)
//                .bookStatus(new BookStatusRecord(2,"Completed"))
//                .lastChapter(7)
//                .readListId(5L)
//                .note("test note")
//                .build()
//                ;
//
//
//        Mockito.when(bookRepository.getNextId()).thenReturn(6L);
//        Mockito.when(dateFactory.getLocalDateTime()).thenReturn(dtl);
//        Mockito.when(authorsService.getAuthor(Mockito.anyLong(), Mockito.anyLong())).thenReturn(Optional.of(author));
////        Mockito.when(seriesService.getSeries(Mockito.anyLong())).thenReturn(Optional.of(series));
//        Mockito.when(bookRepository.getOne(Mockito.anyLong(), Mockito.anyLong())).thenReturn(shouldBook);
//        when(bookStatusesService.findById(anyInt()))
//                .thenReturn(Optional.of(new BookStatusRecord(2,"Completed")));
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
//        readListService.addBook(5L, bookAddView);
//
//        InOrder inOrder = Mockito.inOrder(bookRepository, authorsBooksRepository, seriesBooksRespository);
//
//
//        inOrder.verify(bookRepository).addOne(shouldBook);
//        inOrder.verify(authorsBooksRepository).add(6L, 1L, 5L);
////        inOrder.verify(seriesBooksRespository).add(6L, 3L, 5L, 4L);
//    }
//
//    @Test
//    void shouldAddBookSimplest() throws Exception {
//        BookAddView bookAddView = new BookAddView(
//                "Test", null, 2, null, null, null, null, null, null
//        );
//
//
//        LocalDateTime dtl = LocalDateTime.now();
//        Date dt = Date.from(dtl.toInstant(ZoneOffset.UTC));
//
//        BookImpl shouldBook = new BookImpl.Builder()
//                .bookId(6L)
//                .title("Test")
//                .insertDate(dt)
//                .lastUpdateDate(dt)
//                .bookStatus(new BookStatusRecord(2,"Completed"))
//                .readListId(5L)
//                .build()
//                ;
//
//
//        Mockito.when(bookRepository.getNextId()).thenReturn(6L);
//        Mockito.when(dateFactory.getLocalDateTime()).thenReturn(dtl);
//        Mockito.when(bookRepository.getOne(Mockito.anyLong(), Mockito.anyLong())).thenReturn(shouldBook);
//        when(bookStatusesService.findById(anyInt()))
//                .thenReturn(Optional.of(new BookStatusRecord(2,"Completed")));
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
//        readListService.addBook(5L, bookAddView);
//
//        verify(bookRepository).addOne(shouldBook);
//    }
//
//    @Test
//    void shouldAddBookNoChapter() throws Exception {
//        BookAddView bookAddView = new BookAddView(
//                "Test", 1L, 2, 3L, 4L, null, null, null, null
//        );
//
//        Author author = new Author(1L, 5L, "Author");
//        Series series = new Series.Builder().seriesId(3L).readListId(5L).title("Series").build();
//
//        LocalDateTime dtl = LocalDateTime.now();
//        Date dt = Date.from(dtl.toInstant(ZoneOffset.UTC));
//
//        BookImpl shouldBook = new BookImpl.Builder()
//                .bookId(6L)
//                .title("Test")
//                .insertDate(dt)
//                .lastUpdateDate(dt)
//                .bookStatus(new BookStatusRecord(2,"Completed"))
//                .readListId(5L)
//                .build()
//                ;
//
//
//        Mockito.when(bookRepository.getNextId()).thenReturn(6L);
//        Mockito.when(dateFactory.getLocalDateTime()).thenReturn(dtl);
//        Mockito.when(authorsService.getAuthor(Mockito.anyLong(), Mockito.anyLong())).thenReturn(Optional.of(author));
////        Mockito.when(seriesService.getSeries(Mockito.anyLong())).thenReturn(Optional.of(series));
//        Mockito.when(bookRepository.getOne(Mockito.anyLong(), Mockito.anyLong())).thenReturn(shouldBook);
//        when(bookStatusesService.findById(anyInt()))
//                .thenReturn(Optional.of(new BookStatusRecord(2,"Completed")));
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
//        readListService.addBook(5L, bookAddView);
//
//        InOrder inOrder = Mockito.inOrder(bookRepository, authorsBooksRepository, seriesBooksRespository);
//
//
//        inOrder.verify(bookRepository).addOne(shouldBook);
//        inOrder.verify(authorsBooksRepository).add(6L, 1L, 5L);
////        inOrder.verify(seriesBooksRespository).add(6L, 3L, 5L, 4L);
//    }
//
//    @Test
//    void shouldAddBookNoAuthor() throws Exception {
//        BookAddView bookAddView = new BookAddView(
//                "Test", null, 2, 3L, 4L, 7, null, null, null
//        );
//
//        Series series = new Series.Builder().seriesId(3L).readListId(25L).title("Series").build();
//
//        LocalDateTime dtl = LocalDateTime.now();
//        Date dt = Date.from(dtl.toInstant(ZoneOffset.UTC));
//
//        BookImpl shouldBook = new BookImpl.Builder()
//                .bookId(6L)
//                .title("Test")
//                .insertDate(dt)
//                .lastUpdateDate(dt)
//                .bookStatus(new BookStatusRecord(2,"Completed"))
//                .lastChapter(7)
//                .readListId(5L)
//                .build()
//                ;
//
//
//        Mockito.when(bookRepository.getNextId()).thenReturn(6L);
//        Mockito.when(dateFactory.getLocalDateTime()).thenReturn(dtl);
//        Mockito.when(bookRepository.getOne(Mockito.anyLong(), Mockito.anyLong()))
//                .thenReturn(shouldBook);
//        when(bookStatusesService.findById(anyInt()))
//                .thenReturn(Optional.of(new BookStatusRecord(2,"Completed")));
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
//        readListService.addBook(5L, bookAddView);
//
//        InOrder inOrder = Mockito.inOrder(bookRepository, authorsBooksRepository, seriesBooksRespository);
//
//
//        inOrder.verify(bookRepository).addOne(shouldBook);
////        inOrder.verify(seriesBooksRespository).add(6L, 3L, 5L, 4L);
//
//        verify(authorsBooksRepository, Mockito.never()).add(Mockito.anyLong(), Mockito.anyLong(), Mockito.anyLong());
//    }
//
//    @Test
//    void shouldAddBookNoSeries() throws Exception {
//        BookAddView bookAddView = new BookAddView(
//                "Test", 1L, 2, null, null, 7, null, null, null
//        );
//
//        Author author = new Author(1L, 5L, "Author");
//
//        LocalDateTime dtl = LocalDateTime.now();
//        Date dt = Date.from(dtl.toInstant(ZoneOffset.UTC));
//
//        BookImpl shouldBook = new BookImpl.Builder()
//                .bookId(6L)
//                .title("Test")
//                .insertDate(dt)
//                .lastUpdateDate(dt)
//                .bookStatus(new BookStatusRecord(2,"Completed"))
//                .lastChapter(7)
//                .readListId(5L)
//                .build()
//                ;
//
//
//        Mockito.when(bookRepository.getNextId()).thenReturn(6L);
//        Mockito.when(dateFactory.getLocalDateTime()).thenReturn(dtl);
//        Mockito.when(authorsService.getAuthor(Mockito.anyLong(), Mockito.anyLong())).thenReturn(Optional.of(author));
//        Mockito.when(bookRepository.getOne(Mockito.anyLong(), Mockito.anyLong())).thenReturn(shouldBook);
//        when(bookStatusesService.findById(anyInt()))
//                .thenReturn(Optional.of(new BookStatusRecord(2,"Completed")));
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
//        readListService.addBook(5L, bookAddView);
//
//        InOrder inOrder = Mockito.inOrder(bookRepository, authorsBooksRepository, seriesBooksRespository);
//
//
//        inOrder.verify(bookRepository).addOne(shouldBook);
//        inOrder.verify(authorsBooksRepository).add(6L, 1L, 5L);
//
//        verify(seriesBooksRespository, Mockito.never()).add(Mockito.anyLong(), Mockito.anyLong(), Mockito.anyLong(), Mockito.anyLong());
//
//    }
//
//    @Test
//    void shouldAddBookWithType() throws Exception {
//        BookAddView bookAddView = new BookAddView(
//                "Test", null, 2, null, null, null, 3, null, null
//        );
//
//
//        LocalDateTime dtl = LocalDateTime.now();
//        Date dt = Date.from(dtl.toInstant(ZoneOffset.UTC));
//
//        BookImpl shouldBook = new BookImpl.Builder()
//                .bookId(6L)
//                .title("Test")
//                .insertDate(dt)
//                .lastUpdateDate(dt)
//                .bookStatus(new BookStatusRecord(2,"Completed"))
//                .readListId(5L)
//                .bookType(new BookType(3,"Webtoon"))
//                .build()
//                ;
//
//
//        Mockito.when(bookRepository.getNextId()).thenReturn(6L);
//        when(dateFactory.getLocalDateTime()).thenReturn(dtl);
//        Mockito.when(bookRepository.getOne(Mockito.anyLong(), Mockito.anyLong())).thenReturn(shouldBook);
//        Mockito.when(bookTypesService.findById(Mockito.anyInt())).thenReturn(Optional.of(new BookType(3,"Webtoon")));
//        when(bookStatusesService.findById(anyInt()))
//                .thenReturn(Optional.of(new BookStatusRecord(2,"Completed")));
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
//        readListService.addBook(5L, bookAddView);
//
//        verify(bookRepository).addOne(shouldBook);
//    }
//
//}