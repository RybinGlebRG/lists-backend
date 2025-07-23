package ru.rerumu.lists.dao.book.impl;

import com.jcabi.aspects.Loggable;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import ru.rerumu.lists.crosscut.exception.EntityNotFoundException;
import ru.rerumu.lists.dao.author.AuthorDtoDao;
import ru.rerumu.lists.dao.book.AuthorBookDto;
import ru.rerumu.lists.dao.book.AuthorsBooksRepository;
import ru.rerumu.lists.dao.book.BookDtoDao;
import ru.rerumu.lists.dao.book.BookRepository;
import ru.rerumu.lists.dao.book.mapper.BookMapper;
import ru.rerumu.lists.dao.series.mapper.SeriesBookMapper;
import ru.rerumu.lists.dao.series.mapper.SeriesMapper;
import ru.rerumu.lists.domain.book.BookDTO;
import ru.rerumu.lists.domain.book.impl.BookImpl;
import ru.rerumu.lists.domain.user.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

// TODO: Refactor class
@Slf4j
public class BookRepositoryImpl implements BookRepository {

    private final BookMapper bookMapper;
    private final AuthorsBooksRepository authorsBooksRepository;
    private final SeriesMapper seriesMapper;
    private final SeriesBookMapper seriesBookMapper;

    public BookRepositoryImpl(
            BookMapper bookMapper,
            AuthorsBooksRepository authorsBooksRepository,
            SeriesMapper seriesMapper,
            SeriesBookMapper seriesBookMapper
    ) {
        this.bookMapper = bookMapper;
        this.authorsBooksRepository = authorsBooksRepository;
        this.seriesMapper = seriesMapper;
        this.seriesBookMapper = seriesBookMapper;
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
    public List<BookDtoDao> findByUserChained(Long userId) {

        // Load all books by user
        List<BookDtoDao> bookDtoList = bookMapper.findByUserChained(userId);

        // Load all relations between books and authors by user
        List<AuthorBookDto> authorBookDtoList = authorsBooksRepository.getAllByUserId(userId);
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
}
