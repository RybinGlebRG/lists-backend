package ru.rerumu.lists.model.tag.impl;

import ru.rerumu.lists.model.tag.Tag;
import ru.rerumu.lists.dao.tag.TagsRepository;

import java.util.Objects;

public class TagImpl implements Tag {

    private final Long tagId;
    private final String name;
    private final TagsRepository tagsRepository;

    TagImpl(Long tagId, String name, TagsRepository tagsRepository) {
        this.tagId = tagId;
        this.name = name;
        this.tagsRepository = tagsRepository;
    }

    @Override
    public Long getId() {
        return tagId;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void delete() {
        tagsRepository.delete(tagId);
    }

    @Override
    public void removeFromBook(Long bookId) {
        tagsRepository.remove(bookId, tagId);
    }

    @Override
    public void addToBook(Long bookId) {
        tagsRepository.
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
