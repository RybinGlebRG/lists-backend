package ru.rerumu.lists.domain.book.impl;

import com.jcabi.aspects.Loggable;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.similarity.LevenshteinDistance;
import ru.rerumu.lists.crosscut.exception.EntityNotFoundException;
import ru.rerumu.lists.crosscut.exception.ServerException;
import ru.rerumu.lists.crosscut.utils.DateFactory;
import ru.rerumu.lists.domain.author.Author;
import ru.rerumu.lists.domain.book.Book;
import ru.rerumu.lists.domain.book.BookChain;
import ru.rerumu.lists.domain.booktype.BookType;
import ru.rerumu.lists.domain.readingrecord.ReadingRecord;
import ru.rerumu.lists.domain.readingrecord.impl.ReadingRecordFactory;
import ru.rerumu.lists.domain.series.Series;
import ru.rerumu.lists.domain.series.SeriesFactory;
import ru.rerumu.lists.domain.seriesitem.SeriesItemType;
import ru.rerumu.lists.domain.tag.Tag;
import ru.rerumu.lists.domain.user.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@ToString
@Slf4j
public class BookImpl implements Book{

    private final static SeriesItemType SERIES_ITEM_TYPE = SeriesItemType.BOOK;

    @Getter
    private final Long bookId;

    @Getter
    private String title;

    @Getter
    private LocalDateTime insertDate;

    @Getter
    private BookType bookType;

    @Getter
    private BookChain previousBooks;

    @Getter
    private String note;

    @Getter
    private List<ReadingRecord> readingRecords;

    @Getter
    private String URL;

    private final User user;

    @Getter
    private final List<Tag> tags;

    @Getter
    private final List<Author> textAuthors;

    @Getter
    private final List<Series> seriesList;


    private final ReadingRecordFactory readingRecordFactory;
    private final DateFactory dateFactory;
    private final LevenshteinDistance levenshteinDistance = new LevenshteinDistance();
    private final SeriesFactory seriesFactory;


    BookImpl(
            @NonNull Long bookId,
            @NonNull String title,
            @NonNull LocalDateTime insertDate,
            BookType bookType,
            BookChain previousBooks,
            String note,
            @NonNull List<ReadingRecord> readingRecords,
            String URL,
            @NonNull User user,
            @NonNull List<Tag> tags,
            @NonNull List<Author> textAuthors,
            @NonNull List<Series> seriesList,
            @NonNull DateFactory dateFactory,
            @NonNull ReadingRecordFactory readingRecordFactory,
            @NonNull SeriesFactory seriesFactory
    ) {
        this.bookId = bookId;
        this.title = title;
        this.insertDate = insertDate;
        this.bookType = bookType;
        this.previousBooks = previousBooks;
        this.note = note;
        this.readingRecords = new ArrayList<>(readingRecords);
        this.seriesList = new ArrayList<>(seriesList);
        this.URL = URL;
        this.user = user;
        this.tags = new ArrayList<>(tags);
        this.textAuthors = textAuthors;
        this.dateFactory = dateFactory;
        this.readingRecordFactory = readingRecordFactory;
        this.seriesFactory = seriesFactory;
    }

    @Override
    public boolean currentStatusEquals(Long statusId) {
        ReadingRecord readingRecord = readingRecords.stream()
                .max(Comparator.comparing(ReadingRecord::getStartDate))
                .orElseThrow(() -> new ServerException("Error while processing records"));

        return readingRecord.statusEquals(statusId);
    }

    @Override
    public void addReadingRecord(ReadingRecord readingRecord) {
        // TODO: Remove
        if (readingRecords == null){
            readingRecords = new ArrayList<>();
        }

        readingRecords.add(readingRecord);
    }

    @Override
    public Long getId() {
        return bookId;
    }

    @Override
    public User getUser() {
        return user;
    }

    @Override
    public LocalDateTime getAddedDate() {
        return insertDate;
    }

    @Override
    public void updateInsertDate(LocalDateTime insertDate) {
        this.insertDate = insertDate;
    }

    @Override
    public void updateTitle(String title) {
        this.title = title;
    }

    @Override
    public void updateNote(String note) {
        this.note = note;
    }

    @Override
    public void updateType(BookType bookType) {
        this.bookType = bookType;
    }

    @Override
    public void updateURL(String URL) {
        this.URL = URL;
    }

    @Override
    public void updateTags(@NonNull List<Tag> tags) {
        // Adding new tags
        List<Tag> tagsToAdd = tags.stream()
                .filter(tag -> !this.tags.contains(tag))
                .collect(Collectors.toCollection(ArrayList::new));
        for (Tag tag: tagsToAdd) {
            this.tags.add(tag);
        }

        // Removing tags
        List<Tag> tagsToRemove = this.tags.stream()
                .filter(tag -> !tags.contains(tag))
                .collect(Collectors.toCollection(ArrayList::new));
        for (Tag tag: tagsToRemove) {
            this.tags.remove(tag);
        }
    }

    /**
     * Updates series list
     */
    @Override
    @Loggable(value = Loggable.DEBUG, prepend = true, trim = false, logThis = true)
    public void updateSeries(@NonNull List<Series> seriesList) {
        // Add
        List<Series> seriesToAdd = seriesList.stream()
                .filter(item -> !this.seriesList.contains(item))
                .collect(Collectors.toCollection(ArrayList::new));
        log.debug("seriesToAdd: {}", seriesToAdd);
        for (Series series: seriesToAdd) {
            series.addBookRelation(bookId);
            this.seriesList.add(series);
        }

        // Remove
        List<Series> seriesToRemove = this.seriesList.stream()
                .filter(item -> !seriesList.contains(item))
                .collect(Collectors.toCollection(ArrayList::new));
        log.debug("seriesToRemove: {}", seriesToRemove);
        for (Series series: seriesToRemove) {
            series.removeBookRelation(bookId);
            this.seriesList.remove(series);
        }
    }

    @Override
    public void updateTextAuthors(List<Author> authors) {
        // Add new authors
        List<Author> authorsToAdd = authors.stream()
                .filter(author -> !textAuthors.contains(author))
                .collect(Collectors.toCollection(ArrayList::new));
        for (Author author: authorsToAdd) {
            textAuthors.add(author);
        }

        // Remove existing authors
        List<Author> authorsToRemove = textAuthors.stream()
                .filter(author -> !authors.contains(author))
                .collect(Collectors.toCollection(ArrayList::new));
        for (Author author: authorsToRemove) {
            textAuthors.remove(author);
        }
    }

    @Override
    public Float getTitleFuzzyMatchScore(String value) {
        if (title.equalsIgnoreCase(value)){
            return 1f;
        }

        List<String> titleSubstrings = new ArrayList<>();
        titleSubstrings.add(title);
        titleSubstrings.addAll(Arrays.asList(title.split(" ")));

        List<Float> scores = new ArrayList<>();
        for(String item: titleSubstrings){
            Integer distance = levenshteinDistance.apply(item.toUpperCase(), value.toUpperCase());
            Float score = (float) (item.length() - distance) / item.length();
            scores.add(score);
        }

        Float res = scores.stream().max(Float::compareTo).orElseThrow();
        return res;
    }

    public ReadingRecord deleteReadingRecord(Long readingRecordId){
        ReadingRecord readingRecord = readingRecords.stream()
                .filter(item -> item.getId().equals(readingRecordId))
                .findAny()
                .orElseThrow(EntityNotFoundException::new);

        readingRecords.remove(readingRecord);

        return readingRecord;
    }

    /**
     * Update reading records
     */
    @Override
    @Loggable(value = Loggable.DEBUG, trim = false, prepend = true)
    public void updateReadingRecords(List<ReadingRecord> readingRecords) {

        // Remove records
        List<ReadingRecord> recordsToDelete = this.readingRecords.stream()
                .filter(readingRecord -> !readingRecords.contains(readingRecord))
                .collect(Collectors.toCollection(ArrayList::new));
        for (ReadingRecord readingRecord: recordsToDelete) {
            this.readingRecords.remove(readingRecord);
        }

        // Add records
        for (ReadingRecord readingRecord: readingRecords) {
            if (!this.readingRecords.contains(readingRecord)) {
                this.readingRecords.add(readingRecord);
            }
        }
    }

    @Override
    public BookImpl deepCopy() {
        // TODO: Need actual deep copy
        return new BookImpl(
                bookId,
                title,
                insertDate,
                bookType,
                previousBooks,
                note,
                new ArrayList<>(readingRecords),
                URL,
                user,
                new ArrayList<>(tags),
                new ArrayList<>(textAuthors),
                new ArrayList<>(seriesList),
                dateFactory,
                readingRecordFactory,
                seriesFactory
        );
    }
}
