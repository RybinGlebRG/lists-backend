package ru.rerumu.lists.model.book;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import ru.rerumu.lists.model.BookStatusRecord;
import ru.rerumu.lists.model.base.EntityDTO;
import ru.rerumu.lists.model.book.impl.BookImpl;
import ru.rerumu.lists.model.book.readingrecords.ReadingRecordDTO;
import ru.rerumu.lists.model.book.type.BookTypeDTO;
import ru.rerumu.lists.model.dto.BookOrderedDTO;
import ru.rerumu.lists.model.series.item.SeriesItemDTO;
import ru.rerumu.lists.model.tag.TagDTO;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Builder(toBuilder = true)
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
    @Getter
    public String note;
    @Getter
    public BookTypeDTO bookTypeObj;
    @Getter
    public BookStatusRecord bookStatusObj;
    @Getter
    public List<BookOrderedDTO> previousBooks;
    @Setter
    @Getter
    public List<ReadingRecordDTO> readingRecords;
    @Getter
    public String URL;
    @Getter
    public Long userId;
    @Getter
    public List<TagDTO> tags;

    public BookDTO() {
    }


    public BookDTO(
            Long bookId,
            Long readListId,
            String title,
            Integer bookStatus,
            Date insertDate,
            Date lastUpdateDate,
            Integer lastChapter,
            Integer bookType,
            String note,
            BookTypeDTO bookTypeObj,
            BookStatusRecord bookStatusObj,
            @NonNull List<BookOrderedDTO> previousBooks,
            @NonNull List<ReadingRecordDTO> readingRecords,
            String URL, Long userId,
            @NonNull List<TagDTO> tags
    ) {
        this.bookId = bookId;
        this.readListId = readListId;
        this.title = title;
        this.bookStatus = bookStatus;
        this.insertDate = insertDate;
        this.lastUpdateDate = lastUpdateDate;
        this.lastChapter = lastChapter;
        this.bookType = bookType;
        this.note = note;
        this.bookTypeObj = bookTypeObj;
        this.bookStatusObj = bookStatusObj;
        this.previousBooks = previousBooks;
        this.readingRecords = readingRecords;
        this.URL = URL;
        this.userId = userId;
        this.tags = tags;
    }

    public BookDTO(
            Long bookId,
            Long readListId,
            String title,
            Integer bookStatus,
            Date insertDate,
            Date lastUpdateDate,
            Integer lastChapter,
            String note,
            BookTypeDTO bookTypeObj,
            BookStatusRecord bookStatusObj,
            String URL,
            Long userId
    ) {
        this.bookId = bookId;
        this.readListId = readListId;
        this.title = title;
        this.bookStatus = bookStatus;
        this.insertDate = insertDate;
        this.lastUpdateDate = lastUpdateDate;
        this.lastChapter = lastChapter;
        this.note = note;
        this.bookTypeObj = bookTypeObj;
        this.bookStatusObj = bookStatusObj;
        this.URL = URL;
        this.userId = userId;
    }

    public LocalDateTime getLastUpdateDate_V2() {
        return LocalDateTime.ofInstant(lastUpdateDate.toInstant(), ZoneOffset.UTC);
    }

    public LocalDateTime getLastInsertLocalDate() {
        return LocalDateTime.ofInstant(insertDate.toInstant(), ZoneOffset.UTC);
    }

    public Optional<Integer> getLastChapter() {
        return Optional.ofNullable(lastChapter);
    }

    @Override
    public BookImpl toDomain() {
        throw new RuntimeException("Not implemented");
    }
}
