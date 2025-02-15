package ru.rerumu.lists.model.tag;

public interface Tag {

    Long getId();
    String getName();

    void delete();
    void removeFromBook(Long bookId);
    void addToBook(Long bookId);
}
