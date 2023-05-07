package ru.rerumu.lists.model.dto;

import ru.rerumu.lists.model.BookType;

public class BookTypeDTO implements EntityDTO<BookType>{
    public Integer id;
    public String name;
    @Override
    public BookType toDomain() {
        if (id == null || name == null){
            return null;
        }
        BookType bookType = new BookType(id,name);
        return bookType;
    }
}
