package ru.rerumu.lists.domain.tag;

import org.json.JSONObject;
import ru.rerumu.lists.dao.tag.TagDTO;
import ru.rerumu.lists.domain.user.User;
import ru.rerumu.lists.domain.book.Book;

public interface Tag {

    Long getId();
    String getName();
    User getUser();

    void delete();
    void removeFromBook(Long bookId);
    void addToBook(Book book);
    TagDTO toDTO();
    JSONObject toJSONObject();
}
