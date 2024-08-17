package ru.rerumu.lists.model;


import lombok.Getter;
import lombok.Setter;
import org.json.JSONObject;
import ru.rerumu.lists.exception.EmptyMandatoryParameterException;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

public class Title implements SeriesItem {

    private final static SeriesItemType SERIES_ITEM_TYPE = SeriesItemType.TITLE;

    @Getter
    private final Long titleId;

    @Getter
    private String name;

    @Getter
    private Date createDateUTC;

    @Getter
    private final Long watchListId;

    @Getter
    @Setter
    private Long statusId;

    @Getter
    @Setter
    private VideoType videoType;


    public Title(
            Long titleId,
            Long watchListId,
            String name,
            Date createDateUTC,
            Long statusId,
            VideoType videoType
    ) {
        this.titleId = titleId;
        this.watchListId = watchListId;
        this.name = name;
        this.createDateUTC = createDateUTC;
        this.statusId = statusId;
        this.videoType = videoType;
    }

    public int getdd() {
        return Integer.parseInt(new SimpleDateFormat("dd").format(this.createDateUTC));
    }

    public int getMonth() {
        return Integer.parseInt(new SimpleDateFormat("MM").format(this.createDateUTC));
    }

    public int getyyyy() {
        return Integer.parseInt(new SimpleDateFormat("yyyy").format(this.createDateUTC));
    }

    public int getHH() {
        return Integer.parseInt(new SimpleDateFormat("HH").format(this.createDateUTC));
    }

    public int getmm() {
        return Integer.parseInt(new SimpleDateFormat("mm").format(this.createDateUTC));
    }

    public int getss() {
        return Integer.parseInt(new SimpleDateFormat("ss").format(this.createDateUTC));
    }

    public void setName(String name) throws EmptyMandatoryParameterException {
        if (name == null || name.isEmpty()) {
            throw new EmptyMandatoryParameterException("Name is null or empty");
        }
        this.name = name;
    }

    public void setCreateDateUTC(Date createDateUTC) throws EmptyMandatoryParameterException {
        if (createDateUTC == null) {
            throw new EmptyMandatoryParameterException("createDateUTC is null");
        }
        this.createDateUTC = createDateUTC;
    }


    public JSONObject toJSONObject() {
        JSONObject obj = new JSONObject();
        obj.put("id", titleId);
        obj.put("name", name);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        obj.put("create_date_utc", sdf.format(createDateUTC));
        obj.put("watchListId", this.watchListId);
        obj.put("statusId", this.statusId);
        obj.put("videoType", videoType != null ? videoType.toJSONObject() : null);
        obj.put("itemType",SERIES_ITEM_TYPE.name());
        return obj;
    }

    @Override
    public LocalDateTime getUpdateDate() {
        return createDateUTC
                .toInstant()
                .atOffset(ZoneOffset.UTC)
                .toLocalDateTime();
    }

    public LocalDateTime getCreateDateLocal(){
        return createDateUTC
                .toInstant()
                .atOffset(ZoneOffset.UTC)
                .toLocalDateTime();
    }

    @Override
    public String toString() {
        return this.toJSONObject().toString();
    }

    public static class Builder {
        private Long titleId;
        private String name;
        private Date createDateUTC;
        private Long watchListId;
        private Long statusId;
        private VideoType videoType;

        public Builder titleId(Long titleId) {
            this.titleId = titleId;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder createDateUTC(Date createDateUTC) {
            this.createDateUTC = createDateUTC;
            return this;
        }

        public Builder watchListId(Long watchListId) {
            this.watchListId = watchListId;
            return this;
        }

        public Builder statusId(Long statusId) {
            this.statusId = statusId;
            return this;
        }

        public Builder videoType(VideoType videoType) {
            this.videoType = videoType;
            return this;
        }

        public Title build() {
            Title title = new Title(
                    titleId,
                    watchListId,
                    name,
                    createDateUTC,
                    statusId,
                    videoType
            );
            return title;
        }

    }
}