package ru.rerumu.lists.controller.author.views.out;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
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

    @JsonCreator
    public AuthorView(
            @JsonProperty("authorId") Long authorId,
            @JsonProperty("name") String name
    ) {
        this.authorId = authorId;
        this.name = name;
    }
}
