package ru.rerumu.lists.model.book;

import jakarta.annotation.Nullable;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.apache.commons.text.similarity.LevenshteinDistance;
import org.json.JSONArray;
import org.json.JSONObject;
import ru.rerumu.lists.exception.EntityNotFoundException;
import ru.rerumu.lists.factories.DateFactory;
import ru.rerumu.lists.model.BookChain;
import ru.rerumu.lists.model.BookStatusRecord;
import ru.rerumu.lists.model.User;
import ru.rerumu.lists.model.book.reading_records.ReadingRecord;
import ru.rerumu.lists.model.book.type.BookType;
import ru.rerumu.lists.model.series.item.SeriesItemType;
import ru.rerumu.lists.model.book.reading_records.ReadingRecordImpl;
import ru.rerumu.lists.model.book.reading_records.ReadingRecordFactory;
import ru.rerumu.lists.repository.BookRepository;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

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

    // TODO: Make final
    @Setter
    private ReadingRecordFactory readingRecordFactory;

    @Setter
    private BookRepository bookRepository;

    @Setter
    private DateFactory dateFactory;

    private final LevenshteinDistance levenshteinDistance = new LevenshteinDistance();

    private String URL;

    private User user;


    BookImpl(
            Long bookId,
            Long readListId,
            @NonNull String title,
            @NonNull BookStatusRecord bookStatus,
            @NonNull Date insertDate,
            @NonNull Date lastUpdateDate,
            Integer lastChapter,
            BookType bookType,
            BookChain previousBooks,
            String note,
            List<ReadingRecord> readingRecords,
            String URL,
            User user
    ) {

        this.bookId = bookId;
        this.readListId = readListId;
        this.title = title;
        this.bookStatus = bookStatus;
        this.insertDate = insertDate;
        this.lastUpdateDate = lastUpdateDate;
//        this.lastChapter = lastChapter;
        this.bookType = bookType;
        this.previousBooks = previousBooks;
        this.note = note;
        this.readingRecords = readingRecords;
        this.URL = URL;
        this.user = user;
    }

//    public BookImpl(Long bookId,
//                    Long readListId,
//                    String title,
//                    BookStatusRecord bookStatus,
//                    Date insertDate,
//                    Date lastUpdateDate,
//                    Integer lastChapter) {
//        this(bookId, readListId, title, bookStatus, insertDate, lastUpdateDate, lastChapter, null, null, null, new ArrayList<>());
//    }

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
        bookStatusJson.put("statusId", bookStatus.statusId());
        bookStatusJson.put("statusName", bookStatus.statusName());
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

        return obj;
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
    public Long getId() {
        return bookId;
    }

    @Override
    public Long getListId() {
        return readListId;
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
    public void save() {
        bookRepository.update(this);
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

        readingRecord.save();
    }


    @Override
    public String toString() {
        return this.toJSONObject().toString();
    }

    @Override
    public BookDTO toDTO() {

        BookDTO bookDTO = new BookDTO(
            bookId,
            readListId,
            title,
            bookStatus.statusId(),
            insertDate,
            lastUpdateDate,
            lastChapter,
            bookType.getId(),
            note,
            bookType.toDTO(),
            bookStatus,
            previousBooks != null ? previousBooks.toDTO() : null,
            previousBooks != null ?readingRecords.stream()
                    .map(ReadingRecord::toDTO)
                    .collect(Collectors.toCollection(ArrayList::new)) : null,
            URL,
            user != null ? user.userId() : null
        );

        return bookDTO;
    }
}
