package ru.rerumu.lists.repository.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.rerumu.lists.exception.EmptyMandatoryParameterException;
import ru.rerumu.lists.mappers.BookMapper;
import ru.rerumu.lists.model.Book;
import ru.rerumu.lists.model.BookStatus;
import ru.rerumu.lists.model.BookType;
import ru.rerumu.lists.model.dto.BookDTO;
import ru.rerumu.lists.repository.BookRepository;
import ru.rerumu.lists.repository.BookTypeRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class BookRepositoryImpl implements BookRepository {

    private final BookMapper bookMapper;
    private final BookTypeRepository bookTypeRepository;

    public BookRepositoryImpl(
            BookMapper bookMapper,
            BookTypeRepository bookTypeRepository
    ){
        this.bookMapper = bookMapper;
        this.bookTypeRepository = bookTypeRepository;
    }


    @Override
    public void update(Book book) {
        bookMapper.update(
                book.getReadListId(),
                book.getBookId(),
                book.getTitle(),
                book.getBookStatus().getId(),
                book.getInsertDate(),
                book.getLastUpdateDate(),
                book.getLastChapter().isPresent() ? book.getLastChapter().get() : null ,
                book.getBookType() != null ? book.getBookType().getId() : null
        );
    }

    private Book populate(BookDTO bookDTO){
        Book.Builder builder = new Book.Builder(bookDTO);
        Optional<BookType> optionalBookType = bookTypeRepository.findById(bookDTO.getBookType());
        optionalBookType.ifPresent(builder::bookType);

        switch (bookDTO.getBookStatus()) {
            case 1:
                builder.bookStatus(BookStatus.IN_PROGRESS);
                break;
            case 2:
                builder.bookStatus(BookStatus.COMPLETED);
                break;
            default:
                builder.bookStatus(null);
        }
        try {
            return builder.build();
        } catch (EmptyMandatoryParameterException e){
            // Database is supposed to provide everything needed
            throw new AssertionError();
        }
    }

    @Deprecated
    @Override
    public Book getOne(Long readListId, Long bookId) {
        Optional<Book> optionalBook = getOne(bookId);
        if (optionalBook.isEmpty()){
            throw new IllegalArgumentException();
        }
        return optionalBook.get();
    }

    @Override
    public Optional<Book> getOne(Long bookId) {
        BookDTO bookDTO = bookMapper.getOne( bookId);
        if (bookDTO == null){
            return Optional.empty();
        } else {
            return Optional.of(populate(bookDTO));
        }
    }

    @Override
    public List<Book> getAll(Long readListId) {
        List<BookDTO> bookDTOList = bookMapper.getAll(readListId);
        List<Book> bookList = new ArrayList<>();
        for (BookDTO bookDTO: bookDTOList){
            if (bookDTO != null){
                bookList.add(populate(bookDTO));
            }
        }

        return bookList;
    }

    @Override
    public Long getNextId() {
        return bookMapper.getNextId();
    }

    @Override
    public void addOne(Book book) {
        bookMapper.addOne(
                book.getBookId(),
                book.getReadListId(),
                book.getTitle(),
                book.getBookStatus().getId(),
                book.getInsertDate(),
                book.getLastUpdateDate(),
                book.getLastChapter().isPresent() ? book.getLastChapter().get() : null,
                book.getBookType() != null ? book.getBookType().getId() : null
        );
    }

    @Override
    public void delete(Long bookId) {
        bookMapper.delete(bookId);
    }
}
