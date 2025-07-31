package ru.rerumu.lists.domain.book.impl;

import com.jcabi.aspects.Loggable;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.similarity.LevenshteinDistance;
import org.json.JSONArray;
import org.json.JSONObject;
import ru.rerumu.lists.crosscut.exception.EntityNotFoundException;
import ru.rerumu.lists.crosscut.exception.ServerException;
import ru.rerumu.lists.crosscut.utils.DateFactory;
import ru.rerumu.lists.dao.book.AuthorRole;
import ru.rerumu.lists.dao.book.AuthorsBooksRepository;
import ru.rerumu.lists.dao.book.BookRepository;
import ru.rerumu.lists.domain.BookChain;
import ru.rerumu.lists.domain.author.Author;
import ru.rerumu.lists.domain.author.AuthorFactory;
import ru.rerumu.lists.domain.book.Book;
import ru.rerumu.lists.domain.book.BookDTO;
import ru.rerumu.lists.domain.book.readingrecords.ReadingRecord;
import ru.rerumu.lists.domain.book.readingrecords.RecordDTO;
import ru.rerumu.lists.domain.book.readingrecords.impl.ReadingRecordFactory;
import ru.rerumu.lists.domain.book.readingrecords.impl.ReadingRecordImpl;
import ru.rerumu.lists.domain.book.readingrecords.status.BookStatusRecord;
import ru.rerumu.lists.domain.book.readingrecords.status.StatusFactory;
import ru.rerumu.lists.domain.book.type.BookType;
import ru.rerumu.lists.domain.series.Series;
import ru.rerumu.lists.domain.series.SeriesFactory;
import ru.rerumu.lists.domain.series.item.SeriesItemType;
import ru.rerumu.lists.domain.tag.Tag;
import ru.rerumu.lists.domain.user.User;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@ToString
@Slf4j
public class BookImpl implements Book, Cloneable {
    private final static SeriesItemType SERIES_ITEM_TYPE = SeriesItemType.BOOK;

    @Getter
    private final Long bookId;

    @Getter
    private final Long readListId;

    @Getter
    private String title;

    @Getter
    private BookStatusRecord bookStatus;

    @Getter
    private Date insertDate;

    @Getter
    private Date lastUpdateDate;

    private Integer lastChapter;

    @Getter
    private BookType bookType;

    @Getter
    private BookChain previousBooks;

    @Getter
    private String note;

    @Getter
    private List<ReadingRecord> readingRecords;

    private String URL;

    private final User user;

    @Getter
    private List<Tag> tags;

    private final List<Author> textAuthors;

    @Getter
    private final List<Series> seriesList;


    private final ReadingRecordFactory readingRecordFactory;
    private final BookRepository bookRepository;
    private final DateFactory dateFactory;
    private final LevenshteinDistance levenshteinDistance = new LevenshteinDistance();
    private final StatusFactory statusFactory;
    private final AuthorsBooksRepository authorsBooksRepository;
    private final AuthorFactory authorFactory;
    private final SeriesFactory seriesFactory;


    BookImpl(
            @NonNull Long bookId,
            Long readListId,
            @NonNull String title,
            BookStatusRecord bookStatus,
            @NonNull Date insertDate,
            @NonNull Date lastUpdateDate,
            Integer lastChapter,
            BookType bookType,
            BookChain previousBooks,
            String note,
            @NonNull List<ReadingRecord> readingRecords,
            @NonNull StatusFactory statusFactory,
            String URL,
            @NonNull User user,
            @NonNull List<Tag> tags,
            @NonNull List<Author> textAuthors,
            @NonNull List<Series> seriesList,
            @NonNull DateFactory dateFactory,
            @NonNull ReadingRecordFactory readingRecordFactory,
            @NonNull BookRepository bookRepository,
            @NonNull AuthorsBooksRepository authorsBooksRepository,
            @NonNull AuthorFactory authorFactory,
            @NonNull SeriesFactory seriesFactory
    ) {

        this.bookId = bookId;
        this.readListId = readListId;
        this.title = title;
        this.bookStatus = bookStatus;
        this.insertDate = insertDate;
        this.lastUpdateDate = lastUpdateDate;
        this.bookType = bookType;
        this.previousBooks = previousBooks;
        this.note = note;
        this.readingRecords = new ArrayList<>(readingRecords);
        this.seriesList = new ArrayList<>(seriesList);
        this.statusFactory = statusFactory;
        this.URL = URL;
        this.user = user;
        this.tags = new ArrayList<>(tags);
        this.textAuthors = textAuthors;
        this.dateFactory = dateFactory;
        this.readingRecordFactory = readingRecordFactory;
        this.bookRepository = bookRepository;
        this.authorsBooksRepository = authorsBooksRepository;
        this.authorFactory = authorFactory;
        this.seriesFactory = seriesFactory;
    }

    public LocalDateTime getLastUpdateDate_V2() {
        return LocalDateTime.ofInstant(lastUpdateDate.toInstant(), ZoneOffset.UTC);
    }

    public Optional<Integer> getLastChapter() {
        return Optional.ofNullable(lastChapter);
    }

    @Deprecated
    @Override
    public BookImpl clone() {
        try {
            BookImpl clone = (BookImpl) super.clone();
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    public JSONObject toJSONObject() {
        JSONObject obj = new JSONObject();

        obj.put("bookId", bookId);
        obj.put("readListId", readListId);
        obj.put("title", title);
        JSONObject bookStatusJson = new JSONObject();
//        bookStatusJson.put("statusId", bookStatus.statusId());
//        bookStatusJson.put("statusName", bookStatus.statusName());
        obj.put("bookStatus", bookStatusJson);
        obj.put(
                "insertDate",
                new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(insertDate)
        );
        obj.put(
                "lastUpdateDate",
                new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(lastUpdateDate)
        );
        obj.put("lastChapter", lastChapter);
        obj.put("note", note);

        if (bookType != null) {
            JSONObject bookTypeJson = new JSONObject();
            bookTypeJson.put("typeId", bookType.getId());
            bookTypeJson.put("typeName", bookType.getName());

            obj.put("bookType", bookTypeJson);
        }
        obj.put("itemType", SERIES_ITEM_TYPE.name());
        JSONArray chainArray = new JSONArray();
        if (previousBooks != null){
            chainArray = previousBooks.toJSONArray();
        }
        obj.put("chain", chainArray);

        JSONArray readingRecordsArray = new JSONArray();
        if (readingRecords != null){
            readingRecords.stream()
                    .map(ReadingRecord::toJSONObject)
                    .forEach(readingRecordsArray::put);
        }
        obj.put("readingRecords", readingRecordsArray);
        obj.put("URL", URL);

        JSONArray tagsArray = new JSONArray();
        if (tags != null) {
            tags.stream()
                    .map(Tag::toJSONObject)
                    .forEach(tagsArray::put);
        }
        obj.put("tags", tagsArray);

        return obj;
    }

    @Override
    public boolean currentStatusEquals(Long statusId) {
        ReadingRecord readingRecord = readingRecords.stream()
                .max(Comparable::compareTo)
                .orElseThrow(() -> new ServerException("Error while processing records"));

        return readingRecord.statusEquals(statusId);
    }

    @Override
    public void delete() {

        // TODO: Details of data storage should be encapsulated in DAO layer (probably)
        authorsBooksRepository.getAuthorsByBookId(bookId)
                        .forEach(authorDtoDao -> authorsBooksRepository.delete(
                                bookId,
                                authorDtoDao.getAuthorId()
                        ));

        for (Series series: seriesList) {
            series.removeBookRelation(bookId);
        }

        bookRepository.delete(bookId);
    }

    @Override
    public LocalDateTime getUpdateDate() {
        return getLastUpdateDate_V2();
    }

    @Override
    public void addReadingRecord(
            @NonNull BookStatusRecord bookStatusRecord,
            LocalDateTime startDate,
            LocalDateTime endDate,
            Long lastChapter
    ) {

        ReadingRecordImpl readingRecord = readingRecordFactory.createReadingRecord(
                bookId,
                bookStatusRecord,
                startDate,
                endDate,
                lastChapter
        );

        // TODO: Remove
        if (readingRecords == null){
            readingRecords = new ArrayList<>();
        }

        readingRecords.add(readingRecord);

    }

    @Override
    public void addReadingRecord(@NonNull Long statusId, @NonNull LocalDateTime startDate, LocalDateTime endDate, Long lastChapter) {
        // Find status
        BookStatusRecord bookStatusRecord = statusFactory.findById(statusId);

        // Create record
        ReadingRecordImpl readingRecord = readingRecordFactory.createReadingRecord(
                bookId,
                bookStatusRecord,
                startDate,
                endDate,
                lastChapter
        );

        // TODO: Remove
        if (readingRecords == null){
            readingRecords = new ArrayList<>();
        }

        readingRecords.add(readingRecord);
    }

    @Override
    public Long getId() {
        return bookId;
    }

    @Override
    public Long getListId() {
        return readListId;
    }

    @Override
    public User getUser() {
        return user;
    }

    @Override
    public void updateInsertDate(LocalDateTime insertDate) {
        this.insertDate = Date.from(insertDate.toInstant(ZoneOffset.UTC));
    }

    @Override
    public void updateTitle(String title) {
        this.title = title;
    }

    @Override
    public void updateLastChapter(Integer lastChapter) {
//        if (!Objects.equals(this.lastChapter, lastChapter)){
//            this.lastChapter = lastChapter;
//            lastUpdateDate = dateFactory.getCurrentDate();
//        }
    }

    @Override
    public void updateStatus(BookStatusRecord bookStatusRecord) {
        if (!Objects.equals(this.bookStatus.statusId(), bookStatusRecord.statusId())){
            this.bookStatus = bookStatusRecord;
            lastUpdateDate = dateFactory.getCurrentDate();
        }
    }

    @Override
    public void updateNote(String note) {
        this.note = note;
    }

    @Override
    public void updateType(BookType bookType) {
        this.bookType = bookType;
    }

    @Override
    public void updateURL(String URL) {
        this.URL = URL;
        this.lastUpdateDate = dateFactory.getCurrentDate();
    }

    @Override
    public void updateTags(@NonNull List<Tag> tags) {
        // Adding new tags
        List<Tag> tagsToAdd = tags.stream()
                .filter(tag -> !this.tags.contains(tag))
                .collect(Collectors.toCollection(ArrayList::new));
        for (Tag tag: tagsToAdd) {
            this.tags.add(tag);
            tag.addToBook(this);
        }

        // Removing tags
        List<Tag> tagsToRemove = this.tags.stream()
                .filter(tag -> !tags.contains(tag))
                .collect(Collectors.toCollection(ArrayList::new));
        for (Tag tag: tagsToRemove) {
            this.tags.remove(tag);
            tag.removeFromBook(bookId);
        }
    }

    /**
     * Updates series list
     */
    @Override
    @Loggable(value = Loggable.DEBUG, prepend = true, trim = false, logThis = true)
    public void updateSeries(@NonNull List<Series> seriesList) {
        // Add
        List<Series> seriesToAdd = seriesList.stream()
                .filter(item -> !this.seriesList.contains(item))
                .collect(Collectors.toCollection(ArrayList::new));
        log.debug("seriesToAdd: {}", seriesToAdd);
        for (Series series: seriesToAdd) {
            series.addBookRelation(bookId);
            seriesList.add(series);
        }

        // Remove
        List<Series> seriesToRemove = this.seriesList.stream()
                .filter(item -> !seriesList.contains(item))
                .collect(Collectors.toCollection(ArrayList::new));
        log.debug("seriesToRemove: {}", seriesToRemove);
        for (Series series: seriesToRemove) {
            series.removeBookRelation(bookId);
            seriesList.remove(series);
        }
    }

    @Override
    public void updateTextAuthors(List<Author> authors) {
        // Add new authors
        List<Author> authorsToAdd = authors.stream()
                .filter(author -> !textAuthors.contains(author))
                .collect(Collectors.toCollection(ArrayList::new));
        for (Author author: authorsToAdd) {
            textAuthors.add(author);

            // TODO: Probably better to move to DAO layer. Should be behind BookRepository???
            authorsBooksRepository.add(bookId, author.getId(), user.userId(), AuthorRole.TEXT_AUTHOR.getId());
        }

        // Remove existing authors
        List<Author> authorsToRemove = textAuthors.stream()
                .filter(author -> !authors.contains(author))
                .collect(Collectors.toCollection(ArrayList::new));
        for (Author author: authorsToRemove) {
            textAuthors.remove(author);

            // TODO: Same as previous
            authorsBooksRepository.deleteByAuthor(author.getId());
        }
    }

    @Override
    @Loggable(value = Loggable.DEBUG, prepend = true, trim = false, logThis = true)
    public void save() {
        bookRepository.update(this);

        for (Series series: seriesList) {
            series.save();
        }

    }

    @Override
    public boolean filterByStatusIds(List<Integer> statusIds) {
        return statusIds.contains(bookStatus.statusId());
    }

    @Override
    public Float getTitleFuzzyMatchScore(String value) {
        if (title.equalsIgnoreCase(value)){
            return 1f;
        }

        List<String> titleSubstrings = new ArrayList<>();
        titleSubstrings.add(title);
        titleSubstrings.addAll(Arrays.asList(title.split(" ")));

        List<Float> scores = new ArrayList<>();
        for(String item: titleSubstrings){
            Integer distance = levenshteinDistance.apply(item.toUpperCase(), value.toUpperCase());
            Float score = (float) (item.length() - distance) / item.length();
            scores.add(score);
        }

        Float res = scores.stream().max(Float::compareTo).orElseThrow();
        return res;
    }

    public ReadingRecord deleteReadingRecord(Long readingRecordId){
        ReadingRecord readingRecord = readingRecords.stream()
                .filter(item -> item.getId().equals(readingRecordId))
                .findAny()
                .orElseThrow(EntityNotFoundException::new);

        readingRecords.remove(readingRecord);
        readingRecord.delete();

        return readingRecord;
    }

    @Override
    public void deleteOtherReadingRecords(List<Long> readingRecordIdsToKeep) {
        List<ReadingRecord> readingRecordsToDelete = readingRecords.stream()
                .filter(readingRecord -> !readingRecordIdsToKeep.contains(readingRecord.getId()))
                .collect(Collectors.toCollection(ArrayList::new));

        for (ReadingRecord readingRecord: readingRecordsToDelete) {
            readingRecords.remove(readingRecord);
            readingRecord.delete();
        }
    }

    public void updateReadingRecord(
            @NonNull Long readingRecordId,
            @NonNull BookStatusRecord bookStatusRecord,
            @NonNull LocalDateTime startDate,
            LocalDateTime endDate,
            Long lastChapter
    ){
        ReadingRecord readingRecord = readingRecords.stream()
                .filter(item -> item.getId().equals(readingRecordId))
                .findAny()
                .orElseThrow(EntityNotFoundException::new);

        readingRecord.setStatus(bookStatusRecord);
        readingRecord.setStartDate(startDate);
        readingRecord.setEndDate(endDate);
        readingRecord.setLastChapter(lastChapter);

        lastUpdateDate = dateFactory.getCurrentDate();

        readingRecord.save();
        save();
    }

    @Override
    public void updateReadingRecord(@NonNull Long readingRecordId, @NonNull Long statusId, @NonNull LocalDateTime startDate, LocalDateTime endDate, Long lastChapter) {
        BookStatusRecord bookStatusRecord = statusFactory.findById(statusId);
        updateReadingRecord(readingRecordId, bookStatusRecord, startDate, endDate, lastChapter);
    }

    /**
     * Update reading records according to passed list of records
     */
    @Override
    @Loggable(value = Loggable.DEBUG, trim = false, prepend = true)
    public void updateReadingRecords(List<RecordDTO> records) {

        // Collect records to keep from dto
        List<Long> readingRecordIdsToKeep = records.stream()
                .map(RecordDTO::getRecordId)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toCollection(ArrayList::new));

        // Delete other reading records
        deleteOtherReadingRecords(readingRecordIdsToKeep);

        for (RecordDTO recordDTO: records) {
            if (recordDTO.getRecordId() == null) {
                // Add record
                addReadingRecord(
                        recordDTO.getStatusId(),
                        recordDTO.getStartDate(),
                        recordDTO.getEndDate(),
                        recordDTO.getLastChapter()
                );
            } else {
                // Update record
                updateReadingRecord(
                        recordDTO.getRecordId(),
                        recordDTO.getStatusId(),
                        recordDTO.getStartDate(),
                        recordDTO.getEndDate(),
                        recordDTO.getLastChapter()
                );
            }
        }
    }

//    @Override
//    public String toString() {
//        return this.toJSONObject().toString();
//    }

    @Override
    public BookDTO toDTO() {

        BookDTO bookDTO = new BookDTO(
            bookId,
            readListId,
            title,
            bookStatus != null ? bookStatus.statusId() : null,
            insertDate,
            lastUpdateDate,
            lastChapter,
            bookType != null ? bookType.getId() : null,
            note,
            bookType != null ? bookType.toDTO() : null,
            bookStatus,
            previousBooks != null ? previousBooks.toDTO() : new ArrayList<>(),
            readingRecords != null ? readingRecords.stream()
                    .map(ReadingRecord::toDTO)
                    .collect(Collectors.toCollection(ArrayList::new)) : new ArrayList<>(),
            URL,
            user != null ? user.userId() : null,
            tags != null ? tags.stream()
                    .map(Tag::toDTO)
                    .collect(Collectors.toCollection(ArrayList::new)) : new ArrayList<>(),
            textAuthors.stream()
                    .map(Author::toDTO)
                    .collect(Collectors.toCollection(ArrayList::new)),
            seriesList.stream()
                    .map(Series::toDTO)
                    .collect(Collectors.toCollection(ArrayList::new))
        );

        return bookDTO;
    }
}
