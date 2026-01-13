package ru.rerumu.lists.domain.game.impl;

import lombok.Getter;
import lombok.NonNull;
import org.json.JSONObject;
import ru.rerumu.lists.domain.game.Game;
import ru.rerumu.lists.domain.seriesitem.SeriesItemType;
import ru.rerumu.lists.domain.user.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class GameImpl implements Game {

    private final static SeriesItemType SERIES_ITEM_TYPE = SeriesItemType.GAME;

    private final Long gameId;

    @Getter
    private String title;

    @Getter
    private final User user;

    @Getter
    private LocalDateTime createDateUTC;

    @Getter
    private String note;

    public GameImpl(Long gameId, @NonNull String title, @NonNull User user, @NonNull LocalDateTime createDateUTC, String note) {
        this.gameId = gameId;
        this.title = title;
        this.user = user;
        this.createDateUTC = createDateUTC;
        this.note = note;
    }

    public JSONObject toJSONObject(){
        JSONObject obj = new JSONObject();
        obj.put("gameId", gameId);
        obj.put("title", title);
        obj.put("createDateUTC", createDateUTC.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        obj.put("itemType",SERIES_ITEM_TYPE.name());
        obj.put("note",note);
        return obj;
    }

    @Override
    public Long getId() {
        return gameId;
    }
}
