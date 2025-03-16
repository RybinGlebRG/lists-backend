package ru.rerumu.lists.model.book.type;

import lombok.NonNull;
import ru.rerumu.lists.model.base.EntityDTO;

public class BookTypeDTO implements EntityDTO<BookType> {
    public Integer id;
    public String name;

    public BookTypeDTO(
            @NonNull Integer id,
            @NonNull String name
    ) {
        this.id = id;
        this.name = name;
    }

    @Override
    public BookType toDomain() {
        if (id == null || name == null){
            return null;
        }
        BookType bookType = new BookType(id,name);
        return bookType;
    }
}
