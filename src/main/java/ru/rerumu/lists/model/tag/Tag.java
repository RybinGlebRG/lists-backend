package ru.rerumu.lists.model.tag;

import org.json.JSONObject;
import ru.rerumu.lists.model.user.User;
import ru.rerumu.lists.model.book.Book;

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
