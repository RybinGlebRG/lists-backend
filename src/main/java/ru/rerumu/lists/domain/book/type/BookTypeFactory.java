package ru.rerumu.lists.domain.book.type;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.rerumu.lists.dao.book.type.BookTypeMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class BookTypeFactory {

    private final BookTypeMapper bookTypeMapper;

    @Autowired
    public BookTypeFactory(BookTypeMapper bookTypeMapper) {
        this.bookTypeMapper = bookTypeMapper;
    }

    public List<BookType> getAll() {
        return bookTypeMapper.findAll().stream()
                .map(BookTypeDTO::toDomain)
                .collect(Collectors.toCollection(ArrayList::new));
    }
}
