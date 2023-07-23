package ru.rerumu.lists.model;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public record Game(Integer gameId, String title, User user, LocalDateTime createDateUTC) implements SeriesItem {
    private final static SeriesItemType SERIES_ITEM_TYPE = SeriesItemType.GAME;

    public Game{
        if (title == null || user == null || createDateUTC == null){
            throw new IllegalArgumentException();
        }
    }

    public JSONObject toJSONObject(){
        JSONObject obj = new JSONObject();
        obj.put("gameId", gameId);
        obj.put("title", title);
        obj.put("createDateUTC", createDateUTC.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        obj.put("itemType",SERIES_ITEM_TYPE.name());
        return obj;
    }

    @Override
    public LocalDateTime getUpdateDate() {
        return createDateUTC;
    }

    public final static class Builder{
        private Integer gameId;
        private String title;
        private User user;
        private LocalDateTime createDateUTC;

        public Builder gameId(Integer gameId){
            this.gameId = gameId;
            return this;
        }

        public Builder title(String title){
            this.title = title;
            return this;
        }

        public Builder user(User user){
            this.user = user;
            return this;
        }

        public Builder createDateUTC(LocalDateTime createDateUTC){
            this.createDateUTC = createDateUTC;
            return this;
        }

        public Game build(){
            return new Game(
                    gameId,
                    title,
                    user,
                    createDateUTC != null ? createDateUTC : LocalDateTime.now(ZoneOffset.UTC)
            );
        }
    }
}
