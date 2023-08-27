package ru.rerumu.lists.config.beans.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.context.annotation.RequestScope;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import ru.rerumu.lists.exception.EntityNotFoundException;
import ru.rerumu.lists.mappers.GameMapper;
import ru.rerumu.lists.model.Game;
import ru.rerumu.lists.model.User;
import ru.rerumu.lists.repository.impl.CrudRepositoryEntityImpl;
import ru.rerumu.lists.services.GameService;
import ru.rerumu.lists.services.GameServiceImpl;
import ru.rerumu.lists.services.GameServiceProtectionProxy;
import ru.rerumu.lists.services.UserService;

import java.util.Optional;

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
        Long authUserId = (Long) RequestContextHolder.currentRequestAttributes().getAttribute("authUserId", RequestAttributes.SCOPE_REQUEST);
        Optional<User> authUser = userService.getOne(authUserId);
        logger.info(String.format("GOT USER %d", authUser.orElseThrow().userId()));
        var gameServiceProtectionProxy = new GameServiceProtectionProxy(
                authUser.orElseThrow(),
                gameService
        );
        return gameServiceProtectionProxy;
    }
}
