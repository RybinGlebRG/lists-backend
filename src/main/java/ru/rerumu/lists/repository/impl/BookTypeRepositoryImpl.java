package ru.rerumu.lists.repository.impl;

import org.springframework.stereotype.Component;
import ru.rerumu.lists.mappers.BookTypeMapper;
import ru.rerumu.lists.mappers.CrudMapper;
import ru.rerumu.lists.model.BookType;
import ru.rerumu.lists.repository.BookTypeRepository;

@Component
public class BookTypeRepositoryImpl extends CrudRepositoryImpl<BookType,Integer> implements BookTypeRepository {

    public BookTypeRepositoryImpl(BookTypeMapper bookTypeMapper) {
        super(bookTypeMapper);
    }
}
