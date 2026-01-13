package ru.rerumu.lists.services.game.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;
import org.springframework.web.context.request.RequestContextHolder;
import ru.rerumu.lists.controller.games.views.GameAddView;
import ru.rerumu.lists.crosscut.exception.UserPermissionException;
import ru.rerumu.lists.dao.user.UsersRepository;
import ru.rerumu.lists.domain.game.Game;
import ru.rerumu.lists.domain.user.User;
import ru.rerumu.lists.services.AuthUserParser;
import ru.rerumu.lists.services.book.Search;
import ru.rerumu.lists.services.game.GameService;
import ru.rerumu.lists.services.user.UserService;

import java.util.List;

@Service("GameServiceProtectionProxy")
@Primary
@RequestScope
@Slf4j
public class GameServiceProtectionProxy implements GameService {
    private final User authUser;
    private final GameService gameService;
    private final UsersRepository usersRepository;

    @Autowired
    public GameServiceProtectionProxy(
            @Qualifier("GameService") GameService gameService,
            UserService userService,
            UsersRepository usersRepository
    ) {
        this.gameService = gameService;
        this.usersRepository = usersRepository;
        Long authUserId = AuthUserParser.getAuthUser(RequestContextHolder.currentRequestAttributes());
        authUser = userService.findById(authUserId);
    }

    @Override
    public List<Game> getAll(Long userId, Search search) {
        User user = usersRepository.findById(userId);
        if (!user.equals(authUser)){
            throw new UserPermissionException("UserPermissionException.user_is_not_owner");
        }
        return gameService.getAll(userId, search);
    }

    @Override
    public void addGame(Long userId, GameAddView gameAddView) {
        User user = usersRepository.findById(userId);
        if (!user.equals(authUser)){
            throw new UserPermissionException("UserPermissionException.user_is_not_owner");
        }
        gameService.addGame(userId, gameAddView);
    }

    // TODO: Check game's user???
    @Override
    public void deleteGame(Long gameId, Long userId) {
        User user = usersRepository.findById(userId);
        if (!user.equals(authUser)){
            throw new UserPermissionException("UserPermissionException.user_is_not_owner");
        }
        gameService.deleteGame(gameId, userId);


//        Optional<Game> optionalGame = gameService.findById(gameId);
//        log.debug(String.format("Got optional game: %s",optionalGame));
//        log.debug(String.format("Comparing owner with authUser='%s'",authUser));
//        if (!optionalGame.orElseThrow().getUser().equals(authUser)){
//            throw new UserPermissionException("UserPermissionException.user_is_not_owner");
//        } else {
//            gameService.deleteGame(gameId);
//        }

    }

    @Override
    public Game findById(Long gameId, Long userId) {
        User user = usersRepository.findById(userId);
        if (!user.equals(authUser)){
            throw new UserPermissionException("UserPermissionException.user_is_not_owner");
        }
        return gameService.findById(gameId, userId);
    }
}
