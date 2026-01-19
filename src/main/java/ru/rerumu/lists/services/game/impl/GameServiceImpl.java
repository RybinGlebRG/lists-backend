package ru.rerumu.lists.services.game.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.rerumu.lists.controller.games.views.GameAddView;
import ru.rerumu.lists.dao.game.GamesRepository;
import ru.rerumu.lists.dao.user.UsersRepository;
import ru.rerumu.lists.domain.game.Game;
import ru.rerumu.lists.domain.user.User;
import ru.rerumu.lists.services.game.GameService;

import java.util.List;

@Service("GameService")
public class GameServiceImpl implements GameService {

    private final GamesRepository gamesRepository;
    private final UsersRepository usersRepository;

    @Autowired
    public GameServiceImpl(
            GamesRepository gamesRepository,
            UsersRepository usersRepository
    ){
        this.gamesRepository = gamesRepository;
        this.usersRepository = usersRepository;
    }

    @Transactional(rollbackFor = Exception.class)
    public void addGame(Long userId, GameAddView gameAddView){
        User user = usersRepository.findById(userId);
        gamesRepository.create(
                gameAddView.title(),
                user,
                gameAddView.createDateUTC(),
                gameAddView.note()
        );
    }

    // TODO: fix null
    @Override
    public void deleteGame(Long gameId, Long userId) {
        User user = usersRepository.findById(userId);
        Game game = gamesRepository.findById(gameId, user);
        gamesRepository.delete(game);
    }

    // TODO: fix null
    @Override
    public Game findById(Long gameId, Long userId) {
        User user = usersRepository.findById(userId);
        return gamesRepository.findById(gameId, user);
    }

    @Override
    public List<Game> getAll(Long userId){
        User user = usersRepository.findById(userId);
        return gamesRepository.findByUser(user);
    }
}
