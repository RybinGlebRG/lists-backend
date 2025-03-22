package ru.rerumu.lists.dao.tag;

import lombok.NonNull;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BookTagMapper{

    void delete(@NonNull Long bookId, @NonNull Long tagId);
    void add(@NonNull BookTagDTO bookTagDTO);

    @NonNull
    Long nextval();
}
