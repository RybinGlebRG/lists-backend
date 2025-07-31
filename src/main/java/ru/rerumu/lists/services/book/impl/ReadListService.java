package ru.rerumu.lists.services.book.impl;

import com.jcabi.aspects.Loggable;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import ru.rerumu.lists.domain.author.Author;
import ru.rerumu.lists.domain.author.AuthorFactory;
import ru.rerumu.lists.domain.book.Book;
import ru.rerumu.lists.domain.book.impl.BookFactoryImpl;
import ru.rerumu.lists.domain.book.readingrecords.RecordDTO;
import ru.rerumu.lists.domain.book.readingrecords.status.BookStatusRecord;
import ru.rerumu.lists.domain.book.type.BookType;
import ru.rerumu.lists.domain.books.Filter;
import ru.rerumu.lists.domain.books.Search;
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
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ReadListService implements BookService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final BookRepository bookRepository;
    private final AuthorsBooksRepository authorsBooksRepository;
    private final BookTypesService bookTypesService;
    private final BookStatusesService bookStatusesService;
    private final FuzzyMatchingService fuzzyMatchingService;
    private final BookFactoryImpl bookFactory;
    private final TagFactory tagFactory;
    private final UserFactory userFactory;
    private final AuthorFactory authorFactory;
    private final SeriesFactory seriesFactory;

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
            SeriesFactory seriesFactory
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
    }

    /**
     * Update book
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @Loggable(value = Loggable.DEBUG, trim = false, prepend = true)
    public void updateBook(@NonNull Long bookId, @NonNull Long userId, @NonNull BookUpdateView bookUpdateView) {

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
            BookType optionalBookType = bookTypesService.findById(bookUpdateView.getBookTypeId()).orElseThrow(EntityNotFoundException::new);
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
        List<RecordDTO> records = bookUpdateView.getReadingRecords().stream()
                .map(readingRecordView -> new RecordDTO(
                        readingRecordView.getReadingRecordId(),
                        bookId,
                        Long.valueOf(readingRecordView.getStatusId()),
                        readingRecordView.getStartDate(),
                        readingRecordView.getEndDate(),
                        readingRecordView.getLastChapter()
                ))
                .collect(Collectors.toCollection(ArrayList::new));
        book.updateReadingRecords(records);

        // Update author
        logger.info("Updating text authors...");
        Author author = authorFactory.findById(bookUpdateView.getAuthorId());
        book.updateTextAuthors(List.of(author));

        // Update series
        logger.info("Updating series...");
        if (bookUpdateView.getSeriesId() != null) {
            Series series = seriesFactory.findById(user, bookUpdateView.getSeriesId());
            book.updateSeries(List.of(series));
        }

        // Save book
        logger.info("Saving book...");
        book.save();
        logger.debug(String.format("Updated book: %s", book));
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
            bookType = bookTypesService.findById(bookAddView.getBookTypeId()).orElseThrow();
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
        newBook.addReadingRecord(
                bookStatus,
                bookAddView.insertDate(),
                null,
                bookAddView.getLastChapter() != null ? bookAddView.getLastChapter().longValue() : null
        );

        // Add series
        if (bookAddView.getSeriesId() != null) {
            logger.info("Add series...");
            Series series = seriesFactory.findById(user, bookAddView.getSeriesId());
            newBook.updateSeries(List.of(series));
        }

        // Save book
        logger.info("Saving book...");
        newBook.save();

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
        book.delete();
    }

    public Optional<User> getBookUser(Long bookId) {
        return bookRepository.getBookUser(bookId);
    }
}
