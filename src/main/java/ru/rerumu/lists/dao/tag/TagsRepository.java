package ru.rerumu.lists.dao.tag;

import ru.rerumu.lists.model.tag.TagDTO;
import ru.rerumu.lists.repository.CrudRepository;

public interface TagsRepository extends CrudRepository<TagDTO,Long> {

    void remove(Long bookId, Long tagId);
    void add(Long bookId, Long tagId);
}
