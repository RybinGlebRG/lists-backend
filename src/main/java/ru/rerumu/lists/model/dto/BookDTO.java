package ru.rerumu.lists.model.dto;

import org.json.JSONObject;
import ru.rerumu.lists.exception.EmptyMandatoryParameterException;
import ru.rerumu.lists.model.*;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.Optional;

public class BookDTO implements EntityDTO<Book>, SeriesItemDTO {
    public Long bookId;
    public Long readListId;
    public String title;
    public Integer bookStatus;
    public Date insertDate;
    public Date lastUpdateDate;
    public Integer lastChapter;
    public Integer bookType;
    public BookType bookTypeObj;
    public BookStatusRecord bookStatusObj;

    public BookDTO() {
    }

//    public BookDTO(Long bookId,
//                Long readListId,
//                String title,
//                Integer bookStatus,
//                Date insertDate,
//                Date lastUpdateDate,
//                Integer lastChapter,
//                Integer bookType)  {
//        this.bookId = bookId;
//        this.readListId = readListId;
//        this.title = title;
//        this.bookStatus = bookStatus;
//        this.insertDate = insertDate;
//        this.lastUpdateDate = lastUpdateDate;
//        this.lastChapter = lastChapter;
//        this.bookType = bookType;
//    }

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

    public Book toBook() throws EmptyMandatoryParameterException {
        Book.Builder builder = new Book.Builder(this);
        builder.bookType(bookTypeObj);
        builder.bookStatus(bookStatusObj);

        return builder.build();
    }

    @Override
    public Book toDomain() {
        try {
            return toBook();
        } catch (EmptyMandatoryParameterException e) {
            throw new AssertionError(e);
        }
    }

//    @Override
//    public String toString() {
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("bookId",bookId);
//        jsonObject.put("readListId",readListId);
//        jsonObject.put("title",title);
//        jsonObject.put("bookStatus",bookStatus);
//    }
}
