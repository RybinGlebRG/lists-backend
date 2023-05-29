package ru.rerumu.lists.model.dto;

import org.json.JSONObject;
import ru.rerumu.lists.exception.EmptyMandatoryParameterException;
import ru.rerumu.lists.model.*;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;

public class BookDTO implements EntityDTO<Book>, SeriesItemDTO {
    public Long bookId;
    public Long readListId;
    public String title;
    public Integer bookStatus;
    public Date insertDate;
    public Date lastUpdateDate;
    public Integer lastChapter;
    public Integer bookType;
    public BookTypeDTO bookTypeObj;
    public BookStatusRecord bookStatusObj;

    public List<BookDTO> previousBooks;

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

    @Override
    public Book toDomain() {
        try {
            Book.Builder builder = new Book.Builder();
            builder.bookId(bookId);
            builder.readListId(readListId);
            builder.title(title);
            builder.insertDate(insertDate);
            builder.lastUpdateDate(lastUpdateDate);
            if (lastChapter != null) {
                builder.lastChapter(lastChapter);
            }
            if (bookTypeObj != null) {
                builder.bookType(bookTypeObj.toDomain());
            }
            builder.bookStatus(bookStatusObj);

            builder.previousBooks(
                    previousBooks.stream()
                            .filter(Objects::nonNull)
                            .map(BookDTO::toDomain)
                            .collect(Collectors.toCollection(ArrayList::new))
            );

            Book book = builder.build();
            return book;
        } catch (EmptyMandatoryParameterException e) {
            // Database is supposed to provide everything needed
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
