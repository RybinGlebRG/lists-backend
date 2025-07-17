package ru.rerumu.lists.dao.tag.mapper;

import lombok.NonNull;
import org.apache.ibatis.annotations.Mapper;
import ru.rerumu.lists.dao.base.CrudMapper;
import ru.rerumu.lists.domain.tag.TagDTO;

import java.util.List;

@Mapper
public interface TagsMapper extends CrudMapper<TagDTO, Long, TagDTO> {

    List<TagDTO> findByBookIds(@NonNull List<Long> bookIds);
    List<TagDTO> findByIds(@NonNull List<Long> tagIds, @NonNull Long userId);
}
