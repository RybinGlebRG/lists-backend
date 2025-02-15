package ru.rerumu.lists.model.tag;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.rerumu.lists.exception.NotImplementedException;
import ru.rerumu.lists.model.dto.EntityDTO;

@Getter
@AllArgsConstructor
public class TagDTO implements EntityDTO<Tag> {

    private final Long tagId;
    private final String name;
    private final Long userId;

    @Override
    public Tag toDomain() {
        throw  new NotImplementedException();
    }
}
