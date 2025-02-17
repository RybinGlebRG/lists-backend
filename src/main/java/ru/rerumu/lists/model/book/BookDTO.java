package ru.rerumu.lists.model.book;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.rerumu.lists.model.BookStatusRecord;
import ru.rerumu.lists.model.book.reading_records.ReadingRecordDTO;
import ru.rerumu.lists.model.book.type.BookTypeDTO;
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
    public List<ReadingRecordDTO> readingRecords;
    @Getter
    public String URL;
    @Getter
    public Long userId;

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
            String note,
            BookTypeDTO bookTypeObj,
            BookStatusRecord bookStatusObj,
            List<BookOrderedDTO> previousBooks,
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
        this.previousBooks = previousBooks;
        this.URL = URL;
        this.userId = userId;
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
}
