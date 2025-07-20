package ru.rerumu.lists.domain.book.impl;

import com.jcabi.aspects.Loggable;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.rerumu.lists.crosscut.exception.EmptyMandatoryParameterException;
import ru.rerumu.lists.crosscut.utils.DateFactory;
import ru.rerumu.lists.dao.author.AuthorDtoDao;
import ru.rerumu.lists.dao.book.AuthorsBooksRepository;
import ru.rerumu.lists.dao.book.BookDtoDao;
import ru.rerumu.lists.dao.book.BookRepository;
import ru.rerumu.lists.domain.BookChain;
import ru.rerumu.lists.domain.author.AuthorFactory;
import ru.rerumu.lists.domain.book.Book;
import ru.rerumu.lists.domain.book.BookDTO;
import ru.rerumu.lists.domain.book.BookFactory;
import ru.rerumu.lists.domain.book.readingrecords.ReadingRecord;
import ru.rerumu.lists.domain.book.readingrecords.impl.ReadingRecordFactory;
import ru.rerumu.lists.domain.book.readingrecords.status.BookStatusRecord;
import ru.rerumu.lists.domain.book.readingrecords.status.StatusFactory;
import ru.rerumu.lists.domain.book.type.BookType;
import ru.rerumu.lists.domain.book.type.BookTypeFactory;
import ru.rerumu.lists.domain.dto.BookOrderedDTO;
import ru.rerumu.lists.domain.series.Series;
import ru.rerumu.lists.domain.series.SeriesFactory;
import ru.rerumu.lists.domain.tag.Tag;
import ru.rerumu.lists.domain.tag.TagFactory;
import ru.rerumu.lists.domain.user.User;
import ru.rerumu.lists.domain.user.UserFactory;

import java.time.LocalDateTime;
import java.util.AbstractMap;
import java.util.ArrayList;
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
            @NonNull SeriesFactory seriesFactory
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

        return book;
    }

    @Loggable(value = Loggable.DEBUG, trim = false, prepend = true)
    @NonNull
    @Override
    public Book getBook(Long bookId, Long userId) {
        BookDtoDao bookDTO = bookRepository.findById(bookId, userId);

        // TODO: Details of data retrieval should be encapsulated in DAO layer
        List<AuthorDtoDao> authorsDTOs = authorsBooksRepository.getAuthorsByBookId(bookId);

        // TODO: and same here
        List<Series> seriesList = seriesFactory.findByBook(bookId, userId);
        bookDTO.setSeriesList(seriesList);

        bookDTO.setTextAuthors(authorsDTOs);
        Book book = fromDTO(bookDTO);
        return book;
    }

    /**
     * Find all books of user
     */
    @Override
    public List<Book> findAll(User user, Boolean isChained) {
        if (isChained) {
            List<BookDtoDao> bookDtoList = bookRepository.findByUserChained(user.userId());
            return fromDTO(bookDtoList);
        } else {
            return bookRepository.findByUser(user).stream()
                    .map(this::fromDTO)
                    .collect(Collectors.toCollection(ArrayList::new));
        }
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
        Map<Long, List<ReadingRecord>> bookId2ReadingRecordsMap = readingRecordFactory.findByBookIds(
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
    public List<Book> fromDTO(@NonNull List<BookDtoDao> bookDTOList) {
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

        return book;
    }

    @Override
    @Loggable(value = Loggable.TRACE, trim = false, prepend = true)
    @NonNull
    public Book fromDTO(@NonNull BookDtoDao bookDTO) throws EmptyMandatoryParameterException {

        BookBuilder builder = new BookBuilder(
                statusFactory,
                dateFactory,
                readingRecordFactory,
                bookRepository,
                authorsBooksRepository,
                authorFactory,
                seriesFactory
        )
                .bookId(bookDTO.getBookId())
                .title(bookDTO.getTitle())
                .bookStatus(bookDTO.getBookStatusObj())
                .insertDate(bookDTO.getInsertDate())
                .lastUpdateDate(bookDTO.getLastUpdateDate())
                .lastChapter(bookDTO.getLastChapter())
                .note(bookDTO.getNote())
                .URL(bookDTO.getURL())
                .user(userFactory.fromDTO(bookDTO.getUser()));

        if (bookDTO.getTextAuthors() != null) {
            builder.textAuthors(authorFactory.fromDTO(bookDTO.getTextAuthors()));
        } else {
            builder.textAuthors(new ArrayList<>());
        }

        if (bookDTO.getBookTypeObj() != null) {
            builder.bookType(bookDTO.getBookTypeObj().toDomain());
        }

//        if (bookId2ReadingRecordsMap.get(bookDTO.getBookId()) != null) {
//            builder.readingRecords(bookId2ReadingRecordsMap.get(bookDTO.getBookId()));
//        }
        // TODO: ???
        if (bookDTO.getReadingRecords() != null) {
            builder.readingRecords(
                    bookDTO.getReadingRecords().stream()
                            .map(readingRecordFactory::fromDTO)
                            .collect(Collectors.toCollection(ArrayList::new))
            );
        }

        if (bookDTO.getPreviousBooks() != null) {

            HashMap<Book, Integer> bookOrderMap = bookDTO.getPreviousBooks().stream()
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
        if (bookDTO.getTags() != null) {
            tags = bookDTO.getTags().stream()
                    .map(tagFactory::fromDTO)
                    .collect(Collectors.toCollection(ArrayList::new));
        } else {
            tags = new ArrayList<>();
        }
        builder.tags(tags);


        // Set series list
        if (!bookDTO.getSeriesList().isEmpty()) {
            builder.seriesList(bookDTO.getSeriesList());
        }

        BookImpl book = builder.build();

        return book;
    }
}
