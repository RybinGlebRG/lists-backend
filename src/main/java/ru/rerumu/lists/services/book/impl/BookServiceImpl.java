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
import ru.rerumu.lists.dao.author.AuthorsRepository;
import ru.rerumu.lists.dao.book.AuthorRole;
import ru.rerumu.lists.dao.book.AuthorsBooksRepository;
import ru.rerumu.lists.dao.book.BookRepository;
import ru.rerumu.lists.dao.booktype.BookTypeRepository;
import ru.rerumu.lists.dao.readingrecord.ReadingRecordsRepository;
import ru.rerumu.lists.dao.readingrecordstatus.ReadingRecordStatusRepository;
import ru.rerumu.lists.dao.series.SeriesRepository;
import ru.rerumu.lists.dao.tag.TagsRepository;
import ru.rerumu.lists.dao.user.UsersRepository;
import ru.rerumu.lists.domain.author.Author;
import ru.rerumu.lists.domain.book.Book;
import ru.rerumu.lists.domain.booktype.BookType;
import ru.rerumu.lists.domain.readingrecord.ReadingRecord;
import ru.rerumu.lists.domain.readingrecordstatus.ReadingRecordStatuses;
import ru.rerumu.lists.domain.series.Series;
import ru.rerumu.lists.domain.tag.Tag;
import ru.rerumu.lists.domain.user.User;
import ru.rerumu.lists.services.book.BookService;
import ru.rerumu.lists.services.book.Filter;
import ru.rerumu.lists.services.book.Search;
import ru.rerumu.lists.services.booktype.BookTypesService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component("BookServiceImpl")
public class BookServiceImpl implements BookService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final BookRepository bookRepository;
    private final AuthorsBooksRepository authorsBooksRepository;
    private final BookTypesService bookTypesService;
    private final FuzzyMatchingService fuzzyMatchingService;
    private final BookTypeRepository bookTypeRepository;
    private final ReadingRecordsRepository readingRecordsRepository;
    private final SeriesRepository seriesRepository;
    private final UsersRepository usersRepository;
    private final AuthorsRepository authorsRepository;
    private final ReadingRecordStatusRepository readingRecordStatusRepository;
    private final TagsRepository tagsRepository;

    @Autowired
    public BookServiceImpl(
            BookRepository bookRepository,
            AuthorsBooksRepository authorsBooksRepository,
            BookTypesService bookTypesService,
            FuzzyMatchingService fuzzyMatchingService,
            BookTypeRepository bookTypeRepository,
            ReadingRecordsRepository readingRecordsRepository,
            SeriesRepository seriesRepository,
            UsersRepository usersRepository,
            AuthorsRepository authorsRepository,
            ReadingRecordStatusRepository readingRecordStatusRepository,
            TagsRepository tagsRepository
    ) {
        this.bookRepository = bookRepository;
        this.authorsBooksRepository = authorsBooksRepository;
        this.bookTypesService = bookTypesService;
        this.fuzzyMatchingService = fuzzyMatchingService;
        this.bookTypeRepository = bookTypeRepository;
        this.readingRecordsRepository = readingRecordsRepository;
        this.seriesRepository = seriesRepository;
        this.usersRepository = usersRepository;
        this.authorsRepository = authorsRepository;
        this.readingRecordStatusRepository = readingRecordStatusRepository;
        this.tagsRepository = tagsRepository;
    }

    /**
     * Update book
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @Loggable(value = Loggable.DEBUG, trim = false, prepend = true)
    public Book updateBook(@NonNull Long bookId, @NonNull Long userId, @NonNull BookUpdateView bookUpdateView) {

        logger.info("Getting user with id='{}'...", userId);
        User user = usersRepository.findById(userId);

        logger.info("Getting book with id='{}'...", bookId);
        Book book = bookRepository.findById(bookId, userId);

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
        List<Tag> tags = tagsRepository.findByIds(bookUpdateView.getTagIds(), book.getUser());
        book.updateTags(tags);

        // Update reading records
        logger.info("Updating reading records...");
        Map<Long, BookUpdateView.ReadingRecordView> id2readingRecordViewMap =  bookUpdateView.getReadingRecords().stream()
                .filter(readingRecordView -> readingRecordView.getReadingRecordId() != null)
                .collect(Collectors.toMap(
                        BookUpdateView.ReadingRecordView::getReadingRecordId,
                        Function.identity(),
                        (o1, o2) -> o2,
                        HashMap::new
                ));
        // Remove or update reading records
        for (ReadingRecord readingRecord: book.getReadingRecords()) {
            if (!id2readingRecordViewMap.containsKey(readingRecord.getId())) {
                book.removeReadingRecord(readingRecord);
            } else {
                BookUpdateView.ReadingRecordView readingRecordView = id2readingRecordViewMap.get(readingRecord.getId());
                readingRecord.update(
                        readingRecordStatusRepository.findById(readingRecordView.getStatusId()),
                        readingRecordView.getStartDate(),
                        readingRecordView.getEndDate(),
                        readingRecordView.getLastChapter()
                );
            }
        }
        // Add reading record
        bookUpdateView.getReadingRecords().stream()
                .filter(readingRecordView -> readingRecordView.getReadingRecordId() == null)
                .map(readingRecordView -> readingRecordsRepository.create(
                        bookId,
                        readingRecordStatusRepository.findById(readingRecordView.getStatusId()),
                        readingRecordView.getStartDate(),
                        readingRecordView.getEndDate(),
                        readingRecordView.getLastChapter()
                ))
                .forEachOrdered(book::addReadingRecord);

        // Update author
        logger.info("Updating text authors...");
        if (bookUpdateView.getAuthorId() != null) {
            Author author = authorsRepository.findById(bookUpdateView.getAuthorId(), user);
            book.updateTextAuthors(List.of(author));
        }

        // Update series
        logger.info("Updating series...");
        List<Series> seriesList = bookUpdateView.getSeriesIds().stream()
                // TODO: Should be single query
                .map( item -> seriesRepository.findById(item, user))
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
        return bookRepository.findById(bookId, userId);
    }

    /**
     * Get all book
     */
    @Override
    public List<Book> getAllBooks(Search search, Long userId) {
        List<Book> bookList;

        User user = usersRepository.findById(userId);
        if (search.getChainBySeries()) {
            bookList = bookRepository.findByUserChained(user);
        } else {
            bookList = bookRepository.findByUser(user);
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
        ReadingRecordStatuses bookStatus = readingRecordStatusRepository.findById((long) bookAddView.status());

        // Find type
        logger.info("Find type...");
        BookType bookType = null;
        if (bookAddView.getBookTypeId() != null) {
            bookType = bookTypeRepository.findById(bookAddView.getBookTypeId().longValue());
        }

        // Find user
        logger.info("Find user...");
        User user = usersRepository.findById(userId);

        // Create book
        logger.info("Create book...");
        Book newBook = bookRepository.create(
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
            authorsBooksRepository.addAuthorTo(
                    newBook.getId(),
                    authorsRepository.findById(bookAddView.getAuthorId(), user).getId(),
                    user.getId(),
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
            Series series = seriesRepository.findById(bookAddView.getSeriesId(), user);
            newBook.updateSeries(List.of(series));
        }

        // Save book
        logger.info("Saving book...");
        bookRepository.save(newBook);

        // Getting created book from DB
        logger.info("Loading book...");
        return bookRepository.findById(newBook.getId(), user.getId());
    }

    /**
     * Delete book
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteBook(@NonNull Long bookId, @NonNull Long userId) throws EntityNotFoundException, EmptyMandatoryParameterException {
        Book book = bookRepository.findById(bookId, userId);
        bookRepository.delete(book);
    }
}
