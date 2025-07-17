package ru.rerumu.lists.domain.tag.impl;

import org.json.JSONObject;
import ru.rerumu.lists.domain.user.User;
import ru.rerumu.lists.domain.book.Book;
import ru.rerumu.lists.domain.tag.Tag;
import ru.rerumu.lists.dao.tag.TagsRepository;
import ru.rerumu.lists.domain.tag.TagDTO;

import java.util.Objects;

public class TagImpl implements Tag {

    private final Long tagId;
    private final String name;
    private final TagsRepository tagsRepository;
    private final User user;

    TagImpl(Long tagId, String name, User user, TagsRepository tagsRepository) {
        this.tagId = tagId;
        this.name = name;
        this.tagsRepository = tagsRepository;
        this.user = user;
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
    public User getUser() {
        return user;
    }

    @Override
    public void delete() {
        tagsRepository.delete(tagId, user);
    }

    @Override
    public void removeFromBook(Long bookId) {
        tagsRepository.remove(bookId, tagId);
    }

    @Override
    public void addToBook(Book book) {
        tagsRepository.add(this, book);
    }

    @Override
    public TagDTO toDTO() {
        return new TagDTO(
                tagId,
                name,
                user.userId()
        );
    }

    @Override
    public JSONObject toJSONObject() {
        JSONObject obj = new JSONObject();
        obj.put("tagId", tagId);
        obj.put("name", name);
        return obj;
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
