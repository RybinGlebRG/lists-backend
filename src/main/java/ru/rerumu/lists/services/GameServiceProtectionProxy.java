package ru.rerumu.lists.services;

import ru.rerumu.lists.exception.UserPermissionException;
import ru.rerumu.lists.model.Game;
import ru.rerumu.lists.model.User;
import ru.rerumu.lists.model.books.Search;
import ru.rerumu.lists.views.GameAddView;

import java.util.List;

public class GameServiceProtectionProxy implements GameService{
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
        // TODO: Implement
        throw new RuntimeException("NotImplemented");
    }
}