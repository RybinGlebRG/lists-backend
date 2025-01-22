package ru.rerumu.lists.services;

import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.rerumu.lists.exception.EmptyMandatoryParameterException;
import ru.rerumu.lists.exception.EntityNotFoundException;
import ru.rerumu.lists.factories.DateFactory;
import ru.rerumu.lists.model.*;
import ru.rerumu.lists.model.book.Book;
import ru.rerumu.lists.model.book.BookFactory;
import ru.rerumu.lists.model.book.BookImpl;
import ru.rerumu.lists.model.book.BookBuilder;
import ru.rerumu.lists.model.books.Filter;
import ru.rerumu.lists.model.books.Search;
import ru.rerumu.lists.model.books.reading_records.ReadingRecord;
import ru.rerumu.lists.repository.*;
import ru.rerumu.lists.views.BookAddView;
import ru.rerumu.lists.views.BookUpdateView;
import ru.rerumu.lists.views.ReadingRecordAddView;
import ru.rerumu.lists.views.ReadingRecordUpdateView;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class ReadListService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final BookRepository bookRepository;
    private final AuthorsService authorsService;
    private final AuthorsBooksRepository authorsBooksRepository;
    private final SeriesBooksRespository seriesBooksRespository;
    private final DateFactory dateFactory;
    private final BookSeriesRelationService bookSeriesRelationService;
    private final AuthorsBooksRelationService authorsBooksRelationService;
    private final BookTypesService bookTypesService;
    private final BookStatusesService bookStatusesService;
    private final FuzzyMatchingService fuzzyMatchingService;
    private final ReadingRecordService readingRecordService;
    private final BookFactory bookFactory;

    @Autowired
    public ReadListService(
            BookRepository bookRepository,
            AuthorsService authorsService,
            AuthorsBooksRepository authorsBooksRepository,
            SeriesBooksRespository seriesBooksRespository,
            DateFactory dateFactory,
            BookSeriesRelationService bookSeriesRelationService,
            AuthorsBooksRelationService authorsBooksRelationService,
            BookTypesService bookTypesService,
            BookStatusesService bookStatusesService,
            FuzzyMatchingService fuzzyMatchingService,
            ReadingRecordService readingRecordService, BookFactory bookFactory
    ) {
        this.bookRepository = bookRepository;
        this.authorsService = authorsService;
        this.authorsBooksRepository = authorsBooksRepository;
        this.seriesBooksRespository = seriesBooksRespository;
        this.dateFactory = dateFactory;
        this.bookSeriesRelationService = bookSeriesRelationService;
        this.authorsBooksRelationService = authorsBooksRelationService;
        this.bookTypesService = bookTypesService;
        this.bookStatusesService = bookStatusesService;
        this.fuzzyMatchingService = fuzzyMatchingService;
        this.readingRecordService = readingRecordService;
        this.bookFactory = bookFactory;
    }

    private void updateAuthor(long bookId, Long authorId, long readListId) {
        List<AuthorBookRelation> authorsBooksRepositoryList = authorsBooksRepository.getByBookId(bookId, readListId);

        Optional<Author> optionalAuthor = authorId != null ?
                authorsService.getAuthor(readListId, authorId) :
                Optional.empty();

        authorsBooksRepositoryList.stream()
                .filter(item -> item.getBook().getBookId().equals(bookId) &&
                        item.getBook().getReadListId().equals(readListId) &&
                        (optionalAuthor.isEmpty() || !optionalAuthor.get().equals(item.getAuthor()))
                )
                .forEach(item -> authorsBooksRelationService.delete(
                        item.getBook().getBookId(),
                        item.getAuthor().getAuthorId(),
                        item.getBook().getReadListId()
                ));

        if (authorsBooksRepositoryList.stream()
                .noneMatch(item -> optionalAuthor.isPresent() &&
                        item.getAuthor().equals(optionalAuthor.get()) &&
                        item.getBook().getBookId().equals(bookId) &&
                        item.getBook().getReadListId().equals(readListId))
        ) {
            optionalAuthor.ifPresent(author -> authorsBooksRepository.add(bookId, author.getAuthorId(), author.getReadListId()));
        }
    }

//    private void updateSeries(long bookId, Long seriesId, long readListId, Long seriesOrder, Book book) {
//        List<SeriesBookRelation> seriesBookRelationList = seriesBooksRespository.getByBookId(bookId, readListId);
//
////        List<Series> seriesList = seriesService.findByBook(book);
//
//
//        Optional<Series> optionalSeries = seriesId != null ?
//                seriesService.getSeries( seriesId) :
//                Optional.empty();
//
//        seriesBookRelationList.stream()
//                .filter(item -> item.book().getBookId().equals(bookId) &&
//                        item.book().getReadListId().equals(readListId) &&
//                        (optionalSeries.isEmpty() || !optionalSeries.get().equals(item.series()))
//                )
//                .forEach(item -> bookSeriesRelationService.delete(
//                        item.book().getBookId(),
//                        item.series().getSeriesId(),
//                        item.book().getReadListId()
//                ));
//
//        seriesBookRelationList.stream()
//                .filter(item -> optionalSeries.isPresent() &&
//                        item.series().equals(optionalSeries.get()) &&
//                        item.book().getBookId().equals(bookId) &&
//                        item.book().getReadListId().equals(readListId)
//                )
//                .forEach(item -> bookSeriesRelationService.update(new SeriesBookRelation(
//                        item.book(),
//                        item.series(),
//                        seriesOrder
//                )));
//
//        if (seriesBookRelationList.stream().noneMatch(item -> optionalSeries.isPresent() &&
//                optionalSeries.get().equals(item.series()) &&
//                item.book().getBookId().equals(bookId) &&
//                item.book().getReadListId().equals(readListId))
//        ) {
//            optionalSeries.ifPresent(series -> seriesBooksRespository.add(
//                    bookId,
//                    series.getSeriesId(),
//                    series.getSeriesListId(),
//                    seriesOrder)
//            );
//        }
//    }

    @Transactional(rollbackFor = Exception.class)
    public void updateBook(Long bookId, BookUpdateView bookUpdateView) throws EmptyMandatoryParameterException, CloneNotSupportedException {
        if (
                bookUpdateView == null
                        || bookUpdateView.getReadListId() == null
                        || bookId == null
        ) {
            throw new EmptyMandatoryParameterException();
        }

        BookImpl currentBook = bookRepository.getOne(bookUpdateView.getReadListId(), bookId);

        BookBuilder builder = new BookBuilder(currentBook);

        builder.insertDate(Date.from(bookUpdateView.getInsertDateUTC().toInstant(ZoneOffset.UTC)))
                .title(bookUpdateView.getTitle());

        bookUpdateView.getLastChapter().ifPresent(builder::lastChapter);

        // TODO: Test + add other fields
        if (!bookUpdateView.getLastChapter().equals(currentBook.getLastChapter()) ||
                bookUpdateView.getStatus() != currentBook.getBookStatus().statusId()) {
            builder.lastUpdateDate(dateFactory.getLocalDateTime());
        }

        Objects.requireNonNull(bookUpdateView.getStatus(), "Book status cannot be null");
        builder.bookStatus(
                bookStatusesService.findById(bookUpdateView.getStatus()).orElseThrow()
        );

        if (bookUpdateView.getBookTypeId() != null) {
            Optional<BookType> optionalBookType = bookTypesService.findById(bookUpdateView.getBookTypeId());
            if (optionalBookType.isEmpty()) {
                throw new IllegalArgumentException();
            }

            optionalBookType.ifPresent(builder::bookType);
//            builder.bookType(
//                    new BookTypeOld.Builder()
//                            .typeId(bookUpdateView.getBookTypeId())
//                            .build()
//            );
        }

        builder.note(bookUpdateView.note());

        BookImpl updatedBook = builder.build();

        logger.debug(String.format("Updated book: %s", updatedBook.toString()));

        bookRepository.update(updatedBook);

        updateAuthor(bookId, bookUpdateView.getAuthorId(), bookUpdateView.getReadListId());

//        updateSeries(bookId, bookUpdateView.getSeriesId(), bookUpdateView.getReadListId(), bookUpdateView.getOrder(),updatedBook);

    }

    public BookImpl getBook(Long readListId, Long bookId) {
        BookImpl book = this.bookRepository.getOne(readListId, bookId);
        logger.info(String.format("Got book '%s'", book.toString()));
        return book;
    }

    public Book getBook(Long bookId){
        Book book = bookFactory.getBook(bookId);
        logger.info(String.format("Got book '%s'", book.toString()));
        return book;
    }

    public Optional<BookImpl> getOptionalBook(Long bookId) {
        return this.bookRepository.getOne(bookId);
    }

    public List<BookImpl> getAllBooks(Long readListId, Search search) {
        List<BookImpl> bookList;
        if (search.getChainBySeries()) {
            bookList = bookRepository.getAllChained(readListId);
        } else {
            bookList = this.bookRepository.getAll(readListId);
        }

        Stream<BookImpl> bookStream = bookList.stream();
        for (Filter filter : search.filters()) {
            switch (filter.field()) {
                case "bookStatusIds" -> {
                    bookStream = bookStream
                            .filter(book -> filter.values().contains(book.getBookStatus().statusId().toString()));
                }
                case "titles" -> {
                    bookStream = fuzzyMatchingService.findMatchingBooksByTitle(filter.values(), bookStream);
                }
                default -> throw new IllegalArgumentException();
            }
        }

        bookList = bookStream.collect(Collectors.toCollection(ArrayList::new));

        logger.debug(bookList.toString());
        return bookList;
    }

    @Deprecated
    public List<Author> getAuthors(Long readListId) {
        return authorsService.getAuthors(readListId);
    }

    @Transactional(rollbackFor = Exception.class)
    public Book addBook(Long readListId, BookAddView bookAddView) throws EmptyMandatoryParameterException, EntityNotFoundException {

        // Create book
        BookStatusRecord bookStatus = bookStatusesService.findById(bookAddView.status()).orElseThrow();

        BookType bookType = null;
        if (bookAddView.getBookTypeId() != null) {
            bookType = bookTypesService.findById(bookAddView.getBookTypeId()).orElseThrow();
        }

        Book newBook = bookFactory.createBook(
                readListId,
                bookAddView.getTitle(),
                bookAddView.getLastChapter(),
                bookAddView.note(),
                bookStatus,
                bookAddView.insertDate(),
                bookType
        );


        // Connect with author
        if (bookAddView.getAuthorId() != null) {
            authorsBooksRepository.add(
                    newBook.getId(),
                    authorsService.getAuthor(
                            readListId,
                            bookAddView.getAuthorId()
                    ).orElseThrow().getAuthorId(),
                    readListId
            );
        }

        // Create reading record
        newBook.addReadingRecord(
                bookStatus,
                bookAddView.insertDate(),
                null
        );

        return getBook(newBook.getId());
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteBook(Long bookId) throws EntityNotFoundException {
        Optional<BookImpl> bookOptional = bookRepository.getOne(bookId);
        if (bookOptional.isEmpty()) {
            throw new EntityNotFoundException();
        }

        seriesBooksRespository.getByBookId(
                        bookOptional.get().getBookId(),
                        bookOptional.get().getReadListId()
                )
                .forEach(item -> bookSeriesRelationService.delete(
                        item.book().getBookId(),
                        item.series().getSeriesId(),
                        item.book().getReadListId()
                ));

        authorsBooksRepository.getByBookId(
                        bookOptional.get().getBookId(),
                        bookOptional.get().getBookId()
                )
                .forEach(item -> authorsBooksRelationService.delete(
                        item.getBook().getBookId(),
                        item.getAuthor().getAuthorId(),
                        item.getBook().getReadListId()
                ));

        bookRepository.delete(bookOptional.get().getBookId());
    }

    public Optional<User> getBookUser(Long bookId) {
        return bookRepository.getBookUser(bookId);
    }

    @Transactional(rollbackFor = Exception.class)
    public ReadingRecord addReadingRecord(@NonNull Long bookId, @NonNull ReadingRecordAddView readingRecordAddView) {

        Book book = getOptionalBook(bookId).orElseThrow(EntityNotFoundException::new);
        BookStatusRecord bookStatusRecord = bookStatusesService.findById(readingRecordAddView.statusId()).orElseThrow();
        Long readingRecordId = readingRecordService.getNextId();
        ReadingRecord addedReadingRecord = book.addReadingRecord(
                bookId,
                readingRecordId,
                bookStatusRecord,
                readingRecordAddView.startDate(),
                readingRecordAddView.endDate()
        );

        return readingRecordService.addRecord(addedReadingRecord);
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteReadingRecord(Long bookId, Long readingRecordId) {
        BookImpl book = getOptionalBook(bookId).orElseThrow(EntityNotFoundException::new);

        ReadingRecord readingRecord = book.deleteReadingRecord(readingRecordId);

        readingRecordService.deleteRecord(readingRecord.recordId());
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateReadingRecord(Long bookId, Long recordId, ReadingRecordUpdateView readingRecordUpdateView) {
        BookImpl book = getOptionalBook(bookId).orElseThrow(EntityNotFoundException::new);
        BookStatusRecord bookStatusRecord = bookStatusesService.findById(readingRecordUpdateView.statusId()).orElseThrow();

        ReadingRecord readingRecord = book.updateReadingRecord(
                recordId,
                bookStatusRecord,
                readingRecordUpdateView.startDate(),
                readingRecordUpdateView.endDate()
        );

        readingRecordService.updateRecord(readingRecord);
    }
}
