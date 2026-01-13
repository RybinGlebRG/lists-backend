package ru.rerumu.lists.dao.game.mapper;

import org.apache.ibatis.annotations.Mapper;
import ru.rerumu.lists.dao.base.CrudMapper;
import ru.rerumu.lists.dao.game.impl.GameDTO;

@Mapper
public interface GameMapper extends CrudMapper<GameDTO,Long,GameDTO> {
}
