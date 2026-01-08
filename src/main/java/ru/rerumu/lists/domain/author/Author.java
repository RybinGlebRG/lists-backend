package ru.rerumu.lists.domain.author;

public interface Author {

    Long getId();

    String getName();

    AuthorDTO toDTO();
}
