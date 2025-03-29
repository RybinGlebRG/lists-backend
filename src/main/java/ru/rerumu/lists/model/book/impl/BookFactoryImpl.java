package ru.rerumu.lists.model.book.impl;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.rerumu.lists.crosscut.exception.EmptyMandatoryParameterException;
import ru.rerumu.lists.crosscut.utils.DateFactory;
import ru.rerumu.lists.dao.book.BookDtoDao;
import ru.rerumu.lists.dao.book.BookRepository;
import ru.rerumu.lists.model.BookChain;
import ru.rerumu.lists.model.book.Book;
import ru.rerumu.lists.model.book.BookDTO;
import ru.rerumu.lists.model.book.BookFactory;
import ru.rerumu.lists.model.book.readingrecords.ReadingRecord;
import ru.rerumu.lists.model.book.readingrecords.impl.ReadingRecordFactory;
import ru.rerumu.lists.model.book.readingrecords.status.BookStatusRecord;
import ru.rerumu.lists.model.book.readingrecords.status.StatusFactory;
import ru.rerumu.lists.model.book.type.BookType;
import ru.rerumu.lists.model.book.type.BookTypeFactory;
import ru.rerumu.lists.model.dto.BookOrderedDTO;
import ru.rerumu.lists.model.tag.Tag;
import ru.rerumu.lists.model.tag.TagFactory;
import ru.rerumu.lists.model.user.User;
import ru.rerumu.lists.model.user.UserFactory;

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

    @Autowired
    public BookFactoryImpl(
            DateFactory dateFactory,
            BookRepository bookRepository,
            ReadingRecordFactory readingRecordFactory,
            BookTypeFactory bookTypeFactory,
            UserFactory userFactory, TagFactory tagFactory, StatusFactory statusFactory
    ) {
        this.dateFactory = dateFactory;
        this.bookRepository = bookRepository;
        this.readingRecordFactory = readingRecordFactory;
        this.bookTypeFactory = bookTypeFactory;
        this.userFactory = userFactory;
        this.tagFactory = tagFactory;
        this.statusFactory = statusFactory;
    }

    public Book createBook(
            Long readListId,
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
        BookBuilder bookBuilder = new BookBuilder(statusFactory)
                .bookId(bookId)
                .readListId(readListId)
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

        book.setReadingRecordFactory(readingRecordFactory);
        book.setBookRepository(bookRepository);
        book.setDateFactory(dateFactory);

        return book;
    }

    public Book getBook(Long bookId) throws EmptyMandatoryParameterException {
        BookDtoDao bookDTO = this.bookRepository.findById(bookId);
        log.debug("bookDTO: {}", bookDTO);
        Book book = fromDTO(bookDTO);
        log.debug("Got book: {}", book);

        return book;
    }

    public List<Book> getAllChained(Long readListId) {
        List<BookDTO> res = bookRepository.getAllChained(readListId);
        return fromDTO(res);
    }

    @Override
    public List<Book> findAll(User user, Boolean isChained) {
        bookRepository.
    }

    public List<Book> getAll(Long readListId) {
        return fromDTO(bookRepository.getAll(readListId));
    }

    public Book fromDTO(@NonNull BookDTO bookDTO) {
        return fromDTO(List.of(bookDTO)).get(0);
    }

    @Override
    public Book fromDTO(@NonNull BookDtoDao bookDTO) {
        return fromDTO(List.of(bookDTO), true).get(0);
    }

    public List<Book> fromDTO(@NonNull List<BookDTO> bookDTOList) {
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

    public List<Book> fromDTO(@NonNull List<BookDtoDao> bookDTOList, boolean istrue) {
        // Preparing reading records
        Map<Long, List<ReadingRecord>> bookId2ReadingRecordsMap = readingRecordFactory.findByBookIds(
                        // Collecting bookIds from chain
                        bookDTOList.stream()
                                // flatten book chain
                                .flatMap(bookDTO -> {
                                    List<BookDtoDao> tmp = new ArrayList<>();
                                    tmp.add(bookDTO);

//                                    if (bookDTO.getPreviousBooks() != null) {
//                                        tmp.addAll(
//                                                bookDTO.getPreviousBooks().stream()
//                                                        .map(BookOrderedDTO::getBookDTO)
//                                                        .collect(Collectors.toCollection(ArrayList::new))
//                                        );
//                                    }

                                    return tmp.stream();
                                })
                                .map(BookDtoDao::getBookId)
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

    private Book fromDTO(
            @NonNull BookDTO bookDTO,
            @NonNull Map<Long, List<ReadingRecord>> bookId2ReadingRecordsMap,
            @NonNull Map<Long, User> userId2UserMap
    ) throws EmptyMandatoryParameterException {

        log.debug("bookDTO: {}", bookDTO);

        BookBuilder builder = new BookBuilder(statusFactory)
                .bookId(bookDTO.bookId)
                .readListId(bookDTO.readListId)
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
        book.setReadingRecordFactory(readingRecordFactory);
        book.setBookRepository(bookRepository);
        book.setDateFactory(dateFactory);

        return book;
    }

    private Book fromDTO(
            @NonNull BookDtoDao bookDTO,
            @NonNull Map<Long, List<ReadingRecord>> bookId2ReadingRecordsMap,
            @NonNull Map<Long, User> userId2UserMap
    ) throws EmptyMandatoryParameterException {

        log.debug("bookDTO: {}", bookDTO);

        BookBuilder builder = new BookBuilder(statusFactory)
                .bookId(bookDTO.bookId)
                .readListId(bookDTO.readListId)
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
        book.setReadingRecordFactory(readingRecordFactory);
        book.setBookRepository(bookRepository);
        book.setDateFactory(dateFactory);

        return book;
    }
}
