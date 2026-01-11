package ru.rerumu.lists.dao.game.mapper;

import org.apache.ibatis.annotations.Mapper;
import ru.rerumu.lists.dao.base.CrudMapper;
import ru.rerumu.lists.domain.game.Game;

@Mapper
public interface GameMapper extends CrudMapper<Game,Integer,Game> {
}
