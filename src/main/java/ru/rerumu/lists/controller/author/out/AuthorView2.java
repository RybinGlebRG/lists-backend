package ru.rerumu.lists.controller.author.out;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import ru.rerumu.lists.model.author.AuthorDTO;

@EqualsAndHashCode
@ToString
@Getter
public class AuthorView2 {

    private final Long authorId;
    private final String name;

    public AuthorView2(AuthorDTO authorDTO) {
        this.authorId = authorDTO.getAuthorId();
        this.name = authorDTO.getName();
    }
}
