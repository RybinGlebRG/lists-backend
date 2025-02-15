package ru.rerumu.lists.dao.tag;

import lombok.NonNull;
import org.apache.ibatis.annotations.Mapper;
import ru.rerumu.lists.mappers.CrudMapper;
import ru.rerumu.lists.model.tag.TagDTO;

import java.util.List;

@Mapper
public interface TagsMapper extends CrudMapper<TagDTO, Long, TagDTO> {

    List<TagDTO> findByBookIds(@NonNull List<Long> bookIds);
}
