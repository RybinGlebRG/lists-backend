package ru.rerumu.lists.dao.book;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ru.rerumu.lists.model.book.readingrecords.ReadingRecordDTO;
import ru.rerumu.lists.model.book.readingrecords.status.BookStatusRecord;
import ru.rerumu.lists.model.book.type.BookTypeDTO;
import ru.rerumu.lists.model.dto.BookOrderedDTO;
import ru.rerumu.lists.model.tag.TagDTO;

import java.util.Date;
import java.util.List;

@EqualsAndHashCode
@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BookDtoDao {

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
    public List<ReadingRecordDTO> readingRecords;
    public String URL;
    public Long userId;
    public List<TagDTO> tags;

}
