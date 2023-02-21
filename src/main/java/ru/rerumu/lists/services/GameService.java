package ru.rerumu.lists.services;

import ru.rerumu.lists.model.BookType;
import ru.rerumu.lists.model.Game;
import ru.rerumu.lists.model.User;
import ru.rerumu.lists.repository.CrudRepository;

import java.util.List;

public class GameService {

    private final CrudRepository<Game,Integer> crudRepository;

    public GameService(CrudRepository<Game,Integer> crudRepository){
        this.crudRepository = crudRepository;
    }

    public void addGame(){

    }

    public List<Game> getAll(User user){
        return crudRepository.findAll(user);
    }
}
