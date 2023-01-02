package ru.rerumu.lists.model.dto;

import ru.rerumu.lists.model.BookStatus;
import ru.rerumu.lists.model.BookType;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.Optional;

public class BookDTO {
    private final Long bookId;
    private final Long readListId;
    private final String title;
    private final Integer bookStatus;
    private final Date insertDate;
    private final Date lastUpdateDate;
    private final Integer lastChapter;
    private final Integer bookType;

    public BookDTO(Long bookId,
                Long readListId,
                String title,
                Integer bookStatus,
                Date insertDate,
                Date lastUpdateDate,
                Integer lastChapter,
                Integer bookType)  {
        this.bookId = bookId;
        this.readListId = readListId;
        this.title = title;
        this.bookStatus = bookStatus;
        this.insertDate = insertDate;
        this.lastUpdateDate = lastUpdateDate;
        this.lastChapter = lastChapter;
        this.bookType = bookType;
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

    public Integer getBookStatus() {
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

    public Integer getBookType() {
        return bookType;
    }
}
