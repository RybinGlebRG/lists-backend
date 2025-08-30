package ru.rerumu.lists.domain.author.impl;

import lombok.Getter;
import lombok.Setter;
import org.json.JSONObject;
import ru.rerumu.lists.domain.author.Author;
import ru.rerumu.lists.domain.author.AuthorDTO;
import ru.rerumu.lists.domain.user.User;

import java.util.Objects;

public class AuthorImpl implements Cloneable, Author {

    @Getter
    private final Long authorId;

    @Getter
    private final Long readListId;

    @Setter
    @Getter
    private String name;

    private final User user;

    public AuthorImpl(Long authorId, Long readListId, String name, User user){
        this.authorId = authorId;
        this.readListId = readListId;
        this.name = name;
        this.user = user;
    }

    public JSONObject toJSONObject(){
        JSONObject obj = new JSONObject();

        obj.put("authorId", authorId);
        obj.put("readListId", readListId);
        obj.put("name", name);

        return obj;
    }

    @Override
    public String toString() {
        return this.toJSONObject().toString();
    }

    @Override
    public AuthorImpl clone() {
        try {
            AuthorImpl clone = (AuthorImpl) super.clone();
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
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

    @Override
    public AuthorDTO toDTO() {
        return new AuthorDTO(
                authorId,
                readListId,
                name,
                user.toDTO()
        );
    }
}
