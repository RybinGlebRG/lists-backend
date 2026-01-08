package ru.rerumu.lists.services.book.impl;

import com.jcabi.aspects.Loggable;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.rerumu.lists.controller.book.view.in.BookAddView;
import ru.rerumu.lists.controller.book.view.in.BookUpdateView;
import ru.rerumu.lists.crosscut.exception.EmptyMandatoryParameterException;
import ru.rerumu.lists.crosscut.exception.EntityNotFoundException;
import ru.rerumu.lists.crosscut.utils.FuzzyMatchingService;
import ru.rerumu.lists.dao.book.AuthorRole;
import ru.rerumu.lists.dao.book.AuthorsBooksRepository;
import ru.rerumu.lists.dao.book.BookRepository;
import ru.rerumu.lists.dao.booktype.BookTypeRepository;
import ru.rerumu.lists.dao.readingrecord.ReadingRecordsRepository;
import ru.rerumu.lists.domain.author.Author;
import ru.rerumu.lists.domain.author.AuthorFactory;
import ru.rerumu.lists.domain.book.Book;
import ru.rerumu.lists.domain.book.BookFactory;
import ru.rerumu.lists.domain.book.impl.BookFactoryImpl;
import ru.rerumu.lists.domain.books.Filter;
import ru.rerumu.lists.domain.books.Search;
import ru.rerumu.lists.domain.bookstatus.BookStatusRecord;
import ru.rerumu.lists.domain.bookstatus.StatusFactory;
import ru.rerumu.lists.domain.booktype.BookType;
import ru.rerumu.lists.domain.readingrecords.ReadingRecord;
import ru.rerumu.lists.domain.series.Series;
import ru.rerumu.lists.domain.series.SeriesFactory;
import ru.rerumu.lists.domain.tag.Tag;
import ru.rerumu.lists.domain.tag.TagFactory;
import ru.rerumu.lists.domain.user.User;
import ru.rerumu.lists.domain.user.UserFactory;
import ru.rerumu.lists.services.book.BookService;
import ru.rerumu.lists.services.book.status.BookStatusesService;
import ru.rerumu.lists.services.book.type.BookTypesService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component("BookServiceImpl")
public class ReadListService implements BookService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final BookRepository bookRepository;
    private final AuthorsBooksRepository authorsBooksRepository;
    private final BookTypesService bookTypesService;
    private final BookStatusesService bookStatusesService;
    private final FuzzyMatchingService fuzzyMatchingService;
    private final BookFactory bookFactory;
    private final TagFactory tagFactory;
    private final UserFactory userFactory;
    private final AuthorFactory authorFactory;
    private final SeriesFactory seriesFactory;
    private final BookTypeRepository bookTypeRepository;
    private final StatusFactory statusFactory;
    private final ReadingRecordsRepository readingRecordsRepository;

    @Autowired
    public ReadListService(
            BookRepository bookRepository,
            AuthorsBooksRepository authorsBooksRepository,
            BookTypesService bookTypesService,
            BookStatusesService bookStatusesService,
            FuzzyMatchingService fuzzyMatchingService,
            BookFactoryImpl bookFactory,
            TagFactory tagFactory,
            UserFactory userFactory,
            AuthorFactory authorFactory,
            SeriesFactory seriesFactory,
            BookTypeRepository bookTypeRepository,
            StatusFactory statusFactory, ReadingRecordsRepository readingRecordsRepository
    ) {
        this.bookRepository = bookRepository;
        this.authorsBooksRepository = authorsBooksRepository;
        this.bookTypesService = bookTypesService;
        this.bookStatusesService = bookStatusesService;
        this.fuzzyMatchingService = fuzzyMatchingService;
        this.bookFactory = bookFactory;
        this.tagFactory = tagFactory;
        this.userFactory = userFactory;
        this.authorFactory = authorFactory;
        this.seriesFactory = seriesFactory;
        this.bookTypeRepository = bookTypeRepository;
        this.statusFactory = statusFactory;
        this.readingRecordsRepository = readingRecordsRepository;
    }

    /**
     * Update book
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @Loggable(value = Loggable.DEBUG, trim = false, prepend = true)
    public Book updateBook(@NonNull Long bookId, @NonNull Long userId, @NonNull BookUpdateView bookUpdateView) {

        logger.info("Getting user with id='{}'...", userId);
        User user = userFactory.findById(userId);

        logger.info("Getting book with id='{}'...", bookId);
        Book book = bookFactory.getBook(bookId, userId);

        // Update insert date
        logger.info("Updating insert date...");
        book.updateInsertDate(bookUpdateView.getInsertDateUTC());

        // Update book title
        logger.info("Updating book title...");
        book.updateTitle(bookUpdateView.getTitle());

        // Update note
        logger.info("Updating note...");
        book.updateNote(bookUpdateView.getNote());

        // Update book type
        logger.info("Updating book type...");
        if (bookUpdateView.getBookTypeId() != null) {
            BookType optionalBookType = bookTypesService.findById(Long.valueOf(bookUpdateView.getBookTypeId()));
            book.updateType(optionalBookType);
        }

        // Update URL
        logger.info("Updating URL...");
        book.updateURL(bookUpdateView.getURL());

        // Update tags
        logger.info("Updating tags...");
        List<Tag> tags = tagFactory.findByIds(bookUpdateView.getTagIds(), book.getUser());
        book.updateTags(tags);

        // Update reading records
        logger.info("Updating reading records...");
        List<ReadingRecord> readingRecords = new ArrayList<>();

        List<BookUpdateView.ReadingRecordView> recordsToUpdate = bookUpdateView.getReadingRecords().stream()
                .filter(readingRecordView ->  readingRecordView.getReadingRecordId() != null)
                .collect(Collectors.toCollection(ArrayList::new));
        List<Long> idsToUpdate = recordsToUpdate.stream()
                .map(BookUpdateView.ReadingRecordView::getReadingRecordId)
                .collect(Collectors.toCollection(ArrayList::new));

        // Get reading records to update
        Map<Long, ReadingRecord> readingRecordMap = book.getReadingRecords().stream()
                .filter(readingRecord -> idsToUpdate.contains(readingRecord.getId()))
                .collect(Collectors.toMap(
                        ReadingRecord::getId,
                        Function.identity()
                ));

        // Update existing reading records
        for (BookUpdateView.ReadingRecordView readingRecordView: recordsToUpdate) {
            ReadingRecord readingRecord = readingRecordMap.get(readingRecordView.getReadingRecordId());

            BookStatusRecord bookStatusRecord = statusFactory.findById(Long.valueOf(readingRecordView.getStatusId()));

            readingRecord.update(
                    bookStatusRecord,
                    readingRecordView.getStartDate(),
                    readingRecordView.getEndDate(),
                    readingRecordView.getLastChapter()
            );

            readingRecords.add(readingRecord);
        }

        // Create
        List<BookUpdateView.ReadingRecordView> recordsToCreate = bookUpdateView.getReadingRecords().stream()
                .filter(readingRecordView ->  readingRecordView.getReadingRecordId() == null)
                .collect(Collectors.toCollection(ArrayList::new));
        for (BookUpdateView.ReadingRecordView recordToCreate: recordsToCreate) {

            BookStatusRecord bookStatusRecord = statusFactory.findById(Long.valueOf(recordToCreate.getStatusId()));

            ReadingRecord readingRecord = readingRecordsRepository.create(
                    bookId,
                    bookStatusRecord,
                    recordToCreate.getStartDate(),
                    recordToCreate.getEndDate(),
                    recordToCreate.getLastChapter()
            );

            readingRecords.add(readingRecord);
        }

        book.updateReadingRecords(readingRecords);

        // Update author
        logger.info("Updating text authors...");
        if (bookUpdateView.getAuthorId() != null) {
            Author author = authorFactory.findById(bookUpdateView.getAuthorId());
            book.updateTextAuthors(List.of(author));
        }

        // Update series
        logger.info("Updating series...");
        List<Series> seriesList = bookUpdateView.getSeriesIds().stream()
                // TODO: Should be single query
                .map( item -> seriesFactory.findById(user, item))
                .collect(Collectors.toCollection(ArrayList::new));
        book.updateSeries(seriesList);


        // Save book
        logger.info("Saving book...");
        bookRepository.save(book);
        logger.debug(String.format("Updated book: %s", book));

        return book;
    }

    /**
     * Get book
     */
    @Override
    @NonNull
    @Loggable(value = Loggable.DEBUG, trim = false, prepend = true)
    @Transactional(rollbackFor = Exception.class, readOnly = true)
    public Book getBook(@NonNull Long bookId, @NonNull Long userId) {

        // Find user book
        logger.info("Get book with id='{}' of user with id='{}'", bookId, userId);
        return bookFactory.getBook(bookId, userId);
    }

    /**
     * Get all book
     */
    @Override
    public List<Book> getAllBooks(Search search, Long userId) {
        List<Book> bookList;

        User user = userFactory.findById(userId);
        if (search.getChainBySeries()) {
            bookList = bookFactory.findAll(user, true);
        } else {
            bookList = bookFactory.findAll(user, false);
        }

        Stream<Book> bookStream = bookList.stream();
        for (Filter filter : search.filters()) {
            switch (filter.field()) {
                case "bookStatusIds" -> {
                    List<Long> statusIds = filter.values().stream()
                            .map(Long::valueOf)
                            .collect(Collectors.toCollection(ArrayList::new));
                    bookStream = bookStream
                            .filter(book -> {
                                for (Long statusId: statusIds) {
                                    if (book.currentStatusEquals(statusId)) {
                                        return true;
                                    }
                                }
                                return false;
                            });
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

    /**
     * Add book
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @NonNull
    @Loggable(value = Loggable.INFO, prepend = true, trim = false)
    public Book addBook(@NonNull BookAddView bookAddView, @NonNull Long userId) throws EmptyMandatoryParameterException, EntityNotFoundException {

        // Find status
        logger.info("Find status...");
        BookStatusRecord bookStatus = bookStatusesService.findById(bookAddView.status()).orElseThrow();

        // Find type
        logger.info("Find type...");
        BookType bookType = null;
        if (bookAddView.getBookTypeId() != null) {
            bookType = bookTypeRepository.findById(bookAddView.getBookTypeId().longValue());
        }

        // Find user
        logger.info("Find user...");
        User user = userFactory.findById(userId);

        // Create book
        logger.info("Create book...");
        Book newBook = bookFactory.createBook(
                bookAddView.getTitle(),
                bookAddView.getLastChapter(),
                bookAddView.note(),
                bookStatus,
                bookAddView.insertDate(),
                bookType,
                bookAddView.URL(),
                user
        );

        // Add author
        if (bookAddView.getAuthorId() != null) {
            logger.info("Add author...");
            authorsBooksRepository.add(
                    newBook.getId(),
                    authorFactory.findById(bookAddView.getAuthorId()).getId(),
                    user.userId(),
                    AuthorRole.TEXT_AUTHOR.getId()
            );
        }

        // Add reading record
        logger.info("Add reading record...");
        ReadingRecord readingRecord = readingRecordsRepository.create(
                newBook.getId(),
                bookStatus,
                bookAddView.insertDate(),
                null,
                bookAddView.getLastChapter() != null ? bookAddView.getLastChapter().longValue() : null
        );
        newBook.addReadingRecord(readingRecord);

        // Add series
        if (bookAddView.getSeriesId() != null) {
            logger.info("Add series...");
            Series series = seriesFactory.findById(user, bookAddView.getSeriesId());
            newBook.updateSeries(List.of(series));
        }

        // Save book
        logger.info("Saving book...");
        bookRepository.save(newBook);

        // Getting created book from DB
        logger.info("Loading book...");
        return getBook(newBook.getId(), user.userId());
    }

    /**
     * Delete book
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteBook(@NonNull Long bookId, @NonNull Long userId) throws EntityNotFoundException, EmptyMandatoryParameterException {
        Book book = bookFactory.getBook(bookId, userId);
        bookRepository.delete(book);
    }
}
