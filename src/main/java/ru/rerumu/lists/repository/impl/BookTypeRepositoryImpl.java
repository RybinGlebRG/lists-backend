package ru.rerumu.lists.repository.impl;

import org.springframework.stereotype.Component;
import ru.rerumu.lists.mappers.BookTypeMapper;
import ru.rerumu.lists.model.BookType;
import ru.rerumu.lists.repository.BookTypeRepository;

import java.util.List;
import java.util.Optional;

@Component
public class BookTypeRepositoryImpl extends CrudRepositoryImplEntity<BookType,Integer> implements BookTypeRepository {

    private final BookTypeMapper bookTypeMapper;
    public BookTypeRepositoryImpl(BookTypeMapper bookTypeMapper) {
        super(bookTypeMapper);
        this.bookTypeMapper = bookTypeMapper;
    }

    @Override
    public Optional<BookType> findById(Integer bookTypeId) {
        return Optional.ofNullable(bookTypeMapper.findById(bookTypeId));
    }

    @Override
    public List<BookType> findAll() {
        return bookTypeMapper.findAll();
    }
}
