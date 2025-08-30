package ru.rerumu.lists.dao.booktype;

import ru.rerumu.lists.domain.booktype.BookType;

import java.util.List;

public interface BookTypeRepository {

    BookType findById(Long bookTypeId);

    List<BookType> findAll();

}
