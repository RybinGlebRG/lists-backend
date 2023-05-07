package ru.rerumu.lists.repository.impl;

import ru.rerumu.lists.exception.EmptyMandatoryParameterException;
import ru.rerumu.lists.mappers.BookMapper;
import ru.rerumu.lists.model.Book;
import ru.rerumu.lists.model.BookStatus;
import ru.rerumu.lists.model.BookType;
import ru.rerumu.lists.model.dto.BookDTO;
import ru.rerumu.lists.repository.BookRepository;
import ru.rerumu.lists.repository.CrudRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class BookRepositoryImpl implements BookRepository {

    private final BookMapper bookMapper;
    private final CrudRepository<BookType,Integer> crudRepository;

    public BookRepositoryImpl(
            BookMapper bookMapper,
            CrudRepository<BookType,Integer> crudRepository
    ){
        this.bookMapper = bookMapper;
        this.crudRepository = crudRepository;
    }


    @Override
    public void update(Book book) {
        bookMapper.update(
                book.getReadListId(),
                book.getBookId(),
                book.getTitle(),
                book.getBookStatus().statusId(),
                book.getInsertDate(),
                book.getLastUpdateDate(),
                book.getLastChapter().isPresent() ? book.getLastChapter().get() : null ,
                book.getBookType() != null ? book.getBookType().getId() : null
        );
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
            Optional<BookType> optionalBookType = crudRepository.findById(bookDTO.getBookType());
            optionalBookType.ifPresent(bookType -> bookDTO.bookTypeObj = bookType);
            try {
                return Optional.of(bookDTO.toBook());
            } catch (EmptyMandatoryParameterException e) {
                // Database is supposed to provide everything needed
                throw new AssertionError(e);
            }
        }
    }

    @Override
    public List<Book> getAll(Long readListId) {
        List<BookDTO> bookDTOList = bookMapper.getAll(readListId);
        List<Book> bookList = new ArrayList<>();
        for (BookDTO bookDTO: bookDTOList){
            if (bookDTO != null){
                Optional<BookType> optionalBookType = crudRepository.findById(bookDTO.getBookType());
                optionalBookType.ifPresent(bookType -> bookDTO.bookTypeObj = bookType);
                try {
                    bookList.add(bookDTO.toBook());
                } catch (EmptyMandatoryParameterException e) {
                    // Database is supposed to provide everything needed
                    throw new AssertionError(e);
                }
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
                book.getBookStatus().statusId(),
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
