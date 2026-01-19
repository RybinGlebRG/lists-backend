package ru.rerumu.lists.services.game;

import ru.rerumu.lists.controller.games.views.GameAddView;
import ru.rerumu.lists.domain.game.Game;

import java.util.List;

public interface GameService {

    List<Game> getAll(Long userId);

    void addGame(Long userId, GameAddView gameAddView);

    void deleteGame(Long gameId, Long userId);

    Game findById(Long gameId, Long userId);
}
