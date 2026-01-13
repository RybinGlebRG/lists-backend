package ru.rerumu.lists.dao.tag;

import lombok.NonNull;
import ru.rerumu.lists.domain.book.Book;
import ru.rerumu.lists.domain.user.User;
import ru.rerumu.lists.domain.tag.Tag;
import ru.rerumu.lists.dao.base.CrudRepository;

import java.util.List;

public interface TagsRepository extends CrudRepository<TagDTO,Long> {

    void remove(Long bookId, Long tagId);

    List<Tag> findAll(@NonNull User user);

    @NonNull
    List<Tag> findByIds(@NonNull List<Long> tagIds, @NonNull User user);

    @NonNull
    Tag create(@NonNull String name, @NonNull User user);

    @NonNull
    Tag attach(@NonNull TagDTO tagDTO);

    void addTagTo(Tag tag, Book book);

}
