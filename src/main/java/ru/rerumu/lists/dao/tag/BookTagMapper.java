package ru.rerumu.lists.dao.tag;

import lombok.NonNull;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BookTagMapper{

    void delete(@NonNull Long bookId, @NonNull Long tagId);
    void add(@NonNull BookTagDTO bookTagDTO);

    @NonNull
    Long nextval();
}
