package ru.rerumu.lists.domain.book;

public record AuthorBookRelation(
        Long bookId,
        Long authorId,
        Long userId,
        Long roleId
){}
