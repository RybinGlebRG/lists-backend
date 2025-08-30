package ru.rerumu.lists.domain.booktype;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.rerumu.lists.dao.booktype.mapper.BookTypeMapper;

import java.util.List;

@Component
public class BookTypeFactory {

    private final BookTypeMapper bookTypeMapper;

    @Autowired
    public BookTypeFactory(BookTypeMapper bookTypeMapper) {
        this.bookTypeMapper = bookTypeMapper;
    }

    public List<BookType> getAll() {
        return bookTypeMapper.findAll();
    }
}
