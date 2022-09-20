package ru.rerumu.lists.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.rerumu.lists.exception.EmptyMandatoryParameterException;
import ru.rerumu.lists.exception.EntityNotFoundException;
import ru.rerumu.lists.factories.DateFactory;
import ru.rerumu.lists.model.Author;
import ru.rerumu.lists.model.AuthorBookRelation;
import ru.rerumu.lists.model.Book;
import ru.rerumu.lists.model.Series;
import ru.rerumu.lists.repository.*;
import ru.rerumu.lists.views.BookAddView;
import ru.rerumu.lists.views.BookUpdateView;

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

    public ReadListService(
            BookRepository bookRepository,
            SeriesRepository seriesRepository,
            AuthorsRepository authorsRepository,
            AuthorsService authorsService,
            AuthorsBooksRepository authorsBooksRepository,
            SeriesBooksRespository seriesBooksRespository,
            DateFactory dateFactory,
            BookSeriesService bookSeriesService
    ) {
        this.bookRepository = bookRepository;
        this.seriesRepository = seriesRepository;
//        this.authorsRepository = authorsRepository;
        this.authorsService = authorsService;
        this.authorsBooksRepository = authorsBooksRepository;
        this.seriesBooksRespository = seriesBooksRespository;
        this.dateFactory = dateFactory;
        this.bookSeriesService = bookSeriesService;
    }

//    @Transactional(rollbackFor = Exception.class)
//    public Book updateBook(Long readListId, Long bookId, Book newBook) throws EmptyMandatoryParameterException {
//        if (readListId == null || bookId == null || newBook == null) {
//            throw new EmptyMandatoryParameterException();
//        }
//        Book currentBook = bookRepository.getOne(readListId, bookId);
//
//        Book updatedBook = new Book.Builder(currentBook)
//                .insertDate(newBook.getInsertDate())
//                .lastChapter(newBook.getLastChapter())
//                .lastUpdateDate(newBook.getLastUpdateDate())
//                .statusId(newBook.getStatusId())
//                .title(newBook.getTitle())
//                .authorId(newBook.getAuthorId())
//                .seriesId(newBook.getSeriesId())
//                .seriesOrder(newBook.getSeriesOrder())
//                .build();
//
//        return bookRepository.update(updatedBook);
//    }

    @Transactional(rollbackFor = Exception.class)
    public void updateBook(BookUpdateView bookUpdateView) throws EmptyMandatoryParameterException {
        if (
                bookUpdateView == null
                || bookUpdateView.getReadListId() == null
                || bookUpdateView.getBookId() == null
        ) {
            throw new EmptyMandatoryParameterException();
        }

        Book currentBook = bookRepository.getOne(bookUpdateView.getReadListId(), bookUpdateView.getBookId());

        Book updatedBook = new Book.Builder(currentBook)
//                .insertDate(newBook.getInsertDate())
//                .lastChapter(newBook.getLastChapter())
//                .lastUpdateDate(newBook.getLastUpdateDate())
                .statusId(bookUpdateView.getStatus())
                .title(bookUpdateView.getTitle())
//                .authorId(newBook.getAuthorId())
//                .seriesId(newBook.getSeriesId())
//                .seriesOrder(newBook.getSeriesOrder())
                .build();

        bookRepository.update(updatedBook);

        List<AuthorBookRelation> authorsBooksRepositoryList = authorsBooksRepository.getByBookId(bookUpdateView.getBookId());

        if (bookUpdateView.getAuthorId() != null) {
            Optional<Author> optionalAuthor = authorsService.getAuthor(bookUpdateView.getReadListId(), bookUpdateView.getAuthorId());
            authorsBooksRepositoryList.stream()
                    .map(AuthorBookRelation::getAuthor)
                    .filter(item -> optionalAuthor.isEmpty() || !optionalAuthor.get().equals(item))
                    .forEach(author -> authorsBooksRepository.deleteByAuthor(author.getAuthorId()));

            optionalAuthor.ifPresent(author -> authorsBooksRepository.add(updatedBook.getBookId(), author.getAuthorId(), author.getReadListId()));

        } else {
            authorsBooksRepositoryList.stream()
                    .map(AuthorBookRelation::getAuthor)
                    .forEach(author -> authorsBooksRepository.deleteByAuthor(author.getAuthorId()));
        }

        // TODO: Same for series

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

//    @Deprecated
//    public Author getAuthor(Long readListId, Long authorId) {
//        return authorsRepository.getOne(readListId, authorId);
//    }

    @Deprecated
    public List<Author> getAuthors(Long readListId) {
        return authorsService.getAuthors(readListId);
    }

    @Transactional(rollbackFor = Exception.class)
    public Book addBook(Long readListId, BookAddView bookAddView) throws EmptyMandatoryParameterException, EntityNotFoundException {

        Long bookId = bookRepository.getNextId();

        Date dt = dateFactory.getCurrentDate();

        Book.Builder bookBuilder = new Book.Builder();

        // TODO: Add last chapter
        bookBuilder
                .bookId(bookId)
                .readListId(readListId)
                .title(bookAddView.getTitle())
                .statusId(bookAddView.getStatus())
                .insertDate(dt)
                .lastUpdateDate(dt);

        Book newBook = bookBuilder.build();

        bookRepository.addOne(newBook);

        if (bookAddView.getAuthorId() != null) {
            Optional<Author> author = authorsService.getAuthor(readListId, bookAddView.getAuthorId());
            if (author.isEmpty()){
                throw new EntityNotFoundException();
            }
            author.ifPresent(value -> authorsBooksRepository.add(newBook.getBookId(), value.getAuthorId(), readListId));
        }

        if (bookAddView.getSeriesId() != null) {
            Optional<Series> series = bookSeriesService.getSeries(readListId, bookAddView.getSeriesId());
            if (series.isEmpty()){
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
