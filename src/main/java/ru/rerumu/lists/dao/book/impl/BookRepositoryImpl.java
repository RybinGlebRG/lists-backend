package ru.rerumu.lists.dao.book.impl;

import com.jcabi.aspects.Loggable;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import ru.rerumu.lists.crosscut.exception.EntityNotFoundException;
import ru.rerumu.lists.crosscut.exception.ServerException;
import ru.rerumu.lists.dao.author.AuthorDtoDao;
import ru.rerumu.lists.dao.book.AuthorBookDto;
import ru.rerumu.lists.dao.book.AuthorsBooksRepository;
import ru.rerumu.lists.dao.book.BookDtoDao;
import ru.rerumu.lists.dao.book.BookRepository;
import ru.rerumu.lists.dao.book.mapper.BookMapper;
import ru.rerumu.lists.dao.series.SeriesBooksRespository;
import ru.rerumu.lists.dao.series.SeriesRepository;
import ru.rerumu.lists.dao.series.mapper.SeriesMapper;
import ru.rerumu.lists.domain.base.EntityState;
import ru.rerumu.lists.domain.book.Book;
import ru.rerumu.lists.domain.book.BookDTO;
import ru.rerumu.lists.domain.book.impl.BookImpl;
import ru.rerumu.lists.domain.book.impl.BookPersistenceProxy;
import ru.rerumu.lists.domain.readingrecords.ReadingRecord;
import ru.rerumu.lists.domain.readingrecords.impl.ReadingRecordPersistenceProxy;
import ru.rerumu.lists.domain.series.Series;
import ru.rerumu.lists.domain.series.SeriesBookRelationDto;
import ru.rerumu.lists.domain.series.SeriesDTOv2;
import ru.rerumu.lists.domain.user.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

// TODO: Refactor class
@Slf4j
public class BookRepositoryImpl implements BookRepository {

    private final BookMapper bookMapper;
    private final AuthorsBooksRepository authorsBooksRepository;
    private final SeriesMapper seriesMapper;
    private final SeriesBooksRespository seriesBooksRespository;
    private final SeriesRepository seriesRepository;

    public BookRepositoryImpl(
            BookMapper bookMapper,
            AuthorsBooksRepository authorsBooksRepository,
            SeriesMapper seriesMapper,
            SeriesBooksRespository seriesBooksRespository, SeriesRepository seriesRepository
    ) {
        this.bookMapper = bookMapper;
        this.authorsBooksRepository = authorsBooksRepository;
        this.seriesMapper = seriesMapper;
        this.seriesBooksRespository = seriesBooksRespository;
        this.seriesRepository = seriesRepository;
    }


    @Override
    public void update(BookImpl book) {
        BookDTO bookDTO = book.toDTO();

        bookMapper.update(
                book.getReadListId(),
                book.getBookId(),
                book.getTitle(),
                book.getBookStatus() != null ? book.getBookStatus().statusId() : null,
                book.getInsertDate(),
                book.getLastUpdateDate(),
                book.getLastChapter().isPresent() ? book.getLastChapter().get() : null,
                book.getBookType() != null ? book.getBookType().getId() : null,
                book.getNote(),
                bookDTO.URL,
                book.getUser().userId()
        );
    }

    /**
     * Find books by user and chain then by series. Last book in series is the head of a chain.
     */
    @Override
    @Loggable(value = Loggable.DEBUG, trim = false, prepend = true)
    public List<BookDtoDao> findByUserChained(User user) {

        // Load all books by user
        List<BookDtoDao> bookDtoList = bookMapper.findByUserChained(user.userId());

        // Load all relations between books and authors by user
        List<AuthorBookDto> authorBookDtoList = authorsBooksRepository.getAllByUserId(user.userId());
        Map<Long, List<AuthorBookDto>> authorsMap = authorBookDtoList.stream()
                .collect(Collectors.groupingBy(
                        AuthorBookDto::getBookId,
                        HashMap::new,
                        Collectors.toCollection(ArrayList::new)
                ));

        // Add authors to corresponding books
        for (BookDtoDao item: bookDtoList) {
            List<AuthorBookDto> dtoList = authorsMap.get(item.getBookId());

            List<AuthorDtoDao> authorDtoDaoList = new ArrayList<>();
            if (dtoList != null) {
                authorDtoDaoList = dtoList.stream()
                        .map(AuthorBookDto::getAuthorDtoDao)
                        .collect(Collectors.toCollection(ArrayList::new));
            }
            item.setTextAuthors(authorDtoDaoList);
        }

        // Loading all series
        List<SeriesDTOv2> seriesDTOList = seriesRepository.findByUser(user);
        for (BookDtoDao bookDtoDao: bookDtoList) {

            // Getting list of series that contain book
            List<SeriesDTOv2> containsBook = new ArrayList<>();
            for (SeriesDTOv2 seriesDTO: seriesDTOList) {
                for (SeriesBookRelationDto seriesBookRelationDto: seriesDTO.getSeriesBookRelationDtoList()) {
                    if (seriesBookRelationDto.getBookId().equals(bookDtoDao.getBookId())) {
                        containsBook.add(seriesDTO);
                        break;
                    }
                }
            }

            bookDtoDao.setSeriesList(containsBook);
        }

        return bookDtoList;
    }

    @Override
    @Loggable(value = Loggable.DEBUG, trim = false, prepend = true)
    @NonNull
    public BookDtoDao findById(Long id, Long userId) {
        BookDtoDao book = bookMapper.findById(id, userId);

        if (book == null) {
            throw new EntityNotFoundException();
        }

        List<Long> seriesIds= bookMapper.findSeriesIds(book.getBookId());
        book.setSeriesIds(seriesIds);

        if (!book.getSeriesIds().isEmpty()) {
            List<SeriesDTOv2> seriesDTOList = seriesRepository.findByIds(book.getSeriesIds(), userId);
            book.setSeriesList(seriesDTOList);
        }

        return book;
    }

    @Override
    @Loggable(value = Loggable.DEBUG, trim = false, prepend = true)
    public List<BookDtoDao> findByUser(User user) {
        return bookMapper.findByUser(user);
    }

    @Override
    public Long getNextId() {
        return bookMapper.getNextId();
    }

    @Override
    public void addOne(BookImpl book) {
        BookDTO bookDTO = book.toDTO();

        bookMapper.addOne(
                book.getBookId(),
                book.getReadListId(),
                book.getTitle(),
                book.getBookStatus().statusId(),
                book.getInsertDate(),
                book.getLastUpdateDate(),
                book.getLastChapter().isPresent() ? book.getLastChapter().get() : null,
                book.getBookType() != null ? book.getBookType().getId() : null,
                book.getNote(),
                bookDTO.URL,
                bookDTO.userId
        );
    }

    @Override
    public void delete(Long bookId) {
        bookMapper.delete(bookId);
    }

    @Override
    public Optional<User> getBookUser(Long bookId) {
        return Optional.ofNullable(bookMapper.getBookUser(bookId));
    }

    @Override
    public void save(Book book) {
        if (book instanceof BookPersistenceProxy bookPersistenceProxy) {

            // Save book
            log.debug("Saving book...");
            bookPersistenceProxy.save();
            bookPersistenceProxy.setEntityState(EntityState.PERSISTED);
            bookPersistenceProxy.initPersistedCopy();

            // Save series
            log.debug("Saving series...");
            // collect series from original and current entities
            Set<Series> seriesSet = new HashSet<>();
            seriesSet.addAll(bookPersistenceProxy.getSeriesList());
            seriesSet.addAll(bookPersistenceProxy.getPersistedCopy().getSeriesList());
            // save collected entities
            for (Series series: seriesSet) {
                series.save();
            }

            // Delete removed reading records
            List<ReadingRecord> readingRecordsToDelete = bookPersistenceProxy.getPersistedCopy().getReadingRecords().stream()
                    .filter(item -> !book.getReadingRecords().contains(item))
                    .collect(Collectors.toCollection(ArrayList::new));
            for (ReadingRecord readingRecord: readingRecordsToDelete) {
                if (readingRecord instanceof ReadingRecordPersistenceProxy readingRecordPersistenceProxy) {
                    readingRecordPersistenceProxy.delete();
                    readingRecordPersistenceProxy.setEntityState(EntityState.DELETED);
                    readingRecordPersistenceProxy.clearPersistedCopy();
                } else {
                    throw new ServerException();
                }
            }
            // Save reading records
            for (ReadingRecord readingRecord: book.getReadingRecords()) {
                if (readingRecord instanceof ReadingRecordPersistenceProxy readingRecordPersistenceProxy) {
                    readingRecordPersistenceProxy.save();
                    readingRecordPersistenceProxy.setEntityState(EntityState.PERSISTED);
                    readingRecordPersistenceProxy.initPersistedCopy();
                } else {
                    throw new ServerException();
                }
            }

        } else {
            throw new ServerException();
        }
    }
}
