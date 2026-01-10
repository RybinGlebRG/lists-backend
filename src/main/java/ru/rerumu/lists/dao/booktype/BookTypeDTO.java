package ru.rerumu.lists.dao.booktype;

import lombok.NonNull;
import ru.rerumu.lists.domain.base.EntityDTO;
import ru.rerumu.lists.domain.booktype.impl.BookTypeImpl;

public class BookTypeDTO implements EntityDTO<BookTypeImpl> {
    public Long id;
    public String name;

    public BookTypeDTO(
            @NonNull Long id,
            @NonNull String name
    ) {
        this.id = id;
        this.name = name;
    }

    @Override
    public BookTypeImpl toDomain() {
        if (id == null || name == null){
            return null;
        }
        BookTypeImpl bookType = new BookTypeImpl(id,name);
        return bookType;
    }
}
