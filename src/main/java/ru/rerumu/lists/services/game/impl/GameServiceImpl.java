package ru.rerumu.lists.services.game.impl;

import org.springframework.transaction.annotation.Transactional;
import ru.rerumu.lists.domain.game.Game;
import ru.rerumu.lists.domain.game.impl.GameImpl;
import ru.rerumu.lists.domain.user.User;
import ru.rerumu.lists.services.book.Search;
import ru.rerumu.lists.dao.base.CrudRepository;
import ru.rerumu.lists.services.game.GameService;
import ru.rerumu.lists.views.GameAddView;

import java.util.List;
import java.util.Optional;

public class GameServiceImpl implements GameService {

    private final CrudRepository<Game,Integer> crudRepository;

    public GameServiceImpl(CrudRepository<Game,Integer> crudRepository){
        this.crudRepository = crudRepository;
    }

    @Transactional(rollbackFor = Exception.class)
    public void addGame(User user, GameAddView gameAddView){
        Game newGame = new GameImpl.Builder()
                .title(gameAddView.title())
                .user(user)
                .createDateUTC(gameAddView.createDateUTC())
                .gameId(crudRepository.getNextId())
                .note(gameAddView.note())
                .build();
        crudRepository.create(newGame);
    }

    // TODO: fix null
    @Override
    public void deleteGame(Integer gameId) {
        crudRepository.delete(gameId, null);
    }

    // TODO: fix null
    @Override
    public Optional<Game> findById(Integer gameId) {
        return crudRepository.findById(gameId, null);
    }

    public List<Game> getAll(User user, Search search){
        return crudRepository.findByUser(user);
    }
}
