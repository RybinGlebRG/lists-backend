package ru.rerumu.lists.dao.tag;

import ru.rerumu.lists.model.user.User;
import ru.rerumu.lists.model.book.Book;
import ru.rerumu.lists.model.tag.Tag;
import ru.rerumu.lists.model.tag.TagDTO;
import ru.rerumu.lists.dao.base.CrudRepository;

import java.util.List;

public interface TagsRepository extends CrudRepository<TagDTO,Long> {

    void remove(Long bookId, Long tagId);

    void create(Tag tag);
    void add(Tag tag, Book book);
    List<TagDTO> findByIds(List<Long> tagIds, User user);
}
