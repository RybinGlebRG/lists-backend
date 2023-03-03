package ru.rerumu.lists.services;

import org.springframework.transaction.annotation.Transactional;
import ru.rerumu.lists.model.Game;
import ru.rerumu.lists.model.User;
import ru.rerumu.lists.repository.CrudRepository;
import ru.rerumu.lists.views.GameAddView;

import java.util.List;

public class GameService {

    private final CrudRepository<Game,Integer> crudRepository;

    public GameService(CrudRepository<Game,Integer> crudRepository){
        this.crudRepository = crudRepository;
    }

    @Transactional(rollbackFor = Exception.class)
    public void addGame(User user, GameAddView gameAddView){
        Game newGame = new Game.Builder()
                .title(gameAddView.title())
                .user(user)
                .createDateUTC(gameAddView.createDateUTC())
                .build();
        crudRepository.save(newGame);
    }

    public List<Game> getAll(User user){
        return crudRepository.findAll(user);
    }
}
