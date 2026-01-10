package ru.rerumu.lists.domain.book;

import lombok.NonNull;
import ru.rerumu.lists.domain.author.Author;
import ru.rerumu.lists.domain.booktype.BookType;
import ru.rerumu.lists.domain.readingrecord.ReadingRecord;
import ru.rerumu.lists.domain.series.Series;
import ru.rerumu.lists.domain.tag.Tag;
import ru.rerumu.lists.domain.user.User;

import java.time.LocalDateTime;
import java.util.List;

public interface BookFactory {

    @NonNull
    Book build(
            @NonNull Long bookId,
            @NonNull String title,
            LocalDateTime insertDate,
            BookType bookType,
            BookChain previousBooks,
            String note,
            List<ReadingRecord> readingRecords,
            String URL,
            @NonNull User user,
            List<Tag> tags,
            List<Author> textAuthors,
            List<Series> seriesList
    );
}
