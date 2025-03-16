package ru.rerumu.lists.dao.tag;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BookTagDTO {
    private final Long bookTagId;
    private final Long bookBookId;
    private final Long bookUserId;
    private final Long tagTagId;
    private final Long tagUserId;
}
