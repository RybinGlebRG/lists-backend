package ru.rerumu.lists.domain.tag.impl;

import lombok.Getter;
import ru.rerumu.lists.domain.tag.Tag;
import ru.rerumu.lists.domain.user.User;

import java.util.Objects;

public class TagImpl implements Tag {

    private final Long tagId;

    @Getter
    private final String name;

    @Getter
    private final User user;

    public TagImpl(Long tagId, String name, User user) {
        this.tagId = tagId;
        this.name = name;
        this.user = user;
    }

    @Override
    public Long getId() {
        return tagId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TagImpl tag = (TagImpl) o;
        return Objects.equals(tagId, tag.tagId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(tagId);
    }
}
