package ru.rerumu.lists.dao.game.impl;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.rerumu.lists.crosscut.exception.EntityNotFoundException;
import ru.rerumu.lists.dao.game.GamesRepository;
import ru.rerumu.lists.dao.game.mapper.GameMapper;
import ru.rerumu.lists.dao.user.UsersRepository;
import ru.rerumu.lists.domain.game.Game;
import ru.rerumu.lists.domain.game.impl.GameImpl;
import ru.rerumu.lists.domain.user.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class GamesRepositoryImpl implements GamesRepository {

    private final GameMapper gameMapper;
    private final UsersRepository usersRepository;

    @Autowired
    public GamesRepositoryImpl(GameMapper gameMapper, UsersRepository usersRepository) {
        this.gameMapper = gameMapper;
        this.usersRepository = usersRepository;
    }

    @Override
    public Game create(@NonNull String title, @NonNull User user, @NonNull LocalDateTime createDateUTC, String note) {
        Long nextId = gameMapper.getNextId();

        Game newGame = new GameImpl(
                nextId,
                title,
                user,
                createDateUTC,
                note
        );

        gameMapper.create(new GameDTO(
                newGame.getId(),
                newGame.getUser().getId(),
                newGame.getTitle(),
                newGame.getCreateDateUTC(),
                newGame.getNote()
        ));

        return newGame;
    }

    @Override
    public @NonNull List<Game> findByUser(@NonNull User user) {
        return gameMapper.findByUser(user).stream()
                .map(dto -> new GameImpl(
                            dto.getGameId(),
                            dto.getTitle(),
                            usersRepository.findById(dto.getUserId()),
                            dto.getInsertDate(),
                            dto.getNote()
                ))
                .collect(Collectors.toCollection(ArrayList::new));

    }

    @Override
    public @NonNull Game findById(@NonNull Long gameId, @NonNull User user) {
        GameDTO gameDTO = gameMapper.findById(gameId, user.getId());
        if (gameDTO == null) {
            throw new EntityNotFoundException();
        }

        return new GameImpl(
                gameDTO.getGameId(),
                gameDTO.getTitle(),
                usersRepository.findById(gameDTO.getUserId()),
                gameDTO.getInsertDate(),
                gameDTO.getNote()
        );
    }

    @Override
    public void delete(@NonNull Game game) {
        gameMapper.delete(game.getId(), game.getUser().getId());
    }
}
