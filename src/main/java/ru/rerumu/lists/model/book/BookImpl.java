package ru.rerumu.lists.model.book;

import jakarta.annotation.Nullable;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.json.JSONArray;
import org.json.JSONObject;
import ru.rerumu.lists.exception.EmptyMandatoryParameterException;
import ru.rerumu.lists.exception.EntityNotFoundException;
import ru.rerumu.lists.model.BookChain;
import ru.rerumu.lists.model.BookStatusRecord;
import ru.rerumu.lists.model.BookType;
import ru.rerumu.lists.model.SeriesItem;
import ru.rerumu.lists.model.SeriesItemType;
import ru.rerumu.lists.model.books.reading_records.ReadingRecord;
import ru.rerumu.lists.model.books.reading_records.ReadingRecordFactory;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class BookImpl implements Book, Cloneable, SeriesItem {
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


    public BookImpl(
            Long bookId,
            Long readListId,
            String title,
            BookStatusRecord bookStatus,
            Date insertDate,
            Date lastUpdateDate,
            Integer lastChapter,
            BookType bookType,
            BookChain previousBooks,
            String note,
            List<ReadingRecord> readingRecords
    ) {
        Objects.requireNonNull(title, "Book title cannot be null");
        Objects.requireNonNull(bookStatus, "Book status cannot be null");
        Objects.requireNonNull(insertDate, "Book insert date cannot be null");
        Objects.requireNonNull(lastUpdateDate, "Book last update date cannot be null");

        this.bookId = bookId;
        this.readListId = readListId;
        this.title = title;
        this.bookStatus = bookStatus;
        this.insertDate = insertDate;
        this.lastUpdateDate = lastUpdateDate;
        this.lastChapter = lastChapter;
        this.bookType = bookType;
        this.previousBooks = previousBooks;
        this.note = note;
        this.readingRecords = readingRecords;
    }

    public BookImpl(Long bookId,
                    Long readListId,
                    String title,
                    BookStatusRecord bookStatus,
                    Date insertDate,
                    Date lastUpdateDate,
                    Integer lastChapter) {
        this(bookId, readListId, title, bookStatus, insertDate, lastUpdateDate, lastChapter, null, null, null, new ArrayList<>());
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

        return obj;
    }

    @Override
    public LocalDateTime getUpdateDate() {
        return getLastUpdateDate_V2();
    }

    public ReadingRecord addReadingRecord(
            Long bookId,
            Long readingRecordId,
            BookStatusRecord bookStatusRecord,
            LocalDateTime startDate,
            @Nullable LocalDateTime endDate
    ){
        ReadingRecord readingRecord = ReadingRecord.builder()
                .bookId(bookId)
                .recordId(readingRecordId)
                .bookStatus(bookStatusRecord)
                .startDate(startDate)
                .endDate(endDate)
                .build();

        readingRecords.add(readingRecord);

        return readingRecord;
    }

    @Override
    public void addReadingRecord(
            @NonNull BookStatusRecord bookStatusRecord,
            LocalDateTime startDate,
            LocalDateTime endDate
    ) {

        ReadingRecord readingRecord = readingRecordFactory.createReadingRecord(
                bookId,
                bookStatusRecord,
                startDate,
                endDate
        );

        readingRecords.add(readingRecord);

    }

    @Override
    public Long getId() {
        return bookId;
    }

    public ReadingRecord deleteReadingRecord(Long readingRecordId){
        ReadingRecord readingRecord = readingRecords.stream()
                .filter(item -> item.recordId().equals(readingRecordId))
                .findAny()
                .orElseThrow(EntityNotFoundException::new);

        readingRecords.remove(readingRecord);
        return readingRecord;
    }

    public ReadingRecord updateReadingRecord(
            Long readingRecordId,
            BookStatusRecord bookStatusRecord,
            LocalDateTime startDate,
            @Nullable LocalDateTime endDate
    ){
        ReadingRecord readingRecord = readingRecords.stream()
                .filter(item -> item.recordId().equals(readingRecordId))
                .findAny()
                .orElseThrow(EntityNotFoundException::new);

        readingRecord = readingRecord.toBuilder()
                .bookStatus(bookStatusRecord)
                .startDate(startDate)
                .endDate(endDate)
                .build();

        return readingRecord;
    }


    @Override
    public String toString() {
        return this.toJSONObject().toString();
    }

    /**
     * @deprecated Use {@link BookBuilder}
     */
    @Deprecated
    public final static class Builder {
        private Long bookId;
        private Long readListId;
        private String title;
        private BookStatusRecord bookStatus;
        private Date insertDate;
        private Date lastUpdateDate;
        private Integer lastChapter;

        private BookType bookType;

        private BookChain previousBooks;

        private String note;
        private List<ReadingRecord> readingRecords;

        public Builder() {
        }

        public Builder(BookImpl book) {
            this.bookId = book.bookId;
            this.readListId = book.readListId;
            this.title = book.title;
            this.bookStatus = book.bookStatus;
            this.insertDate = book.insertDate;
            this.lastUpdateDate = book.lastUpdateDate;
            this.lastChapter = book.lastChapter;
            this.previousBooks = book.previousBooks;
            this.note = book.note;
        }

        public Builder bookId(Long bookId) {
            this.bookId = bookId;
            return this;
        }

        public Builder readListId(Long readListId) {
            this.readListId = readListId;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder bookStatus(BookStatusRecord bookStatus) {
            this.bookStatus = bookStatus;
            return this;
        }

        public Builder insertDate(Date insertDate) {
            this.insertDate = insertDate;
            return this;
        }

        public Builder insertDate(LocalDateTime insertDate) {
            this.insertDate = Date.from(insertDate.toInstant(ZoneOffset.UTC));
            return this;
        }

        public Builder lastUpdateDate(Date lastUpdateDate) {
            this.lastUpdateDate = lastUpdateDate;
            return this;
        }

        public Builder lastUpdateDate(LocalDateTime lastUpdateDate) {
            this.lastUpdateDate = Date.from(lastUpdateDate.toInstant(ZoneOffset.UTC));
            return this;
        }

        public Builder lastChapter(Integer lastChapter) {
            this.lastChapter = lastChapter;
            return this;
        }

        public Builder bookType(BookType bookType) {
            this.bookType = bookType;
            return this;
        }

        public Builder previousBooks(BookChain previousBooks) {
            this.previousBooks = previousBooks;
            return this;
        }

        public Builder note(String note){
            this.note = note;
            return this;
        }

        public Builder readingRecords(@NonNull List<ReadingRecord> readingRecords){
            this.readingRecords = readingRecords;
            return this;
        }


        public BookImpl build() throws EmptyMandatoryParameterException {
            return new BookImpl(
                    bookId,
                    readListId,
                    title,
                    bookStatus,
                    insertDate,
                    lastUpdateDate,
                    lastChapter,
                    bookType,
                    previousBooks,
                    note,
                    readingRecords
            );
        }
    }
}
