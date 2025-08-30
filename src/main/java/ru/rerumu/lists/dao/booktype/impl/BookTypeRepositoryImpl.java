package ru.rerumu.lists.dao.booktype.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.rerumu.lists.crosscut.exception.EntityNotFoundException;
import ru.rerumu.lists.dao.base.impl.CrudRepositoryDictionaryImpl;
import ru.rerumu.lists.dao.booktype.BookTypeRepository;
import ru.rerumu.lists.dao.booktype.mapper.BookTypeMapper;
import ru.rerumu.lists.domain.booktype.BookType;

@Component
public class BookTypeRepositoryImpl extends CrudRepositoryDictionaryImpl<BookType, Long> implements BookTypeRepository {

    private final BookTypeMapper bookTypeMapper;

    @Autowired
    public BookTypeRepositoryImpl(BookTypeMapper bookTypeMapper) {
        super(bookTypeMapper);
        this.bookTypeMapper = bookTypeMapper;
    }

    @Override
    public BookType findById(Long bookTypeId) {
        BookType bookType = bookTypeMapper.findById(bookTypeId);

        if (bookType == null) {
            throw new EntityNotFoundException();
        } else {
            return bookType;
        }
    }
}
