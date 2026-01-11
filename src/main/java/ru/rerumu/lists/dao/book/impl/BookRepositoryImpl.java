package ru.rerumu.lists.dao.book.impl;

import com.jcabi.aspects.Loggable;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.rerumu.lists.crosscut.exception.EntityNotFoundException;
import ru.rerumu.lists.dao.author.AuthorDtoDao;
import ru.rerumu.lists.dao.author.AuthorsRepository;
import ru.rerumu.lists.dao.base.EntityState;
import ru.rerumu.lists.dao.book.AuthorBookDto;
import ru.rerumu.lists.dao.book.AuthorRole;
import ru.rerumu.lists.dao.book.AuthorsBooksRepository;
import ru.rerumu.lists.dao.book.BookMyBatisEntity;
import ru.rerumu.lists.dao.book.BookRepository;
import ru.rerumu.lists.dao.book.mapper.BookMapper;
import ru.rerumu.lists.dao.readingrecord.ReadingRecordsRepository;
import ru.rerumu.lists.dao.series.SeriesBooksRepository;
import ru.rerumu.lists.dao.series.SeriesItemRelationDTO;
import ru.rerumu.lists.dao.series.SeriesMyBatisEntity;
import ru.rerumu.lists.dao.series.SeriesRepository;
import ru.rerumu.lists.dao.series.impl.SeriesBookRelationDto;
import ru.rerumu.lists.dao.tag.TagsRepository;
import ru.rerumu.lists.dao.user.UsersRepository;
import ru.rerumu.lists.domain.author.Author;
import ru.rerumu.lists.domain.book.Book;
import ru.rerumu.lists.domain.book.BookChain;
import ru.rerumu.lists.domain.book.BookFactory;
import ru.rerumu.lists.domain.booktype.BookType;
import ru.rerumu.lists.domain.readingrecord.ReadingRecord;
import ru.rerumu.lists.domain.readingrecordstatus.ReadingRecordStatuses;
import ru.rerumu.lists.domain.series.Series;
import ru.rerumu.lists.domain.series.SeriesFactory;
import ru.rerumu.lists.domain.tag.Tag;
import ru.rerumu.lists.domain.user.User;

import java.time.LocalDateTime;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


@Slf4j
@Component
public class BookRepositoryImpl implements BookRepository {

    private final BookMapper bookMapper;
    private final AuthorsBooksRepository authorsBooksRepository;
    private final SeriesBooksRepository seriesBooksRepository;
    private final SeriesRepository seriesRepository;
    private final ReadingRecordsRepository readingRecordsRepository;
    private final BookFactory bookFactory;
    private final TagsRepository tagsRepository;
    private final AuthorsRepository authorsRepository;
    private final SeriesFactory seriesFactory;
    private final UsersRepository usersRepository;

    @Autowired
    public BookRepositoryImpl(
            BookMapper bookMapper,
            AuthorsBooksRepository authorsBooksRepository,
            SeriesBooksRepository seriesBooksRepository,
            SeriesRepository seriesRepository,
            ReadingRecordsRepository readingRecordsRepository,
            BookFactory bookFactory,
            TagsRepository tagsRepository,
            AuthorsRepository authorsRepository,
            SeriesFactory seriesFactory,
            UsersRepository usersRepository
    ) {
        this.bookMapper = bookMapper;
        this.authorsBooksRepository = authorsBooksRepository;
        this.seriesBooksRepository = seriesBooksRepository;
        this.seriesRepository = seriesRepository;
        this.readingRecordsRepository = readingRecordsRepository;
        this.bookFactory = bookFactory;
        this.tagsRepository = tagsRepository;
        this.authorsRepository = authorsRepository;
        this.seriesFactory = seriesFactory;
        this.usersRepository = usersRepository;
    }


    @Override
    public void update(Book book) {

        bookMapper.update(
                null,
                book.getId(),
                book.getTitle(),
                null,
                book.getInsertDate(),
                null,
                null,
                book.getBookType() != null ? book.getBookType().getId() : null,
                book.getNote(),
                book.getURL(),
                book.getUser().getId()
        );
    }

    /**
     * Find books by user and chain then by series. Last book in series is the head of a chain.
     */
    @Override
    @Loggable(value = Loggable.DEBUG, trim = false, prepend = true)
    public List<Book> findByUserChained(User user) {

        // Load all books by user
        List<BookMyBatisEntity> bookDtoList = bookMapper.findByUserChained(user.getId());

        // Load all relations between books and authors by user
        List<AuthorBookDto> authorBookDtoList = authorsBooksRepository.getAllByUserId(user.getId());
        Map<Long, List<AuthorBookDto>> authorsMap = authorBookDtoList.stream()
                .collect(Collectors.groupingBy(
                        AuthorBookDto::getBookId,
                        HashMap::new,
                        Collectors.toCollection(ArrayList::new)
                ));

        // Add authors to corresponding books
        for (BookMyBatisEntity item: bookDtoList) {
            List<AuthorBookDto> dtoList = authorsMap.get(item.getBookId());

            List<AuthorDtoDao> authorDtoDaoList = new ArrayList<>();
            if (dtoList != null) {
                authorDtoDaoList = dtoList.stream()
                        .map(AuthorBookDto::getAuthorDtoDao)
                        .collect(Collectors.toCollection(ArrayList::new));
            }
            item.setTextAuthors(authorDtoDaoList);
        }

        // Loading all series
        List<SeriesMyBatisEntity> seriesMyBatisEntities = seriesRepository.findByUser(user).stream()
                .map(SeriesMyBatisEntity::fromDomain)
                .collect(Collectors.toCollection(ArrayList::new));
        for (BookMyBatisEntity bookMybatisEntity : bookDtoList) {

            // Getting list of series that contain book
            List<SeriesMyBatisEntity> containsBook = new ArrayList<>();
            for (SeriesMyBatisEntity seriesMyBatisEntity: seriesMyBatisEntities) {
                for (SeriesBookRelationDto seriesBookRelationDto: seriesMyBatisEntity.getSeriesBookRelationDtoList()) {
                    if (seriesBookRelationDto.getBookId().equals(bookMybatisEntity.getBookId())) {
                        containsBook.add(seriesMyBatisEntity);
                        break;
                    }
                }
            }

            bookMybatisEntity.setSeriesList(containsBook);
        }

        List<Book> books = new ArrayList<>();
        for (BookMyBatisEntity bookMyBatisEntity: bookDtoList) {
            Book book = attach(bookMyBatisEntity);
            books.add(new BookPersistenceProxy(book, EntityState.PERSISTED));
        }

        return books;
    }

    @Override
    @Loggable(value = Loggable.DEBUG, trim = false, prepend = true)
    @NonNull
    public Book findById(Long id, Long userId) {
        BookMyBatisEntity book = bookMapper.findById(id, userId);
        if (book == null) {
            throw new EntityNotFoundException();
        }

        List<Long> seriesIds= bookMapper.findSeriesIds(book.getBookId());
        book.setSeriesIds(seriesIds);

        if (!book.getSeriesIds().isEmpty()) {
            List<SeriesMyBatisEntity> seriesDTOList = seriesRepository.findByIds(book.getSeriesIds(), userId).stream()
                    .map(SeriesMyBatisEntity::fromDomain)
                    .collect(Collectors.toCollection(ArrayList::new));
            book.setSeriesList(seriesDTOList);
        }

        List<AuthorDtoDao> authorsDTOs = authorsBooksRepository.getAuthorsByBookId(id);
        book.setTextAuthors(authorsDTOs);

        Book result = attach(book);
        return new BookPersistenceProxy(result, EntityState.PERSISTED);
    }

    @Override
    @Loggable(value = Loggable.DEBUG, trim = false, prepend = true)
    public List<Book> findByUser(User user) {

        List<BookMyBatisEntity> bookMyBatisEntities = bookMapper.findByUser(user);

        // Load all relations between books and authors by user
        List<AuthorBookDto> authorBookDtoList = authorsBooksRepository.getAllByUserId(user.getId());
        Map<Long, List<AuthorBookDto>> authorsMap = authorBookDtoList.stream()
                .collect(Collectors.groupingBy(
                        AuthorBookDto::getBookId,
                        HashMap::new,
                        Collectors.toCollection(ArrayList::new)
                ));

        // Add authors to corresponding books
        for (BookMyBatisEntity item: bookMyBatisEntities) {
            List<AuthorBookDto> dtoList = authorsMap.get(item.getBookId());

            List<AuthorDtoDao> authorDtoDaoList = new ArrayList<>();
            if (dtoList != null) {
                authorDtoDaoList = dtoList.stream()
                        .map(AuthorBookDto::getAuthorDtoDao)
                        .collect(Collectors.toCollection(ArrayList::new));
            }
            item.setTextAuthors(authorDtoDaoList);
        }

        // Loading all series
        List<SeriesMyBatisEntity> seriesMyBatisEntities = seriesRepository.findByUser(user).stream()
                .map(SeriesMyBatisEntity::fromDomain)
                .collect(Collectors.toCollection(ArrayList::new));
        for (BookMyBatisEntity bookMybatisEntity : bookMyBatisEntities) {

            // Getting list of series that contain book
            List<SeriesMyBatisEntity> containsBook = new ArrayList<>();
            for (SeriesMyBatisEntity seriesMyBatisEntity: seriesMyBatisEntities) {
                for (SeriesBookRelationDto seriesBookRelationDto: seriesMyBatisEntity.getSeriesBookRelationDtoList()) {
                    if (seriesBookRelationDto.getBookId().equals(bookMybatisEntity.getBookId())) {
                        containsBook.add(seriesMyBatisEntity);
                        break;
                    }
                }
            }

            bookMybatisEntity.setSeriesList(containsBook);
        }

        List<Book> books = new ArrayList<>();
        for (BookMyBatisEntity bookMyBatisEntity: bookMyBatisEntities) {
            Book book = attach(bookMyBatisEntity);
            books.add(new BookPersistenceProxy(book, EntityState.PERSISTED));
        }

        return books;

    }

    @Override
    public Long getNextId() {
        return bookMapper.getNextId();
    }

    @Override
    public void addOne(Book book) {

        bookMapper.addOne(
                book.getId(),
                null,
                book.getTitle(),
                null,
                book.getInsertDate(),
                null,
                null,
                book.getBookType() != null ? book.getBookType().getId() : null,
                book.getNote(),
                book.getURL(),
                book.getUser().getId()
        );
    }

    @Override
    public void delete(Long bookId, User user) {
        bookMapper.delete(bookId, user.getId());
    }

    @Override
    public Optional<User> getBookUser(Long bookId) {
        return Optional.ofNullable(bookMapper.getBookUser(bookId));
    }

    @Override
    public void save(Book book) {
        BookPersistenceProxy bookPersistenceProxy = (BookPersistenceProxy) book;

        // Save book
        log.debug("Saving book...");
        update(book);

        // Save series
        log.debug("Saving series...");
        for (Series series: bookPersistenceProxy.getAllSeries()) {
            seriesRepository.save(series);
        }

        log.debug("Saving records...");
        // Delete removed reading records
        for (ReadingRecord readingRecord: bookPersistenceProxy.getRemovedReadingRecords()) {
            readingRecordsRepository.delete(readingRecord);
        }
        // Save reading records
        for (ReadingRecord readingRecord: book.getReadingRecords()) {
            readingRecordsRepository.update(readingRecord);
        }

        log.debug("Saving tags...");
        // Removing tags
        for (Tag tag: bookPersistenceProxy.getRemovedTags()) {
            tagsRepository.remove(book.getId(), tag.getId());
        }
        // Adding new tags
        for (Tag tag: bookPersistenceProxy.getAddedTags()) {
            tagsRepository.addTagTo(tag, book);
        }

        log.debug("Saving authors...");
        // Remove existing authors
        for (Author author: bookPersistenceProxy.getRemovedAuthors()) {
            authorsBooksRepository.deleteByAuthor(author.getId());
        }
        // Add new authors
        for (Author author: bookPersistenceProxy.getAddedAuthors()) {
            authorsBooksRepository.addAuthorTo(author, book, AuthorRole.TEXT_AUTHOR);
        }

        // Mark as saved
        bookPersistenceProxy.setEntityState(EntityState.PERSISTED);
        bookPersistenceProxy.initPersistedCopy();

    }

    // TODO: Not deleting???
    @Override
    public void delete(Book book) {

        // TODO: Check
        Long bookId = book.getId();
        authorsBooksRepository.getAuthorsByBookId(bookId)
                .forEach(authorDtoDao -> authorsBooksRepository.delete(
                        bookId,
                        authorDtoDao.getAuthorId()
                ));

        // TODO: Check
        for (Series series: book.getSeriesList()) {
            series.removeBookRelation(bookId);
        }

        // Delete reading records
        List<ReadingRecord> readingRecords = book.getReadingRecords();
        for (ReadingRecord readingRecord: readingRecords) {
            readingRecordsRepository.delete(readingRecord);
        }

        // Delete relations with tags
        book.updateTags(new ArrayList<>());

        // Delete relations with series
        book.updateSeries(new ArrayList<>());

        // Delete relations with authors
        book.updateTextAuthors(new ArrayList<>());

        // Save changes
        save(book);

        // Delete book
        bookMapper.delete(book.getId(), book.getUser().getId());
    }

    @Override
    public Book create(
            String title,
            Integer lastChapter,
            String note,
            ReadingRecordStatuses bookStatus,
            LocalDateTime insertDate,
            BookType bookType,
            String URL,
            User user
    ) {
        Long bookId = getNextId();

        Book book = bookFactory.build(
                bookId,
                title,
                insertDate,
                bookType,
                null,
                note,
                null,
                URL,
                user,
                null,
                null,
                null
        );

        addOne(book);

        BookPersistenceProxy bookPersistenceProxy = new BookPersistenceProxy(book, EntityState.PERSISTED);
        bookPersistenceProxy.initPersistedCopy();

        return bookPersistenceProxy;
    }

    @Override
    public @NonNull Book attach(@NonNull BookMyBatisEntity bookMyBatisEntity) {

        // Get authors
        List<Author> authors;
        if (bookMyBatisEntity.getTextAuthors() != null) {
            authors = bookMyBatisEntity.getTextAuthors().stream()
                    .map(authorsRepository::fromDTO)
                    .collect(Collectors.toCollection(ArrayList::new));
        } else {
            authors = new ArrayList<>();
        }

        // Get book type
        BookType bookType;
        if (bookMyBatisEntity.getBookTypeObj() != null) {
            bookType = bookMyBatisEntity.getBookTypeObj();
        } else {
            bookType = null;
        }

        // Get reading records
        List<ReadingRecord> readingRecords;
        if (bookMyBatisEntity.getReadingRecords() != null) {
            readingRecords = bookMyBatisEntity.getReadingRecords().stream()
                    .map(readingRecordsRepository::attach)
                    .collect(Collectors.toCollection(ArrayList::new));
        } else {
            readingRecords = new ArrayList<>();
        }

        // Get previous books
        BookChain bookChain;
        if (bookMyBatisEntity.getPreviousBooks() != null) {

            HashMap<Book, Integer> bookOrderMap = bookMyBatisEntity.getPreviousBooks().stream()
                    .filter(Objects::nonNull)
                    .map(item -> new AbstractMap.SimpleImmutableEntry<>(
                            attach(item.getBookDTO()),
                            item.getOrder()
                    ))
                    .collect(
                            HashMap::new,
                            (map, item) -> map.put(item.getKey(), item.getValue()),
                            HashMap::putAll
                    );

            bookChain = new BookChain(bookOrderMap);
        } else {
            bookChain = null;
        }

        // Get tags
        List<Tag> tags;
        if (bookMyBatisEntity.getTags() != null) {
            tags = bookMyBatisEntity.getTags().stream()
                    .map(tagsRepository::attach)
                    .collect(Collectors.toCollection(ArrayList::new));
        } else {
            tags = new ArrayList<>();
        }

        // Get series list
        List<Series> seriesList;
        if (bookMyBatisEntity.getSeriesList() != null && !bookMyBatisEntity.getSeriesList().isEmpty()) {

            List<SeriesMyBatisEntity> seriesDTOList = bookMyBatisEntity.getSeriesList();

            // Converting Series DTO to domain entity
            seriesList = new ArrayList<>();
            for (SeriesMyBatisEntity seriesMyBatisEntity: seriesDTOList) {

                // Getting list of relations to book for each series
                List<SeriesItemRelationDTO> seriesItemRelationDTOList = seriesMyBatisEntity.getSeriesBookRelationDtoList().stream()
                        .map(item -> (SeriesItemRelationDTO)item)
                        .sorted(Comparator.comparingLong(SeriesItemRelationDTO::getOrder))
                        .collect(Collectors.toCollection(ArrayList::new));

                Series series = seriesFactory.buildSeries(
                        seriesMyBatisEntity.getSeriesId(),
                        seriesMyBatisEntity.getTitle(),
                        usersRepository.findById(seriesMyBatisEntity.getUserId()),
                        seriesBooksRepository.fromDTO(seriesItemRelationDTOList)
                );

                series = seriesRepository.attach(series);

                seriesList.add(series);
            }
        } else {
            seriesList = new ArrayList<>();
        }

        User user = usersRepository.findById(bookMyBatisEntity.getUser().getUserId());

        // Create book
        return bookFactory.build(
                bookMyBatisEntity.getBookId(),
                bookMyBatisEntity.getTitle(),
                bookMyBatisEntity.getInsertDate(),
                bookType,
                bookChain,
                bookMyBatisEntity.getNote(),
                readingRecords,
                bookMyBatisEntity.getURL(),
                user,
                tags,
                authors,
                seriesList
        );
    }
}
