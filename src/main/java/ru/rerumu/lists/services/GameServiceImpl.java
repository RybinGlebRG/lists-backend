package ru.rerumu.lists.services;

import org.springframework.transaction.annotation.Transactional;
import ru.rerumu.lists.model.Game;
import ru.rerumu.lists.model.User;
import ru.rerumu.lists.model.books.Search;
import ru.rerumu.lists.repository.CrudRepository;
import ru.rerumu.lists.views.GameAddView;

import java.util.List;
import java.util.Optional;

public class GameServiceImpl implements GameService{

    private final CrudRepository<Game,Integer> crudRepository;

    public GameServiceImpl(CrudRepository<Game,Integer> crudRepository){
        this.crudRepository = crudRepository;
    }

    @Transactional(rollbackFor = Exception.class)
    public void addGame(User user, GameAddView gameAddView){
        Game newGame = new Game.Builder()
                .title(gameAddView.title())
                .user(user)
                .createDateUTC(gameAddView.createDateUTC())
                .gameId(crudRepository.getNextId())
                .note(gameAddView.note())
                .build();
        crudRepository.create(newGame);
    }

    @Override
    public void deleteGame(Integer gameId) {
        crudRepository.delete(gameId);
    }

    @Override
    public Optional<Game> findById(Integer gameId) {
        return crudRepository.findById(gameId);
    }

    public List<Game> getAll(User user, Search search){
        return crudRepository.findByUser(user);
    }
}
