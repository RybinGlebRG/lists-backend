package ru.rerumu.lists.dao.tag;

import lombok.Getter;
import lombok.NonNull;
import ru.rerumu.lists.crosscut.exception.NotImplementedException;
import ru.rerumu.lists.dao.base.MyBatisEntity;
import ru.rerumu.lists.domain.base.EntityDTO;
import ru.rerumu.lists.domain.tag.Tag;

@Getter
public class TagDTO implements EntityDTO<Tag>, MyBatisEntity {

    private final Long tagId;
    private final String name;
    private final Long userId;

    public TagDTO(@NonNull Long tagId, @NonNull String name, @NonNull Long userId) {
        this.tagId = tagId;
        this.name = name;
        this.userId = userId;
    }

    @Override
    public Tag toDomain() {
        throw  new NotImplementedException();
    }
}
