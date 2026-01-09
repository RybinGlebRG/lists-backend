package ru.rerumu.lists.domain.author.impl;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.rerumu.lists.domain.author.Author;
import ru.rerumu.lists.domain.user.User;

import java.util.Objects;

@ToString(doNotUseGetters = true)
public class AuthorImpl implements Author {

    @Getter
    private final Long authorId;

    @Setter
    @Getter
    private String name;

    @Getter
    private final User user;

    public AuthorImpl(Long authorId, String name, User user){
        this.authorId = authorId;
        this.name = name;
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuthorImpl author = (AuthorImpl) o;
        return authorId.equals(author.authorId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(authorId);
    }

    @Override
    public Long getId() {
        return authorId;
    }
}
