package ru.rerumu.lists.model.book;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.rerumu.lists.model.BookStatusRecord;
import ru.rerumu.lists.model.book.type.BookTypeDTO;
import ru.rerumu.lists.model.books.reading_records.ReadingRecord;
import ru.rerumu.lists.model.dto.BookOrderedDTO;
import ru.rerumu.lists.model.dto.EntityDTO;
import ru.rerumu.lists.model.series.item.SeriesItemDTO;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Builder(toBuilder = true)
@AllArgsConstructor
@ToString
public class BookDTO implements EntityDTO<BookImpl>, SeriesItemDTO {
    @Getter
    public Long bookId;
    @Getter
    public Long readListId;
    @Getter
    public String title;
    @Getter
    public Integer bookStatus;
    @Getter
    public Date insertDate;
    @Getter
    public Date lastUpdateDate;
    public Integer lastChapter;
    @Getter
    public Integer bookType;
    public String note;
    public BookTypeDTO bookTypeObj;
    public BookStatusRecord bookStatusObj;
    @Getter
    public List<BookOrderedDTO> previousBooks;
    @Setter
    public List<ReadingRecord> readingRecords;

    public BookDTO() {
    }

    public LocalDateTime getLastUpdateDate_V2() {
        return LocalDateTime.ofInstant(lastUpdateDate.toInstant(), ZoneOffset.UTC);
    }

    public Optional<Integer> getLastChapter() {
        return Optional.ofNullable(lastChapter);
    }

    @Override
    public BookImpl toDomain() {
        throw new RuntimeException("Not implemented");
    }

//    @Override
//    public BookImpl toDomain() {
//        try {
//
//
//            BookBuilder builder = new BookBuilder()
//                    .bookId(bookId)
//                    .readListId(readListId)
//                    .title(title)
//                    .insertDate(insertDate)
//                    .lastUpdateDate(lastUpdateDate)
//                    .note(note)
//                    .readingRecords(readingRecords);
//
//            if (lastChapter != null) {
//                builder.lastChapter(lastChapter);
//            }
//            if (bookTypeObj != null) {
//                builder.bookType(bookTypeObj.toDomain());
//            }
//            builder.bookStatus(bookStatusObj);
//
//            if (previousBooks != null) {
//                HashMap<BookImpl,Integer> bookOrderMap = previousBooks.stream()
//                        .filter(Objects::nonNull)
//                        .map(item -> new AbstractMap.SimpleImmutableEntry<>(
//                                item.bookDTO.toDomain(),
//                                item.getOrder()
//                        ))
//                        .collect(
//                                HashMap::new,
//                                (map,item) -> map.put(item.getKey(),item.getValue()),
//                                HashMap::putAll
//                        );
//
//                builder.previousBooks(
//                       new BookChain(bookOrderMap)
//                );
//            }
//
//            BookImpl book = builder.build();
//            return book;
//        } catch (EmptyMandatoryParameterException e) {
//            // Database is supposed to provide everything needed
//            throw new AssertionException(e);
//        }
//    }

//    @Override
//    public String toString() {
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("bookId",bookId);
//        jsonObject.put("readListId",readListId);
//        jsonObject.put("title",title);
//        jsonObject.put("bookStatus",bookStatus);
//    }
}
