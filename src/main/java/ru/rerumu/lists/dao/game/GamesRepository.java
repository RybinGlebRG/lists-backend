package ru.rerumu.lists.dao.game;

import lombok.NonNull;
import ru.rerumu.lists.domain.game.Game;
import ru.rerumu.lists.domain.user.User;

import java.time.LocalDateTime;
import java.util.List;

public interface GamesRepository {

    Game create(
            @NonNull String title,
            @NonNull User user,
            @NonNull LocalDateTime createDateUTC,
            String note
    );

    @NonNull
    List<Game> findByUser(@NonNull User user);

    @NonNull
    Game findById(@NonNull Long gameId, @NonNull User user);

    void delete(@NonNull Game game);

}
