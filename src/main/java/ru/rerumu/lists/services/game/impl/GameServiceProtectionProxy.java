package ru.rerumu.lists.services.game.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.rerumu.lists.crosscut.exception.UserPermissionException;
import ru.rerumu.lists.domain.game.Game;
import ru.rerumu.lists.domain.user.User;
import ru.rerumu.lists.services.book.Search;
import ru.rerumu.lists.services.game.GameService;
import ru.rerumu.lists.controller.games.views.GameAddView;

import java.util.List;
import java.util.Optional;

public class GameServiceProtectionProxy implements GameService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final User authUser;
    private final GameService gameService;

    public GameServiceProtectionProxy(User authUser, GameService gameService) {
        this.authUser = authUser;
        this.gameService = gameService;
    }

    @Override
    public List<Game> getAll(User user, Search search) {
        if (!user.equals(authUser)){
            throw new UserPermissionException();
        }
        return gameService.getAll(user, search);
    }

    @Override
    public void addGame(User user, GameAddView gameAddView) {
        if (!user.equals(authUser)){
            throw new UserPermissionException();
        }
        gameService.addGame(user, gameAddView);
    }

    @Override
    public void deleteGame(Integer gameId) {
        Optional<Game> optionalGame = gameService.findById(gameId);
        logger.debug(String.format("Got optional game: %s",optionalGame));
        logger.debug(String.format("Comparing owner with authUser='%s'",authUser));
        if (!optionalGame.orElseThrow().getUser().equals(authUser)){
            throw new UserPermissionException();
        } else {
            gameService.deleteGame(gameId);
        }

    }

    @Override
    public Optional<Game> findById(Integer gameId) {
        Optional<Game> optionalGame = gameService.findById(gameId);
        if (!optionalGame.orElseThrow().getUser().equals(authUser)){
            throw new UserPermissionException();
        } else {
            return optionalGame;
        }
    }
}
