package ru.rerumu.lists.dao.tag;

import lombok.Getter;
import lombok.NonNull;
import ru.rerumu.lists.dao.base.MyBatisEntity;

@Getter
public class TagDTO implements MyBatisEntity {

    private final Long tagId;
    private final String name;
    private final Long userId;

    public TagDTO(@NonNull Long tagId, @NonNull String name, @NonNull Long userId) {
        this.tagId = tagId;
        this.name = name;
        this.userId = userId;
    }
}
