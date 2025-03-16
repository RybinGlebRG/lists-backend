package ru.rerumu.lists.dao.tag.impl;

import org.springframework.stereotype.Component;
import ru.rerumu.lists.dao.tag.BookTagDTO;
import ru.rerumu.lists.dao.tag.BookTagMapper;
import ru.rerumu.lists.dao.tag.TagsMapper;
import ru.rerumu.lists.model.user.User;
import ru.rerumu.lists.model.book.Book;
import ru.rerumu.lists.model.tag.Tag;
import ru.rerumu.lists.model.tag.TagDTO;
import ru.rerumu.lists.dao.tag.TagsRepository;
import ru.rerumu.lists.dao.base.impl.CrudRepositoryDtoImpl;

import java.util.List;

@Component
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
    public void create(Tag tag) {
        tagsMapper.create(tag.toDTO());
    }

    @Override
    public void add(Tag tag, Book book) {
        Long nextId = bookTagMapper.nextval();

        BookTagDTO bookTagDTO = new BookTagDTO(
                nextId,
                book.getId(),
                book.getUser().userId(),
                tag.getId(),
                tag.getUser().userId()
        );
        bookTagMapper.add(bookTagDTO);
    }

    @Override
    public List<TagDTO> findByIds(List<Long> tagIds, User user) {
        return tagsMapper.findByIds(tagIds, user.userId());
    }
}
