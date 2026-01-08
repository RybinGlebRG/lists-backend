package ru.rerumu.lists.dao.book;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.rerumu.lists.crosscut.exception.NotImplementedException;
import ru.rerumu.lists.dao.author.AuthorDtoDao;
import ru.rerumu.lists.dao.readingrecord.ReadingRecordMyBatisEntity;
import ru.rerumu.lists.dao.series.SeriesMyBatisEntity;
import ru.rerumu.lists.dao.user.UserDtoDao;
import ru.rerumu.lists.domain.bookstatus.BookStatusRecord;
import ru.rerumu.lists.domain.booktype.BookType;
import ru.rerumu.lists.domain.series.item.SeriesItem;
import ru.rerumu.lists.domain.series.item.SeriesItemDTO;
import ru.rerumu.lists.domain.tag.TagDTO;

import java.time.LocalDateTime;
import java.util.List;

@EqualsAndHashCode
@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BookMyBatisEntity implements SeriesItemDTO {

    private Long bookId;
    private Long readListId;
    private String title;
    private Integer bookStatus;
    private LocalDateTime insertDate;
    private LocalDateTime lastUpdateDate;
    private Integer lastChapter;
    private Integer bookType;
    private String note;
    private BookType bookTypeObj;
    private BookStatusRecord bookStatusObj;
    private List<BookOrderedDtoDao> previousBooks;

    @Setter
    private List<ReadingRecordMyBatisEntity> readingRecords;

    private String URL;
    private Long userId;
    private List<TagDTO> tags;
    private UserDtoDao user;

    @Setter
    private List<Long> seriesIds;

    @Setter
    private List<SeriesMyBatisEntity> seriesList;

    @Setter
    private List<AuthorDtoDao> textAuthors;

    @Override
    public SeriesItem toDomain() {
        throw new NotImplementedException();
    }
}
