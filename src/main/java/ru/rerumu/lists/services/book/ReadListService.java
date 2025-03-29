package ru.rerumu.lists.services.book;

import com.jcabi.aspects.Loggable;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.rerumu.lists.controller.book.view.in.BookUpdateView;
import ru.rerumu.lists.crosscut.exception.EmptyMandatoryParameterException;
import ru.rerumu.lists.crosscut.exception.EntityNotFoundException;
import ru.rerumu.lists.crosscut.utils.DateFactory;
import ru.rerumu.lists.crosscut.utils.FuzzyMatchingService;
import ru.rerumu.lists.dao.book.BookRepository;
import ru.rerumu.lists.dao.repository.AuthorsBooksRepository;
import ru.rerumu.lists.dao.repository.SeriesBooksRespository;
import ru.rerumu.lists.model.Author;
import ru.rerumu.lists.model.AuthorBookRelation;
import ru.rerumu.lists.model.book.Book;
import ru.rerumu.lists.model.book.impl.BookFactoryImpl;
import ru.rerumu.lists.model.book.readingrecords.RecordDTO;
import ru.rerumu.lists.model.book.readingrecords.status.BookStatusRecord;
import ru.rerumu.lists.model.book.type.BookType;
import ru.rerumu.lists.model.books.Filter;
import ru.rerumu.lists.model.books.Search;
import ru.rerumu.lists.model.tag.Tag;
import ru.rerumu.lists.model.tag.TagFactory;
import ru.rerumu.lists.model.user.User;
import ru.rerumu.lists.model.user.UserFactory;
import ru.rerumu.lists.services.AuthorsBooksRelationService;
import ru.rerumu.lists.services.BookSeriesRelationService;
import ru.rerumu.lists.services.author.AuthorsService;
import ru.rerumu.lists.services.book.readingrecord.ReadingRecordService;
import ru.rerumu.lists.services.book.status.BookStatusesService;
import ru.rerumu.lists.services.book.type.BookTypesService;
import ru.rerumu.lists.controller.book.view.in.BookAddView;
import ru.rerumu.lists.views.ReadingRecordAddView;
import ru.rerumu.lists.views.ReadingRecordUpdateView;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
    private final BookFactoryImpl bookFactory;
    private final TagFactory tagFactory;
    private final UserFactory userFactory;

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
            ReadingRecordService readingRecordService,
            BookFactoryImpl bookFactory,
            TagFactory tagFactory,
            UserFactory userFactory
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
        this.tagFactory = tagFactory;
        this.userFactory = userFactory;
    }

    private void updateAuthor(long bookId, Long authorId, long readListId) {
        List<AuthorBookRelation> authorsBooksRepositoryList = authorsBooksRepository.getByBookId(bookId, readListId);

        Optional<Author> optionalAuthor = authorId != null ?
                authorsService.getAuthor(readListId, authorId) :
                Optional.empty();

        authorsBooksRepositoryList.stream()
                .filter(item -> item.getBook().getId().equals(bookId) &&
                        item.getBook().getListId().equals(readListId) &&
                        (optionalAuthor.isEmpty() || !optionalAuthor.get().equals(item.getAuthor()))
                )
                .forEach(item -> authorsBooksRelationService.delete(
                        item.getBook().getId(),
                        item.getAuthor().getAuthorId(),
                        item.getBook().getListId()
                ));

        if (authorsBooksRepositoryList.stream()
                .noneMatch(item -> optionalAuthor.isPresent() &&
                        item.getAuthor().equals(optionalAuthor.get()) &&
                        item.getBook().getId().equals(bookId) &&
                        item.getBook().getListId().equals(readListId))
        ) {
            optionalAuthor.ifPresent(author -> authorsBooksRepository.add(bookId, author.getAuthorId(), author.getReadListId()));
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Loggable(value = Loggable.DEBUG, trim = false, prepend = true)
    public void updateBook(Long bookId, BookUpdateView bookUpdateView) throws EmptyMandatoryParameterException, CloneNotSupportedException {
        if (
                bookUpdateView == null
                        || bookUpdateView.getReadListId() == null
                        || bookId == null
        ) {
            throw new EmptyMandatoryParameterException();
        }

        logger.info("Getting book with id='{}'...", bookId);
        Book book = bookFactory.getBook(bookId);

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

        // Save book
        logger.info("Saving book...");
        book.save();

        logger.debug(String.format("Updated book: %s", book));

        // Update author
        updateAuthor(bookId, bookUpdateView.getAuthorId(), bookUpdateView.getReadListId());
    }

    public Book getBook(Long bookId) throws EmptyMandatoryParameterException {
        Book book = bookFactory.getBook(bookId);
        logger.info(String.format("Got book '%s'", book.toString()));
        return book;
    }

    public List<Book> getAllBooks(Long readListId, Search search) {
        List<Book> bookList;
        if (search.getChainBySeries()) {
            bookList = bookFactory.getAllChained(readListId);
        } else {
            bookList = bookFactory.getAll(readListId);
        }

        Stream<Book> bookStream = bookList.stream();
        for (Filter filter : search.filters()) {
            switch (filter.field()) {
                case "bookStatusIds" -> {
                    List<Integer> statusIds = filter.values().stream()
                            .map(Integer::valueOf)
                            .collect(Collectors.toCollection(ArrayList::new));
                    bookStream = bookStream
                            .filter(book -> book.filterByStatusIds(statusIds));
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
    public void addBook(Long readListId, BookAddView bookAddView, User user) throws EmptyMandatoryParameterException, EntityNotFoundException {

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
                bookType,
                bookAddView.URL(),
                user
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
                null,
                bookAddView.getLastChapter() != null ? bookAddView.getLastChapter().longValue() : null
        );

        getBook(newBook.getId());
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteBook(Long bookId) throws EntityNotFoundException, EmptyMandatoryParameterException {

        Book book = bookFactory.getBook(bookId);

        seriesBooksRespository.getByBookId(
                        book.getId(),
                        book.getListId()
                )
                .forEach(item -> bookSeriesRelationService.delete(
                        item.book().getId(),
                        item.series().getSeriesId(),
                        item.book().getListId()
                ));

        authorsBooksRepository.getByBookId(
                        book.getId(),
                        book.getListId()
                )
                .forEach(item -> authorsBooksRelationService.delete(
                        item.getBook().getId(),
                        item.getAuthor().getAuthorId(),
                        item.getBook().getListId()
                ));

        bookRepository.delete(book.getId());
    }

    public Optional<User> getBookUser(Long bookId) {
        return bookRepository.getBookUser(bookId);
    }

    @Transactional(rollbackFor = Exception.class)
    public void addReadingRecord(@NonNull Long bookId, @NonNull ReadingRecordAddView readingRecordAddView) throws EmptyMandatoryParameterException {

        Book book = getBook(bookId);
        BookStatusRecord bookStatusRecord = bookStatusesService.findById(readingRecordAddView.statusId()).orElseThrow();

        book.addReadingRecord(
                bookStatusRecord,
                readingRecordAddView.startDate(),
                readingRecordAddView.endDate(),
                readingRecordAddView.lastChapter()
        );
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteReadingRecord(Long bookId, Long readingRecordId) throws EmptyMandatoryParameterException {
        Book book = getBook(bookId);
        book.deleteReadingRecord(readingRecordId);
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateReadingRecord(Long bookId, Long recordId, ReadingRecordUpdateView readingRecordUpdateView) throws EmptyMandatoryParameterException {
        Book book = getBook(bookId);
        BookStatusRecord bookStatusRecord = bookStatusesService.findById(readingRecordUpdateView.statusId()).orElseThrow();

        book.updateReadingRecord(
                recordId,
                bookStatusRecord,
                readingRecordUpdateView.startDate(),
                readingRecordUpdateView.endDate(),
                readingRecordUpdateView.lastChapter()
        );
    }
}
