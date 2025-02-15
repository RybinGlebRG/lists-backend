package ru.rerumu.lists.mappers;

import org.apache.ibatis.annotations.Mapper;
import ru.rerumu.lists.model.Game;
import ru.rerumu.lists.model.User;

import java.util.ArrayList;
import java.util.List;

@Mapper
public interface GameMapper extends CrudMapper<Game,Integer,Game> {
}
