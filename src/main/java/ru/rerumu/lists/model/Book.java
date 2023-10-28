package ru.rerumu.lists.model;

import org.json.JSONArray;
import org.json.JSONObject;
import ru.rerumu.lists.exception.EmptyMandatoryParameterException;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

public record Book(
        Long bookId,
        Long readListId,
        String title,
        BookStatusRecord bookStatus,
        Date insertDate,
        Date lastUpdateDate,
        Integer lastChapter,
        BookType bookType,

        BookChain previousBooks,

        String note
) implements Cloneable, SeriesItem {
    private final static SeriesItemType SERIES_ITEM_TYPE = SeriesItemType.BOOK;

    public Book {
        Objects.requireNonNull(title, "Book title cannot be null");
        Objects.requireNonNull(bookStatus, "Book status cannot be null");
        Objects.requireNonNull(insertDate, "Book insert date cannot be null");
        Objects.requireNonNull(lastUpdateDate, "Book last update date cannot be null");
    }


    public Book(Long bookId,
                Long readListId,
                String title,
                BookStatusRecord bookStatus,
                Date insertDate,
                Date lastUpdateDate,
                Integer lastChapter) {
        this(bookId, readListId, title, bookStatus, insertDate, lastUpdateDate, lastChapter, null, null, null);
    }

    public Long getReadListId() {
        return readListId;
    }

    public Long getBookId() {
        return bookId;
    }

    public String getTitle() {

        return title;
    }

    public BookStatusRecord getBookStatus() {
        return bookStatus;
    }

    public Date getInsertDate() {
        return insertDate;
    }

    public Date getLastUpdateDate() {
        return lastUpdateDate;
    }

    public LocalDateTime getLastUpdateDate_V2() {
        return LocalDateTime.ofInstant(lastUpdateDate.toInstant(), ZoneOffset.UTC);
    }

    public Optional<Integer> getLastChapter() {
        return Optional.ofNullable(lastChapter);
    }

    public BookType getBookType() {
        return bookType;
    }

    @Override
    public Book clone() {
        try {
            Book clone = (Book) super.clone();
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

        return obj;
    }

    @Override
    public LocalDateTime getUpdateDate() {
        return getLastUpdateDate_V2();
    }


    @Override
    public String toString() {
        return this.toJSONObject().toString();
    }

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

        public Builder() {
        }

        public Builder(Book book) {
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


        public Book build() throws EmptyMandatoryParameterException {
            return new Book(
                    bookId,
                    readListId,
                    title,
                    bookStatus,
                    insertDate,
                    lastUpdateDate,
                    lastChapter,
                    bookType,
                    previousBooks,
                    note
            );
        }
    }
}
