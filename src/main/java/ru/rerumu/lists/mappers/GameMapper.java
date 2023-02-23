package ru.rerumu.lists.mappers;

import ru.rerumu.lists.model.Game;
import ru.rerumu.lists.model.User;

import java.util.ArrayList;
import java.util.List;

public interface GameMapper extends CrudMapper<Game,Integer,Game> {

    default Game findById(Integer id){
        return null;
    }


}
