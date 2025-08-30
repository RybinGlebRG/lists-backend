package ru.rerumu.lists.domain.game;

import lombok.Getter;
import lombok.NonNull;
import org.json.JSONObject;
import ru.rerumu.lists.crosscut.exception.NotImplementedException;
import ru.rerumu.lists.domain.series.item.SeriesItem;
import ru.rerumu.lists.domain.series.item.SeriesItemDTOv2;
import ru.rerumu.lists.domain.series.item.SeriesItemType;
import ru.rerumu.lists.domain.user.User;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

@Getter
public class Game implements SeriesItem {
    private final static SeriesItemType SERIES_ITEM_TYPE = SeriesItemType.GAME;

    private Integer gameId;
    private String title;
    private User user;
    private LocalDateTime createDateUTC;
    private String note;

    public Game(Integer gameId, @NonNull String title, @NonNull User user, @NonNull LocalDateTime createDateUTC, String note) {
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
    public LocalDateTime getUpdateDate() {
        return createDateUTC;
    }

    @Override
    public SeriesItemDTOv2 toDTO() {
        throw new NotImplementedException();
    }

    public final static class Builder{
        private Integer gameId;
        private String title;
        private User user;
        private LocalDateTime createDateUTC;

        private String note;

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

        public Builder note(String note){
            this.note = note;
            return this;
        }

        public Game build(){
            return new Game(
                    gameId,
                    title,
                    user,
                    createDateUTC != null ? createDateUTC : LocalDateTime.now(ZoneOffset.UTC),
                    note
            );
        }
    }
}
