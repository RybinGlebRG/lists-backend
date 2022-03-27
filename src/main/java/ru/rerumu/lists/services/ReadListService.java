package ru.rerumu.lists.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.rerumu.lists.exception.EmptyMandatoryParameterException;
import ru.rerumu.lists.model.Author;
import ru.rerumu.lists.model.Book;
import ru.rerumu.lists.model.Series;
import ru.rerumu.lists.repository.AuthorsRepository;
import ru.rerumu.lists.repository.BookRepository;
import ru.rerumu.lists.repository.SeriesRepository;

import java.util.List;

@Component
public class ReadListService {

    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private SeriesRepository seriesRepository;
    @Autowired
    private AuthorsRepository authorsRepository;

    @Transactional(rollbackFor = Exception.class)
    public Book updateBook(Long readListId, Long bookId, Book newBook) throws EmptyMandatoryParameterException {
        if (readListId == null || bookId == null || newBook == null) {
            throw new EmptyMandatoryParameterException();
        }
        Book currentBook = this.bookRepository.getOne(readListId, bookId);

        currentBook.setInsertDate(newBook.getInsertDate());
        currentBook.setLastChapter(newBook.getLastChapter());
        currentBook.setLastUpdateDate(newBook.getLastUpdateDate());
        currentBook.setStatusId(newBook.getStatusId());
        currentBook.setTitle(newBook.getTitle());
        currentBook.setAuthorId(newBook.getAuthorId());
        currentBook.setSeriesId(newBook.getSeriesId());
        currentBook.setSeriesOrder(newBook.getSeriesOrder());

        return this.bookRepository.update(currentBook);
    }

    public Book getBook(Long readListId, Long bookId) {
        return this.bookRepository.getOne(readListId, bookId);
    }

    public List<Book> getAllBooks(Long readListId) {
        return this.bookRepository.getAll(readListId);
    }

    public Series getSeries(Long readListId, Long seriesId) {
        return seriesRepository.getOne(readListId, seriesId);
    }

    public List<Series> getAllSeries(Long readListId) {
        List<Series> seriesList =seriesRepository.getAll(readListId);
        for (Series item: seriesList){
            int bookCount = seriesRepository.getBookCount(readListId,item.getSeriesId());
            item.setBookCount(bookCount);
        }
        return seriesList;
    }

    public Author getAuthor(Long readListId, Long authorId) {
        return authorsRepository.getOne(readListId, authorId);
    }
}
