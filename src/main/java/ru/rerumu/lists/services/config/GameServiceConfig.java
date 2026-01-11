package ru.rerumu.lists.services.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.context.annotation.RequestScope;
import org.springframework.web.context.request.RequestContextHolder;
import ru.rerumu.lists.crosscut.exception.EntityNotFoundException;
import ru.rerumu.lists.dao.base.impl.CrudRepositoryEntityImpl;
import ru.rerumu.lists.dao.game.mapper.GameMapper;
import ru.rerumu.lists.domain.game.Game;
import ru.rerumu.lists.domain.user.User;
import ru.rerumu.lists.services.AuthUserParser;
import ru.rerumu.lists.services.game.GameService;
import ru.rerumu.lists.services.game.impl.GameServiceImpl;
import ru.rerumu.lists.services.game.impl.GameServiceProtectionProxy;
import ru.rerumu.lists.services.user.UserService;

@Configuration
public class GameServiceConfig {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Bean("GameService")
    public GameService getGameService(
            GameMapper gameMapper
    ) {
        CrudRepositoryEntityImpl<Game, Integer> gameRepository = new CrudRepositoryEntityImpl<>(gameMapper);
        return new GameServiceImpl(gameRepository);
    }

    @Bean("GameServiceProtectionProxy")
    @Primary
    @RequestScope
    public GameService getGameServiceProtectionProxy(
            @Qualifier("GameService") GameService gameService,
            UserService userService
    ) throws EntityNotFoundException {
        Long authUserId = AuthUserParser.getAuthUser(RequestContextHolder.currentRequestAttributes());
        User authUser = userService.findById(authUserId);
        logger.info(String.format("GOT USER %d", authUser.getId()));
        var gameServiceProtectionProxy = new GameServiceProtectionProxy(
                authUser,
                gameService
        );
        return gameServiceProtectionProxy;
    }
}
