package ru.rerumu.lists.domain.book.impl;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.rerumu.lists.crosscut.utils.DateFactory;
import ru.rerumu.lists.dao.book.AuthorsBooksRepository;
import ru.rerumu.lists.domain.author.Author;
import ru.rerumu.lists.domain.book.Book;
import ru.rerumu.lists.domain.book.BookChain;
import ru.rerumu.lists.domain.book.BookFactory;
import ru.rerumu.lists.domain.booktype.BookType;
import ru.rerumu.lists.domain.readingrecord.ReadingRecord;
import ru.rerumu.lists.domain.readingrecord.impl.ReadingRecordFactory;
import ru.rerumu.lists.domain.series.Series;
import ru.rerumu.lists.domain.series.SeriesFactory;
import ru.rerumu.lists.domain.tag.Tag;
import ru.rerumu.lists.domain.user.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class BookFactoryImpl implements BookFactory {

    private final DateFactory dateFactory;
    private final ReadingRecordFactory readingRecordFactory;
    private final AuthorsBooksRepository authorsBooksRepository;
    private final SeriesFactory seriesFactory;

    @Autowired
    public BookFactoryImpl(
            DateFactory dateFactory,
            ReadingRecordFactory readingRecordFactory,
            AuthorsBooksRepository authorsBooksRepository,
            @NonNull SeriesFactory seriesFactory
    ) {
        this.dateFactory = dateFactory;
        this.readingRecordFactory = readingRecordFactory;
        this.authorsBooksRepository = authorsBooksRepository;
        this.seriesFactory = seriesFactory;
    }

    @Override
    @NonNull
    public Book build(
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
    ) {
        LocalDateTime actualInsertDate;
        if (insertDate != null) {
            actualInsertDate = insertDate;

        } else {
            LocalDateTime tmp = dateFactory.getLocalDateTime();
            actualInsertDate = tmp;
        }

        if (readingRecords == null) {
            readingRecords = new ArrayList<>();
        }

        if (tags == null) {
            tags = new ArrayList<>();
        }

        if (textAuthors == null) {
            textAuthors = new ArrayList<>();
        }

        if (seriesList == null) {
            seriesList = new ArrayList<>();
        }

        return new BookImpl(
                bookId,
                title,
                actualInsertDate,
                bookType,
                previousBooks,
                note,
                readingRecords,
                URL,
                user,
                tags,
                textAuthors,
                seriesList,
                dateFactory,
                readingRecordFactory,
                authorsBooksRepository,
                seriesFactory
        );
    }
}
