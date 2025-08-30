package ru.rerumu.lists.domain.book;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import ru.rerumu.lists.crosscut.exception.NotImplementedException;
import ru.rerumu.lists.domain.author.AuthorDTO;
import ru.rerumu.lists.domain.base.EntityDTO;
import ru.rerumu.lists.domain.book.impl.BookImpl;
import ru.rerumu.lists.domain.book.readingrecords.ReadingRecordDTO;
import ru.rerumu.lists.domain.book.readingrecords.status.BookStatusRecord;
import ru.rerumu.lists.domain.booktype.BookTypeDTO;
import ru.rerumu.lists.domain.dto.BookOrderedDTO;
import ru.rerumu.lists.domain.series.SeriesDTOv2;
import ru.rerumu.lists.domain.series.item.SeriesItemDTO;
import ru.rerumu.lists.domain.series.item.SeriesItemDTOv2;
import ru.rerumu.lists.domain.tag.TagDTO;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Builder(toBuilder = true)
@ToString
@EqualsAndHashCode
@Getter
public class BookDTO implements EntityDTO<BookImpl>, SeriesItemDTO, SeriesItemDTOv2 {
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
    public Date lastUpdateDate;
    public Integer lastChapter;
    @Getter
    public Long bookType;
    @Getter
    public String note;
    @Getter
    public BookTypeDTO bookTypeObj;
    @Getter
    public BookStatusRecord bookStatusObj;
    public List<BookOrderedDTO> previousBooks;
    @Setter
    public List<ReadingRecordDTO> readingRecords;
    @Getter
    public String URL;
    @Getter
    public Long userId;
    public List<TagDTO> tags;

    @Getter
    private List<AuthorDTO> textAuthors;

    private List<SeriesDTOv2> seriesList;

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
            Long bookType,
            String note,
            BookTypeDTO bookTypeObj,
            BookStatusRecord bookStatusObj,
            @NonNull List<BookOrderedDTO> previousBooks,
            @NonNull List<ReadingRecordDTO> readingRecords,
            String URL, Long userId,
            @NonNull List<TagDTO> tags,
            List<AuthorDTO> textAuthors,
            @NonNull List<SeriesDTOv2> seriesList
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
        this.textAuthors = textAuthors;
        this.seriesList = seriesList;
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

    @NonNull
    public List<BookOrderedDTO> getPreviousBooks() {
        return Objects.requireNonNullElseGet(previousBooks, ArrayList::new);
    }

    @NonNull
    public List<ReadingRecordDTO> getReadingRecords() {
        return Objects.requireNonNullElseGet(readingRecords, ArrayList::new);
    }

    @NonNull
    public List<TagDTO> getTags() {
        return Objects.requireNonNullElseGet(tags, ArrayList::new);
    }

    @Override
    public BookImpl toDomain() {
        throw new NotImplementedException();
    }

    @Override
    public LocalDateTime getLastUpdateDate() {
        throw new NotImplementedException();
    }
}
