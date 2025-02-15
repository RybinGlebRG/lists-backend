package ru.rerumu.lists.dao.tag.impl;

import ru.rerumu.lists.dao.tag.BookTagMapper;
import ru.rerumu.lists.dao.tag.TagsMapper;
import ru.rerumu.lists.model.tag.TagDTO;
import ru.rerumu.lists.dao.tag.TagsRepository;
import ru.rerumu.lists.repository.impl.CrudRepositoryDtoImpl;

public class TagsRepositoryImpl extends CrudRepositoryDtoImpl<TagDTO,Long> implements TagsRepository {

    private final TagsMapper tagsMapper;
    private final BookTagMapper bookTagMapper;

    public TagsRepositoryImpl(TagsMapper tagsMapper, BookTagMapper bookTagMapper) {
        super(tagsMapper);
        this.tagsMapper = tagsMapper;
        this.bookTagMapper = bookTagMapper;
    }

    @Override
    public void remove(Long bookId, Long tagId) {
            // Removing tag from book
            bookTagMapper.delete(bookId, tagId);
    }

    @Override
    public void add(Long bookId, Long tagId) {

    }
}
