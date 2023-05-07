package ru.rerumu.lists.model;

import org.json.JSONObject;
import ru.rerumu.lists.exception.EmptyMandatoryParameterException;
import ru.rerumu.lists.model.dto.BookDTO;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;

public record Book(
        Long bookId,
        Long readListId,
        String title,
        BookStatusRecord bookStatus,
        Date insertDate,
        Date lastUpdateDate,
        Integer lastChapter,
        BookType bookType
) implements Cloneable, SeriesItem{
    private final static String SERIES_ITEM_TYPE = "BOOK";
//    private final Long bookId;
//    private final Long readListId;
//    private final String title;
//    private final BookStatus bookStatus;
//    private final Date insertDate;
//    private final Date lastUpdateDate;
//    private final Integer lastChapter;
//    private final BookType bookType;

    public Book{
        Objects.requireNonNull(title ,"Book title cannot be null");
        Objects.requireNonNull(bookStatus,"Book status cannot be null");
        Objects.requireNonNull(insertDate,"Book insert date cannot be null");
        Objects.requireNonNull(lastUpdateDate,"Book last update date cannot be null");
    }


    public Book(Long bookId,
                Long readListId,
                String title,
                BookStatusRecord bookStatus,
                Date insertDate,
                Date lastUpdateDate,
                Integer lastChapter){
        this(bookId,readListId,title,bookStatus,insertDate,lastUpdateDate,lastChapter,null);
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
        bookStatusJson.put("statusId",bookStatus.statusId());
        bookStatusJson.put("statusName",bookStatus.statusName());
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

        if (bookType != null) {
            JSONObject bookTypeJson = new JSONObject();
            bookTypeJson.put("typeId", bookType.getId());
            bookTypeJson.put("typeName", bookType.getName());

            obj.put("bookType", bookTypeJson);
        }
        obj.put("itemType",SERIES_ITEM_TYPE);

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
        }

        // TODO: Move to DTO
        public Builder(BookDTO bookDTO) {
            this.bookId = bookDTO.getBookId();
            this.readListId = bookDTO.getReadListId();
            this.title = bookDTO.getTitle();
            this.insertDate = bookDTO.getInsertDate();
            this.lastUpdateDate = bookDTO.getLastUpdateDate();
            Optional<Integer> optionalLastChapter = bookDTO.getLastChapter();
            optionalLastChapter.ifPresent(item -> {this.lastChapter = optionalLastChapter.get();});
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

        public Builder bookStatus(BookStatusRecord bookStatus){
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

        public Builder bookType(BookType bookType){
            this.bookType = bookType;
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
                    bookType
            );
        }
    }
}
