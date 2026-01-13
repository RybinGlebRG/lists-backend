package ru.rerumu.lists.dao.booktype;

import lombok.NonNull;

public class BookTypeDTO {
    public Long id;
    public String name;

    public BookTypeDTO(
            @NonNull Long id,
            @NonNull String name
    ) {
        this.id = id;
        this.name = name;
    }
}
