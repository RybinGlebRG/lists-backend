package ru.rerumu.lists.controller.author.views.out;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import ru.rerumu.lists.domain.author.Author;

@EqualsAndHashCode
@ToString
@Getter
public class AuthorView {

    private final Long authorId;
    private final String name;

    public AuthorView(Author author) {
        this.authorId = author.getId();
        this.name = author.getName();
    }
}
