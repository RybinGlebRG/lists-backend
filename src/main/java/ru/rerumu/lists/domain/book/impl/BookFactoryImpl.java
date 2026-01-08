package ru.rerumu.lists.domain.book.impl;

import com.jcabi.aspects.Loggable;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.rerumu.lists.crosscut.exception.EmptyMandatoryParameterException;
import ru.rerumu.lists.crosscut.utils.DateFactory;
import ru.rerumu.lists.dao.book.AuthorsBooksRepository;
import ru.rerumu.lists.dao.book.BookMyBatisEntity;
import ru.rerumu.lists.dao.book.BookRepository;
import ru.rerumu.lists.dao.readingrecord.ReadingRecordsRepository;
import ru.rerumu.lists.dao.series.SeriesMyBatisEntity;
import ru.rerumu.lists.domain.BookChain;
import ru.rerumu.lists.domain.author.AuthorFactory;
import ru.rerumu.lists.domain.base.EntityState;
import ru.rerumu.lists.domain.book.Book;
import ru.rerumu.lists.domain.book.BookDTO;
import ru.rerumu.lists.domain.book.BookFactory;
import ru.rerumu.lists.domain.bookstatus.BookStatusRecord;
import ru.rerumu.lists.domain.bookstatus.StatusFactory;
import ru.rerumu.lists.domain.booktype.BookType;
import ru.rerumu.lists.domain.booktype.BookTypeFactory;
import ru.rerumu.lists.domain.dto.BookOrderedDTO;
import ru.rerumu.lists.domain.readingrecords.ReadingRecord;
import ru.rerumu.lists.domain.readingrecords.impl.ReadingRecordFactory;
import ru.rerumu.lists.domain.series.Series;
import ru.rerumu.lists.domain.series.SeriesFactory;
import ru.rerumu.lists.domain.series.SeriesItemRelationDTO;
import ru.rerumu.lists.domain.series.SeriesItemRelationFactory;
import ru.rerumu.lists.domain.tag.Tag;
import ru.rerumu.lists.domain.tag.TagFactory;
import ru.rerumu.lists.domain.user.User;
import ru.rerumu.lists.domain.user.UserFactory;

import java.time.LocalDateTime;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Component
public class BookFactoryImpl implements BookFactory {

    private final DateFactory dateFactory;
    private final BookRepository bookRepository;
    private final ReadingRecordFactory readingRecordFactory;
    private final BookTypeFactory bookTypeFactory;
    private final UserFactory userFactory;
    private final TagFactory tagFactory;
    private final StatusFactory statusFactory;
    private final AuthorsBooksRepository authorsBooksRepository;
    private final AuthorFactory authorFactory;
    private final SeriesFactory seriesFactory;
    private final SeriesItemRelationFactory seriesItemRelationFactory;
    private final ReadingRecordsRepository readingRecordsRepository;

    @Autowired
    public BookFactoryImpl(
            DateFactory dateFactory,
            BookRepository bookRepository,
            ReadingRecordFactory readingRecordFactory,
            BookTypeFactory bookTypeFactory,
            UserFactory userFactory,
            TagFactory tagFactory,
            StatusFactory statusFactory,
            AuthorsBooksRepository authorsBooksRepository,
            @NonNull AuthorFactory authorFactory,
            @NonNull SeriesFactory seriesFactory,
            @NonNull SeriesItemRelationFactory seriesItemRelationFactory, ReadingRecordsRepository readingRecordsRepository
    ) {
        this.dateFactory = dateFactory;
        this.bookRepository = bookRepository;
        this.readingRecordFactory = readingRecordFactory;
        this.bookTypeFactory = bookTypeFactory;
        this.userFactory = userFactory;
        this.tagFactory = tagFactory;
        this.statusFactory = statusFactory;
        this.authorsBooksRepository = authorsBooksRepository;
        this.authorFactory = authorFactory;
        this.seriesFactory = seriesFactory;
        this.seriesItemRelationFactory = seriesItemRelationFactory;
        this.readingRecordsRepository = readingRecordsRepository;
    }

    public Book createBook(
            String title,
            Integer lastChapter,
            String note,
            BookStatusRecord bookStatus,
            LocalDateTime insertDate,
            BookType bookType,
            String URL,
            User user
    ) throws EmptyMandatoryParameterException {

        Long bookId = bookRepository.getNextId();
        BookBuilder bookBuilder = new BookBuilder(
                statusFactory,
                dateFactory,
                readingRecordFactory,
                bookRepository,
                authorsBooksRepository,
                authorFactory,
                seriesFactory
        )
                .bookId(bookId)
                .title(title)
                .lastChapter(lastChapter)
                .note(note)
                .bookStatus(bookStatus)
                .URL(URL)
                .user(user)
                .tags(new ArrayList<>());

        if (insertDate != null) {
            bookBuilder.insertDate(insertDate);
            bookBuilder.lastUpdateDate(insertDate);
        } else {
            LocalDateTime tmp = dateFactory.getLocalDateTime();
            bookBuilder.insertDate(tmp);
            bookBuilder.lastUpdateDate(tmp);
        }

        if (bookType != null) {
            bookBuilder.bookType(bookType);
        }

        BookImpl book = bookBuilder.build();

        bookRepository.addOne(book);

        BookPersistenceProxy bookPersistenceProxy = new BookPersistenceProxy(book, EntityState.NEW);
        bookPersistenceProxy.initPersistedCopy();

        return bookPersistenceProxy;
    }

    public Book fromDTO(@NonNull BookDTO bookDTO) {
        return fromDTOOld(List.of(bookDTO)).get(0);
    }

    @Override
//    public Book fromDTO(@NonNull BookDtoDao bookDTO) {
//        return fromDTO(List.of(bookDTO), true).get(0);
//    }

    public List<Book> fromDTOOld(@NonNull List<BookDTO> bookDTOList) {
        // Preparing reading records
        Map<Long, List<ReadingRecord>> bookId2ReadingRecordsMap = readingRecordsRepository.findByBookIds(
                        // Collecting bookIds from chain
                        bookDTOList.stream()
                                // flatten book chain
                                .flatMap(bookDTO -> {
                                    List<BookDTO> tmp = new ArrayList<>();
                                    tmp.add(bookDTO);

                                    if (bookDTO.getPreviousBooks() != null) {
                                        tmp.addAll(
                                                bookDTO.getPreviousBooks().stream()
                                                        .map(BookOrderedDTO::getBookDTO)
                                                        .collect(Collectors.toCollection(ArrayList::new))
                                        );
                                    }

                                    return tmp.stream();
                                })
                                .map(BookDTO::getBookId)
                                .collect(Collectors.toCollection(ArrayList::new))
                ).stream()
                .collect(
                        Collectors.groupingBy(
                                ReadingRecord::getBookId,
                                HashMap::new,
                                Collectors.toCollection(ArrayList::new)
                        )
                );

        // Prepare users
        List<Long> userIdsToFind = bookDTOList.stream()
                .map(item -> item.userId)
                .distinct()
                .collect(Collectors.toCollection(ArrayList::new));
        Map<Long, User> userId2UserMap = userFactory.findByIds(userIdsToFind).stream()
                .collect(Collectors.toMap(
                        User::userId,
                        Function.identity(),
                        (o1, o2) -> o2,
                        HashMap::new
                ));


        return bookDTOList.stream()
                .map(bookDTO -> fromDTO(bookDTO, bookId2ReadingRecordsMap,userId2UserMap))
                .collect(Collectors.toCollection(ArrayList::new));

    }

    @Override
    public List<Book> fromDTO(@NonNull List<BookMyBatisEntity> bookDTOList) {
        return bookDTOList.stream()
                .map(this::fromDTO)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private Book fromDTO(
            @NonNull BookDTO bookDTO,
            @NonNull Map<Long, List<ReadingRecord>> bookId2ReadingRecordsMap,
            @NonNull Map<Long, User> userId2UserMap
    ) throws EmptyMandatoryParameterException {

        log.debug("bookDTO: {}", bookDTO);

        BookBuilder builder = new BookBuilder(statusFactory, dateFactory, readingRecordFactory, bookRepository, authorsBooksRepository, authorFactory, seriesFactory)
                .bookId(bookDTO.bookId)
                .title(bookDTO.title)
                .bookStatus(bookDTO.bookStatusObj)
                .insertDate(bookDTO.insertDate)
                .lastUpdateDate(bookDTO.lastUpdateDate)
                .lastChapter(bookDTO.lastChapter)
                .note(bookDTO.note)
                .URL(bookDTO.URL)
                .user(userId2UserMap.get(bookDTO.userId));

        if (bookDTO.bookTypeObj != null) {
            builder.bookType(bookDTO.bookTypeObj.toDomain());
        }

        if (bookId2ReadingRecordsMap.get(bookDTO.bookId) != null) {
            builder.readingRecords(bookId2ReadingRecordsMap.get(bookDTO.bookId));
        }

        if (bookDTO.previousBooks != null) {

            HashMap<Book, Integer> bookOrderMap = bookDTO.previousBooks.stream()
                    .filter(Objects::nonNull)
                    .map(item -> new AbstractMap.SimpleImmutableEntry<>(
                            fromDTO(item.getBookDTO(), bookId2ReadingRecordsMap, userId2UserMap),
                            item.getOrder()
                    ))
                    .collect(
                            HashMap::new,
                            (map, item) -> map.put(item.getKey(), item.getValue()),
                            HashMap::putAll
                    );

            builder.previousBooks(
                    new BookChain(bookOrderMap)
            );
        }

        // Set Tags
        List<Tag> tags;
        if (bookDTO.tags != null) {
            tags = bookDTO.tags.stream()
                    .map(tagFactory::fromDTO)
                    .collect(Collectors.toCollection(ArrayList::new));
        } else {
            tags = new ArrayList<>();
        }
        builder.tags(tags);

        BookImpl book = builder.build();

        BookPersistenceProxy bookPersistenceProxy = new BookPersistenceProxy(book, EntityState.PERSISTED);
        bookPersistenceProxy.initPersistedCopy();

        return bookPersistenceProxy;
    }

    @Override
    @Loggable(value = Loggable.TRACE, trim = false, prepend = true)
    @NonNull
    public Book fromDTO(@NonNull BookMyBatisEntity bookMyBatisEntity) throws EmptyMandatoryParameterException {

        BookBuilder builder = new BookBuilder(
                statusFactory,
                dateFactory,
                readingRecordFactory,
                bookRepository,
                authorsBooksRepository,
                authorFactory,
                seriesFactory
        )
                .bookId(bookMyBatisEntity.getBookId())
                .title(bookMyBatisEntity.getTitle())
                .bookStatus(bookMyBatisEntity.getBookStatusObj())
                .insertDate(bookMyBatisEntity.getInsertDate())
                .lastUpdateDate(bookMyBatisEntity.getLastUpdateDate())
                .lastChapter(bookMyBatisEntity.getLastChapter())
                .note(bookMyBatisEntity.getNote())
                .URL(bookMyBatisEntity.getURL())
                .user(userFactory.fromDTO(bookMyBatisEntity.getUser()));

        if (bookMyBatisEntity.getTextAuthors() != null) {
            builder.textAuthors(authorFactory.fromDTO(bookMyBatisEntity.getTextAuthors()));
        } else {
            builder.textAuthors(new ArrayList<>());
        }

        if (bookMyBatisEntity.getBookTypeObj() != null) {
            builder.bookType(bookMyBatisEntity.getBookTypeObj());
        }

//        if (bookId2ReadingRecordsMap.get(bookMyBatisEntity.getBookId()) != null) {
//            builder.readingRecords(bookId2ReadingRecordsMap.get(bookMyBatisEntity.getBookId()));
//        }
        // TODO: ???
        if (bookMyBatisEntity.getReadingRecords() != null) {
            builder.readingRecords(
                    bookMyBatisEntity.getReadingRecords().stream()
                            .map(readingRecordFactory::fromDTO)
                            .map(readingRecordsRepository::attach)
                            .collect(Collectors.toCollection(ArrayList::new))
            );
        }

        if (bookMyBatisEntity.getPreviousBooks() != null) {

            HashMap<Book, Integer> bookOrderMap = bookMyBatisEntity.getPreviousBooks().stream()
                    .filter(Objects::nonNull)
                    .map(item -> new AbstractMap.SimpleImmutableEntry<>(
                            fromDTO(item.getBookDTO()),
                            item.getOrder()
                    ))
                    .collect(
                            HashMap::new,
                            (map, item) -> map.put(item.getKey(), item.getValue()),
                            HashMap::putAll
                    );

            builder.previousBooks(
                    new BookChain(bookOrderMap)
            );
        }

        // Set Tags
        List<Tag> tags;
        if (bookMyBatisEntity.getTags() != null) {
            tags = bookMyBatisEntity.getTags().stream()
                    .map(tagFactory::fromDTO)
                    .collect(Collectors.toCollection(ArrayList::new));
        } else {
            tags = new ArrayList<>();
        }
        builder.tags(tags);


        // Set series list
        if (bookMyBatisEntity.getSeriesList() != null && !bookMyBatisEntity.getSeriesList().isEmpty()) {

            List<SeriesMyBatisEntity> seriesDTOList = bookMyBatisEntity.getSeriesList();

            // Converting Series DTO to domain entity
            // TODO: Refactoring required?
            List<Series> seriesList = new ArrayList<>();
            for (SeriesMyBatisEntity seriesMyBatisEntity: seriesDTOList) {

                // Getting list of relations to book for each series
                List<SeriesItemRelationDTO> seriesItemRelationDTOList = seriesMyBatisEntity.getSeriesBookRelationDtoList().stream()
                        .map(item -> (SeriesItemRelationDTO)item)
                        .sorted(Comparator.comparingLong(SeriesItemRelationDTO::getOrder))
                        .collect(Collectors.toCollection(ArrayList::new));

                Series series = seriesFactory.buildSeries(
                        seriesMyBatisEntity.getSeriesId(),
                        seriesMyBatisEntity.getTitle(),
                        userFactory.findById(seriesMyBatisEntity.getUserId()),
                        EntityState.PERSISTED,
                        seriesItemRelationFactory.fromDTO(seriesItemRelationDTOList)
                );

                seriesList.add(series);
            }
            
            builder.seriesList(seriesList);
        }

        BookImpl book = builder.build();

        BookPersistenceProxy bookPersistenceProxy = new BookPersistenceProxy(book, EntityState.PERSISTED);
        bookPersistenceProxy.initPersistedCopy();

        return bookPersistenceProxy;
    }
}
