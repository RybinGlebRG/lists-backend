package ru.rerumu.lists.domain.author;

public record AuthorBookRelation(
        Long bookId,
        Long authorId,
        Long userId,
        Long roleId
){}
