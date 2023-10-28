package ru.rerumu.lists.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.rerumu.lists.exception.EmptyMandatoryParameterException;
import ru.rerumu.lists.exception.EntityNotFoundException;
import ru.rerumu.lists.factories.DateFactory;
import ru.rerumu.lists.model.*;
import ru.rerumu.lists.model.books.Search;
import ru.rerumu.lists.repository.*;
import ru.rerumu.lists.views.BookAddView;
import ru.rerumu.lists.views.BookUpdateView;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
public class ReadListService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    private final BookRepository bookRepository;

//    private final SeriesRepository seriesRepository;

    private final AuthorsService authorsService;
    private final AuthorsBooksRepository authorsBooksRepository;
    private final SeriesBooksRespository seriesBooksRespository;

    private final DateFactory dateFactory;
//    private final SeriesService seriesService;

    private final BookSeriesRelationService bookSeriesRelationService;

    private final AuthorsBooksRelationService authorsBooksRelationService;

    private final BookTypesService bookTypesService;

    private final BookStatusesService bookStatusesService;

    public ReadListService(
            BookRepository bookRepository,
//            SeriesRepository seriesRepository,
//            AuthorsRepository authorsRepository,
            AuthorsService authorsService,
            AuthorsBooksRepository authorsBooksRepository,
            SeriesBooksRespository seriesBooksRespository,
            DateFactory dateFactory,
//            SeriesService seriesService,
            BookSeriesRelationService bookSeriesRelationService,
            AuthorsBooksRelationService authorsBooksRelationService,
            BookTypesService bookTypesService,
            BookStatusesService bookStatusesService
    ) {
        this.bookRepository = bookRepository;
//        this.seriesRepository = seriesRepository;
//        this.authorsRepository = authorsRepository;
        this.authorsService = authorsService;
        this.authorsBooksRepository = authorsBooksRepository;
        this.seriesBooksRespository = seriesBooksRespository;
        this.dateFactory = dateFactory;
//        this.seriesService = seriesService;
        this.bookSeriesRelationService = bookSeriesRelationService;
        this.authorsBooksRelationService = authorsBooksRelationService;
        this.bookTypesService = bookTypesService;
        this.bookStatusesService = bookStatusesService;
    }

    private void updateAuthor(long bookId, Long authorId, long readListId) {
        List<AuthorBookRelation> authorsBooksRepositoryList = authorsBooksRepository.getByBookId(bookId, readListId);

        Optional<Author> optionalAuthor = authorId != null ?
                authorsService.getAuthor(readListId, authorId) :
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

        if (authorsBooksRepositoryList.stream()
                .noneMatch(item -> optionalAuthor.isPresent() &&
                        item.getAuthor().equals(optionalAuthor.get()) &&
                        item.getBook().getBookId().equals(bookId) &&
                        item.getBook().getReadListId().equals(readListId))
        ) {
            optionalAuthor.ifPresent(author -> authorsBooksRepository.add(bookId, author.getAuthorId(), author.getReadListId()));
        }
    }

//    private void updateSeries(long bookId, Long seriesId, long readListId, Long seriesOrder, Book book) {
//        List<SeriesBookRelation> seriesBookRelationList = seriesBooksRespository.getByBookId(bookId, readListId);
//
////        List<Series> seriesList = seriesService.findByBook(book);
//
//
//        Optional<Series> optionalSeries = seriesId != null ?
//                seriesService.getSeries( seriesId) :
//                Optional.empty();
//
//        seriesBookRelationList.stream()
//                .filter(item -> item.book().getBookId().equals(bookId) &&
//                        item.book().getReadListId().equals(readListId) &&
//                        (optionalSeries.isEmpty() || !optionalSeries.get().equals(item.series()))
//                )
//                .forEach(item -> bookSeriesRelationService.delete(
//                        item.book().getBookId(),
//                        item.series().getSeriesId(),
//                        item.book().getReadListId()
//                ));
//
//        seriesBookRelationList.stream()
//                .filter(item -> optionalSeries.isPresent() &&
//                        item.series().equals(optionalSeries.get()) &&
//                        item.book().getBookId().equals(bookId) &&
//                        item.book().getReadListId().equals(readListId)
//                )
//                .forEach(item -> bookSeriesRelationService.update(new SeriesBookRelation(
//                        item.book(),
//                        item.series(),
//                        seriesOrder
//                )));
//
//        if (seriesBookRelationList.stream().noneMatch(item -> optionalSeries.isPresent() &&
//                optionalSeries.get().equals(item.series()) &&
//                item.book().getBookId().equals(bookId) &&
//                item.book().getReadListId().equals(readListId))
//        ) {
//            optionalSeries.ifPresent(series -> seriesBooksRespository.add(
//                    bookId,
//                    series.getSeriesId(),
//                    series.getSeriesListId(),
//                    seriesOrder)
//            );
//        }
//    }

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
                .title(bookUpdateView.getTitle());

        bookUpdateView.getLastChapter().ifPresent(builder::lastChapter);

        // TODO: Test + add other fields
        if (!bookUpdateView.getLastChapter().equals(currentBook.getLastChapter()) ||
                bookUpdateView.getStatus() != currentBook.getBookStatus().statusId()) {
            builder.lastUpdateDate(dateFactory.getLocalDateTime());
        }

        Objects.requireNonNull(bookUpdateView.getStatus(), "Book status cannot be null");
        builder.bookStatus(
                bookStatusesService.findById(bookUpdateView.getStatus()).orElseThrow()
        );

        if (bookUpdateView.getBookTypeId() != null) {
            Optional<BookType> optionalBookType = bookTypesService.findById(bookUpdateView.getBookTypeId());
            if (optionalBookType.isEmpty()) {
                throw new IllegalArgumentException();
            }

            optionalBookType.ifPresent(builder::bookType);
//            builder.bookType(
//                    new BookTypeOld.Builder()
//                            .typeId(bookUpdateView.getBookTypeId())
//                            .build()
//            );
        }

        builder.note(bookUpdateView.note());

        Book updatedBook = builder.build();

        logger.debug(String.format("Updated book: %s", updatedBook.toString()));

        bookRepository.update(updatedBook);

        updateAuthor(bookId, bookUpdateView.getAuthorId(), bookUpdateView.getReadListId());

//        updateSeries(bookId, bookUpdateView.getSeriesId(), bookUpdateView.getReadListId(), bookUpdateView.getOrder(),updatedBook);

    }

    public Book getBook(Long readListId, Long bookId) {
        Book book = this.bookRepository.getOne(readListId, bookId);
        logger.info(String.format("Got book '%s'", book.toString()));
        return book;
    }

    public Optional<Book> getBook(Long bookId) {
        return this.bookRepository.getOne(bookId);
    }

    public List<Book> getAllBooks(Long readListId, Search search) {
        List<Book> bookList;
        if (search.getChainBySeries()){
            bookList = bookRepository.getAllChained(readListId);
        } else {
            bookList = this.bookRepository.getAll(readListId);
        }
        logger.debug(bookList.toString());
        return bookList;
    }

    @Deprecated
    public List<Author> getAuthors(Long readListId) {
        return authorsService.getAuthors(readListId);
    }

    @Transactional(rollbackFor = Exception.class)
    public Book addBook(Long readListId, BookAddView bookAddView) throws EmptyMandatoryParameterException, EntityNotFoundException {
        Long bookId = bookRepository.getNextId();
        Book.Builder bookBuilder = new Book.Builder();

        bookBuilder
                .bookId(bookId)
                .readListId(readListId)
                .title(bookAddView.getTitle())
                .lastChapter(bookAddView.getLastChapter())
                .note(bookAddView.note());

        Objects.requireNonNull(bookAddView.status());
        bookBuilder.bookStatus(
                bookStatusesService.findById(bookAddView.status())
                        .orElseThrow()
        );

        if (bookAddView.insertDate() != null){
            bookBuilder.insertDate(bookAddView.insertDate());
            bookBuilder.lastUpdateDate(bookAddView.insertDate());
        } else {
            LocalDateTime tmp = dateFactory.getLocalDateTime();
            bookBuilder.insertDate(tmp);
            bookBuilder.lastUpdateDate(tmp);
        }


        if (bookAddView.getBookTypeId() != null) {
            bookBuilder.bookType(
                    bookTypesService.findById(bookAddView.getBookTypeId()).orElseThrow()
            );
        }

        Book newBook = bookBuilder.build();

        bookRepository.addOne(newBook);

        if (bookAddView.getAuthorId() != null) {
            authorsBooksRepository.add(
                    newBook.getBookId(),
                    authorsService.getAuthor(
                            readListId,
                            bookAddView.getAuthorId()
                    ).orElseThrow().getAuthorId(),
                    readListId
            );
        }

        return getBook(readListId, bookId);
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteBook(Long bookId) throws EntityNotFoundException {
        Optional<Book> bookOptional = bookRepository.getOne(bookId);
        if (bookOptional.isEmpty()) {
            throw new EntityNotFoundException();
        }

        seriesBooksRespository.getByBookId(
                        bookOptional.get().getBookId(),
                        bookOptional.get().getReadListId()
                )
                .forEach(item -> bookSeriesRelationService.delete(
                        item.book().getBookId(),
                        item.series().getSeriesId(),
                        item.book().getReadListId()
                ));

        authorsBooksRepository.getByBookId(
                        bookOptional.get().getBookId(),
                        bookOptional.get().getBookId()
                )
                .forEach(item -> authorsBooksRelationService.delete(
                        item.getBook().getBookId(),
                        item.getAuthor().getAuthorId(),
                        item.getBook().getReadListId()
                ));

        bookRepository.delete(bookOptional.get().getBookId());
    }
}
