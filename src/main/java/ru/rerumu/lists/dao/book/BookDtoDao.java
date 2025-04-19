package ru.rerumu.lists.dao.book;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ru.rerumu.lists.crosscut.exception.NotImplementedException;
import ru.rerumu.lists.dao.user.UserDtoDao;
import ru.rerumu.lists.model.book.readingrecords.ReadingRecordDTO;
import ru.rerumu.lists.model.book.readingrecords.status.BookStatusRecord;
import ru.rerumu.lists.model.book.type.BookTypeDTO;
import ru.rerumu.lists.model.series.item.SeriesItem;
import ru.rerumu.lists.model.series.item.SeriesItemDTO;
import ru.rerumu.lists.model.tag.TagDTO;

import java.util.Date;
import java.util.List;

@EqualsAndHashCode
@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BookDtoDao implements SeriesItemDTO {

    private Long bookId;
    private Long readListId;
    private String title;
    private Integer bookStatus;
    private Date insertDate;
    private Date lastUpdateDate;
    private Integer lastChapter;
    private Integer bookType;
    private String note;
    private BookTypeDTO bookTypeObj;
    private BookStatusRecord bookStatusObj;
    private List<BookOrderedDtoDao> previousBooks;
    private List<ReadingRecordDTO> readingRecords;
    private String URL;
    private Long userId;
    private List<TagDTO> tags;
    private UserDtoDao user;

    @Override
    public SeriesItem toDomain() {
        throw new NotImplementedException();
    }
}
