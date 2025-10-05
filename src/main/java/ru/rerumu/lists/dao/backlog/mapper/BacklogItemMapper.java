package ru.rerumu.lists.dao.backlog.mapper;

import org.apache.ibatis.annotations.Mapper;
import ru.rerumu.lists.dao.backlog.BacklogItemDTO;
import ru.rerumu.lists.dao.base.CrudMapper;

@Mapper
public interface BacklogItemMapper extends CrudMapper<BacklogItemDTO, Long, BacklogItemDTO> {
}
