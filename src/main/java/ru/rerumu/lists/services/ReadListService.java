package ru.rerumu.lists.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.rerumu.lists.exception.EmptyMandatoryParameterException;
import ru.rerumu.lists.exception.EntityNotFoundException;
import ru.rerumu.lists.factories.DateFactory;
import ru.rerumu.lists.model.*;
import ru.rerumu.lists.repository.*;
import ru.rerumu.lists.views.BookAddView;
import ru.rerumu.lists.views.BookUpdateView;

import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class ReadListService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    private final BookRepository bookRepository;

    private final SeriesRepository seriesRepository;

//    private final AuthorsRepository authorsRepository;

    private final AuthorsService authorsService;
    private final AuthorsBooksRepository authorsBooksRepository;
    private final SeriesBooksRespository seriesBooksRespository;

    private final DateFactory dateFactory;
    private final BookSeriesService bookSeriesService;

    private final BookSeriesRelationService bookSeriesRelationService;

    private final AuthorsBooksRelationService authorsBooksRelationService;

    public ReadListService(
            BookRepository bookRepository,
            SeriesRepository seriesRepository,
            AuthorsRepository authorsRepository,
            AuthorsService authorsService,
            AuthorsBooksRepository authorsBooksRepository,
            SeriesBooksRespository seriesBooksRespository,
            DateFactory dateFactory,
            BookSeriesService bookSeriesService,
            BookSeriesRelationService bookSeriesRelationService,
            AuthorsBooksRelationService authorsBooksRelationService
    ) {
        this.bookRepository = bookRepository;
        this.seriesRepository = seriesRepository;
//        this.authorsRepository = authorsRepository;
        this.authorsService = authorsService;
        this.authorsBooksRepository = authorsBooksRepository;
        this.seriesBooksRespository = seriesBooksRespository;
        this.dateFactory = dateFactory;
        this.bookSeriesService = bookSeriesService;
        this.bookSeriesRelationService = bookSeriesRelationService;
        this.authorsBooksRelationService = authorsBooksRelationService;
    }

    private void updateAuthor(long bookId, Long authorId, long readListId) {
        List<AuthorBookRelation> authorsBooksRepositoryList = authorsBooksRepository.getByBookId(bookId, readListId);

        Optional<Author> optionalAuthor = authorId != null ?
                authorsService.getAuthor(readListId, authorId):
                Optional.empty();

        authorsBooksRepositoryList.stream()
                .filter(item -> item.getBook().getBookId().equals(bookId) &&
                        item.getBook().getReadListId().equals(readListId) &&
                        (optionalAuthor.isEmpty() || !optionalAuthor.get().equals(item.getAuthor()))
                )
                .forEach(item -> authorsBooksRelationService.delete(
                        item.getBook().getBookId(),
                        item.getAuthor().getAuthorId(),
                        item.getBook().getReadListId()
                ));

        if(authorsBooksRepositoryList.stream()
                .noneMatch(item -> optionalAuthor.isPresent() &&
                        item.getAuthor().equals(optionalAuthor.get()) &&
                        item.getBook().getBookId().equals(bookId) &&
                        item.getBook().getReadListId().equals(readListId))
        ){
            optionalAuthor.ifPresent(author -> authorsBooksRepository.add(bookId, author.getAuthorId(), author.getReadListId()));
        }
    }

    private void updateSeries(long bookId, Long seriesId, long readListId, Long seriesOrder) {
        List<SeriesBookRelation> seriesBookRelationList = seriesBooksRespository.getByBookId(bookId, readListId);


        Optional<Series> optionalSeries = seriesId != null ?
                bookSeriesService.getSeries(readListId, seriesId) :
                Optional.empty();

        seriesBookRelationList.stream()
                .filter(item -> item.getBook().getBookId().equals(bookId) &&
                        item.getBook().getReadListId().equals(readListId) &&
                        (optionalSeries.isEmpty() || !optionalSeries.get().equals(item.getSeries()))
                )
                .forEach(item -> bookSeriesRelationService.delete(
                        item.getBook().getBookId(),
                        item.getSeries().getSeriesId(),
                        item.getBook().getReadListId()
                ));

        seriesBookRelationList.stream()
                .filter(item -> optionalSeries.isPresent() &&
                        item.getSeries().equals(optionalSeries.get()) &&
                        item.getBook().getBookId().equals(bookId) &&
                        item.getBook().getReadListId().equals(readListId)
                )
                .forEach(item -> bookSeriesRelationService.update(new SeriesBookRelation(
                        item.getBook(),
                        item.getSeries(),
                        seriesOrder
                )));

        if (seriesBookRelationList.stream().noneMatch(item -> optionalSeries.isPresent() &&
                optionalSeries.get().equals(item.getSeries()) &&
                item.getBook().getBookId().equals(bookId) &&
                item.getBook().getReadListId().equals(readListId))
        ) {
            optionalSeries.ifPresent(series -> seriesBooksRespository.add(
                    bookId,
                    series.getSeriesId(),
                    series.getSeriesListId(),
                    seriesOrder)
            );
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateBook(Long bookId, BookUpdateView bookUpdateView) throws EmptyMandatoryParameterException, CloneNotSupportedException {
        if (
                bookUpdateView == null
                        || bookUpdateView.getReadListId() == null
                        || bookId == null
        ) {
            throw new EmptyMandatoryParameterException();
        }

        Book currentBook = bookRepository.getOne(bookUpdateView.getReadListId(), bookId);

        Book.Builder builder = new Book.Builder(currentBook);

        builder.insertDate(Date.from(bookUpdateView.getInsertDateUTC().toInstant(ZoneOffset.UTC)))
                .lastChapter(bookUpdateView.getLastChapter())
//                .lastUpdateDate(new Date())
                .title(bookUpdateView.getTitle());

        switch (bookUpdateView.getStatus()) {
            case 1:
                builder.bookStatus(BookStatus.IN_PROGRESS);
                break;
            case 2:
                builder.bookStatus(BookStatus.COMPLETED);
                break;
            default:
                builder.bookStatus(null);
        }

        Book updatedBook = builder.build();


        bookRepository.update(updatedBook);

        updateAuthor(bookId, bookUpdateView.getAuthorId(), bookUpdateView.getReadListId());

        updateSeries(bookId, bookUpdateView.getSeriesId(), bookUpdateView.getReadListId(), bookUpdateView.getOrder());

    }

    public Book getBook(Long readListId, Long bookId) {
        Book book = this.bookRepository.getOne(readListId, bookId);
        logger.info(String.format("Got book '%s'", book.toString()));
        return book;
    }

    public List<Book> getAllBooks(Long readListId) {
        return this.bookRepository.getAll(readListId);
    }

    @Deprecated
    public Series getSeries(Long readListId, Long seriesId) {
        return seriesRepository.getOne(readListId, seriesId);
    }

    public List<Series> getAllSeries(Long readListId) {
        List<Series> seriesList = seriesRepository.getAll(readListId);
        for (Series item : seriesList) {
            int bookCount = seriesRepository.getBookCount(readListId, item.getSeriesId());
            item.setBookCount(bookCount);
        }
        return seriesList;
    }

    @Deprecated
    public List<Author> getAuthors(Long readListId) {
        return authorsService.getAuthors(readListId);
    }

    @Transactional(rollbackFor = Exception.class)
    public Book addBook(Long readListId, BookAddView bookAddView) throws EmptyMandatoryParameterException, EntityNotFoundException {

        Long bookId = bookRepository.getNextId();

        Date dt = dateFactory.getCurrentDate();

        Book.Builder bookBuilder = new Book.Builder();

        BookStatus bookStatus;
        switch (bookAddView.getStatus()) {
            case 1:
                bookStatus = BookStatus.IN_PROGRESS;
                break;
            case 2:
                bookStatus = BookStatus.COMPLETED;
                break;
            default:
                bookStatus = null;
        }

        bookBuilder
                .bookId(bookId)
                .readListId(readListId)
                .title(bookAddView.getTitle())
                .bookStatus(bookStatus)
                .insertDate(dt)
                .lastUpdateDate(dt)
                .lastChapter(bookAddView.getLastChapter());

        Book newBook = bookBuilder.build();

        bookRepository.addOne(newBook);

        if (bookAddView.getAuthorId() != null) {
            Optional<Author> author = authorsService.getAuthor(readListId, bookAddView.getAuthorId());
            if (author.isEmpty()) {
                throw new EntityNotFoundException();
            }
            author.ifPresent(value -> authorsBooksRepository.add(newBook.getBookId(), value.getAuthorId(), readListId));
        }

        if (bookAddView.getSeriesId() != null) {
            Optional<Series> series = bookSeriesService.getSeries(readListId, bookAddView.getSeriesId());
            if (series.isEmpty()) {
                throw new EntityNotFoundException();
            }
            series.ifPresent(value -> seriesBooksRespository.add(
                    newBook.getBookId(),
                    value.getSeriesId(),
                    readListId,
                    bookAddView.getOrder())
            );
        }


        return getBook(readListId, bookId);
    }
}
