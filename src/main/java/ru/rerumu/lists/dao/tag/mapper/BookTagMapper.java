package ru.rerumu.lists.dao.tag.mapper;

import lombok.NonNull;
import org.apache.ibatis.annotations.Mapper;
import ru.rerumu.lists.dao.tag.BookTagDTO;

@Mapper
public interface BookTagMapper{

    void delete(@NonNull Long bookId, @NonNull Long tagId);
    void add(@NonNull BookTagDTO bookTagDTO);

    @NonNull
    Long nextval();
}
