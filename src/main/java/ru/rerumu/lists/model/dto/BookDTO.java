package ru.rerumu.lists.model.dto;

import lombok.Builder;
import org.json.JSONObject;
import ru.rerumu.lists.exception.AssertionException;
import ru.rerumu.lists.exception.EmptyMandatoryParameterException;
import ru.rerumu.lists.model.*;
import ru.rerumu.lists.model.books.reading_records.ReadingRecord;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;

@Builder(toBuilder = true)
public class BookDTO implements EntityDTO<Book>, SeriesItemDTO {
    public Long bookId;
    public Long readListId;
    public String title;
    public Integer bookStatus;
    public Date insertDate;
    public Date lastUpdateDate;
    public Integer lastChapter;
    public Integer bookType;
    public String note;
    public BookTypeDTO bookTypeObj;
    public BookStatusRecord bookStatusObj;

    public List<BookOrderedDTO> previousBooks;
    public List<ReadingRecord> readingRecords;

    public BookDTO() {
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

    @Override
    public Book toDomain() {
        try {
            Book.Builder builder = new Book.Builder()
                    .bookId(bookId)
                    .readListId(readListId)
                    .title(title)
                    .insertDate(insertDate)
                    .lastUpdateDate(lastUpdateDate)
                    .note(note)
                    .readingRecords(readingRecords);

            if (lastChapter != null) {
                builder.lastChapter(lastChapter);
            }
            if (bookTypeObj != null) {
                builder.bookType(bookTypeObj.toDomain());
            }
            builder.bookStatus(bookStatusObj);

            if (previousBooks != null) {
                HashMap<Book,Integer> bookOrderMap = previousBooks.stream()
                        .filter(Objects::nonNull)
                        .map(item -> new AbstractMap.SimpleImmutableEntry<>(
                                item.bookDTO.toDomain(),
                                item.getOrder()
                        ))
                        .collect(
                                HashMap::new,
                                (map,item) -> map.put(item.getKey(),item.getValue()),
                                HashMap::putAll
                        );

                builder.previousBooks(
                       new BookChain(bookOrderMap)
                );
            }

            Book book = builder.build();
            return book;
        } catch (EmptyMandatoryParameterException e) {
            // Database is supposed to provide everything needed
            throw new AssertionException(e);
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
