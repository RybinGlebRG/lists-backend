package ru.rerumu.lists.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.rerumu.lists.exception.EmptyMandatoryParameterException;
import ru.rerumu.lists.model.Author;
import ru.rerumu.lists.model.Book;
import ru.rerumu.lists.model.Series;
import ru.rerumu.lists.repository.*;
import ru.rerumu.lists.views.BookAddView;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Component
public class ReadListService {
    private final Logger logger = LoggerFactory.getLogger(ReadListService.class);

    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private SeriesRepository seriesRepository;
    @Autowired
    private AuthorsRepository authorsRepository;

    private final AuthorsService authorsService;
    private final AuthorsBooksRepository authorsBooksRepository;
    private final SeriesBooksRespository seriesBooksRespository;

    public ReadListService(
            AuthorsService authorsService,
            AuthorsBooksRepository authorsBooksRepository,
            SeriesBooksRespository seriesBooksRespository
    ) {
        this.authorsService = authorsService;
        this.authorsBooksRepository = authorsBooksRepository;
        this.seriesBooksRespository = seriesBooksRespository;
    }

    @Transactional(rollbackFor = Exception.class)
    public Book updateBook(Long readListId, Long bookId, Book newBook) throws EmptyMandatoryParameterException {
        if (readListId == null || bookId == null || newBook == null) {
            throw new EmptyMandatoryParameterException();
        }
        Book currentBook = bookRepository.getOne(readListId, bookId);

        Book updatedBook = new Book.Builder(currentBook)
                .insertDate(newBook.getInsertDate())
                .lastChapter(newBook.getLastChapter())
                .lastUpdateDate(newBook.getLastUpdateDate())
                .statusId(newBook.getStatusId())
                .title(newBook.getTitle())
                .authorId(newBook.getAuthorId())
                .seriesId(newBook.getSeriesId())
                .seriesOrder(newBook.getSeriesOrder())
                .build();

        return bookRepository.update(updatedBook);
    }

    public Book getBook(Long readListId, Long bookId) {
        Book book = this.bookRepository.getOne(readListId, bookId);
        logger.info(String.format("Got book '%s'", book.toString()));
        return book;
    }

    public List<Book> getAllBooks(Long readListId) {
        return this.bookRepository.getAll(readListId);
    }

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

//    @Deprecated
//    public Author getAuthor(Long readListId, Long authorId) {
//        return authorsRepository.getOne(readListId, authorId);
//    }

    @Deprecated
    public List<Author> getAuthors(Long readListId) {
        return authorsRepository.getAll(readListId);
    }

    @Transactional(rollbackFor = Exception.class)
    public Book addBook(Long readListId, BookAddView bookAddView) throws EmptyMandatoryParameterException {
        Book book = bookAddView.getBook();

        Optional<Author> author = Optional.empty();
        if (bookAddView.getAuthorId() != null) {
            author = authorsService.getAuthor(
                    readListId,
                    bookAddView.getAuthorId()
            );
        }

        Optional<Series> series = Optional.empty();
        if (bookAddView.getSeriesId() != null) {
            series = Optional.of(
                    getSeries(readListId, bookAddView.getSeriesId())
            );
        }

        Long bookId = bookRepository.getNextId();

        Book.Builder bookBuilder = new Book.Builder();
        bookBuilder
                .bookId(bookId)
                .readListId(readListId)
                .title(book.getTitle())
                .statusId(book.getStatusId())
                .lastChapter(book.getLastChapter())
                .insertDate(new Date())
                .lastUpdateDate(new Date());

        Book newBook = bookBuilder.build();

        bookRepository.addOne(newBook);

        author.ifPresent(value -> authorsBooksRepository.add(newBook.getBookId(), value.getAuthorId(), readListId));
        series.ifPresent(value -> seriesBooksRespository.add(
                newBook.getBookId(),
                value.getSeriesId(),
                readListId,
                bookAddView.getOrder())
        );

        return getBook(readListId, newBook.getBookId());
    }
}
