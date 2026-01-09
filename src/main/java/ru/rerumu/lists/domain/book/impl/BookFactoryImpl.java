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
import ru.rerumu.lists.dao.readingrecord.ReadingRecordsRepository;
import ru.rerumu.lists.dao.series.SeriesMyBatisEntity;
import ru.rerumu.lists.dao.series.impl.SeriesPersistenceProxy;
import ru.rerumu.lists.domain.author.Author;
import ru.rerumu.lists.domain.author.AuthorFactory;
import ru.rerumu.lists.domain.base.EntityState;
import ru.rerumu.lists.domain.book.Book;
import ru.rerumu.lists.domain.book.BookChain;
import ru.rerumu.lists.domain.book.BookFactory;
import ru.rerumu.lists.domain.bookstatus.BookStatusRecord;
import ru.rerumu.lists.domain.bookstatus.StatusFactory;
import ru.rerumu.lists.domain.booktype.BookType;
import ru.rerumu.lists.domain.booktype.BookTypeFactory;
import ru.rerumu.lists.domain.readingrecords.ReadingRecord;
import ru.rerumu.lists.domain.readingrecords.impl.ReadingRecordFactory;
import ru.rerumu.lists.domain.series.Series;
import ru.rerumu.lists.domain.series.SeriesFactory;
import ru.rerumu.lists.dao.series.SeriesItemRelationDTO;
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
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Component
public class BookFactoryImpl implements BookFactory {

    private final DateFactory dateFactory;
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
            Long bookId,
            String title,
            Integer lastChapter,
            String note,
            BookStatusRecord bookStatus,
            LocalDateTime insertDate,
            BookType bookType,
            String URL,
            User user
    ) {

        LocalDateTime actualInsertDate;
        LocalDateTime actualUpdateDate;
        if (insertDate != null) {
            actualInsertDate = insertDate;
            actualUpdateDate = insertDate;
        } else {
            LocalDateTime tmp = dateFactory.getLocalDateTime();
            actualInsertDate = tmp;
            actualUpdateDate =  tmp;
        }

        BookImpl book = new BookImpl(
                bookId,
                null,
                title,
                bookStatus,
                actualInsertDate,
                actualUpdateDate,
                lastChapter,
                bookType,
                null,
                note,
                new ArrayList<>(),
                statusFactory,
                URL,
                user,
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>(),
                dateFactory,
                readingRecordFactory,
                authorsBooksRepository,
                authorFactory,
                seriesFactory
        );

        return book;
    }

    @Override
    @Loggable(value = Loggable.TRACE, trim = false, prepend = true)
    @NonNull
    public Book fromDTO(@NonNull BookMyBatisEntity bookMyBatisEntity) throws EmptyMandatoryParameterException {

        // Get authors
        List<Author> authors;
        if (bookMyBatisEntity.getTextAuthors() != null) {
            authors = authorFactory.fromDTO(bookMyBatisEntity.getTextAuthors());
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
                    .map(readingRecordFactory::fromDTO)
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
                            fromDTO(item.getBookDTO()),
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
                    .map(tagFactory::fromDTO)
                    .collect(Collectors.toCollection(ArrayList::new));
        } else {
            tags = new ArrayList<>();
        }

        // Get series list
        List<Series> seriesList;
        if (bookMyBatisEntity.getSeriesList() != null && !bookMyBatisEntity.getSeriesList().isEmpty()) {

            List<SeriesMyBatisEntity> seriesDTOList = bookMyBatisEntity.getSeriesList();

            // Converting Series DTO to domain entity
            // TODO: Refactoring required?
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
                        userFactory.findById(seriesMyBatisEntity.getUserId()),
                        seriesItemRelationFactory.fromDTO(seriesItemRelationDTOList)
                );

                // TODO: move to repository
                series = new SeriesPersistenceProxy(series, EntityState.PERSISTED);

                seriesList.add(series);
            }
        } else {
            seriesList = new ArrayList<>();
        }

        // Create book
        Book book = new BookImpl(
                bookMyBatisEntity.getBookId(),
                null,
                bookMyBatisEntity.getTitle(),
                bookMyBatisEntity.getBookStatusObj(),
                bookMyBatisEntity.getInsertDate(),
                bookMyBatisEntity.getLastUpdateDate(),
                bookMyBatisEntity.getLastChapter(),
                bookType,
                bookChain,
                bookMyBatisEntity.getNote(),
                readingRecords,
                statusFactory,
                bookMyBatisEntity.getURL(),
                userFactory.fromDTO(bookMyBatisEntity.getUser()),
                tags,
                authors,
                seriesList,
                dateFactory,
                readingRecordFactory,
                authorsBooksRepository,
                authorFactory,
                seriesFactory
        );

        return book;
    }
}
