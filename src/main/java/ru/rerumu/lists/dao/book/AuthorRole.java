package ru.rerumu.lists.dao.book;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AuthorRole {
    TEXT_AUTHOR(1L);

    private final Long id;
}
