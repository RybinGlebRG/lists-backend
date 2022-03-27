package ru.rerumu.lists.model;

import org.json.JSONObject;

import java.text.SimpleDateFormat;

public class Series {

    private final Long seriesId;
    private final Long seriesListId;
    private String title;
    private Integer bookCount;

    public Series(Long seriesId, Long seriesListId, String title){
        this.seriesId = seriesId;
        this.seriesListId = seriesListId;
        this.title = title;
    }

    public Long getSeriesId() {
        return seriesId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getSeriesListId() {
        return seriesListId;
    }

    public void setBookCount(Integer bookCount) {
        this.bookCount = bookCount;
    }

    public JSONObject toJSONObject(){
        JSONObject obj = new JSONObject();

        obj.put("seriesId", seriesId);
        obj.put("seriesListId", seriesListId);
        obj.put("title", title);
        obj.put("bookCount", bookCount);

        return obj;
    }

    @Override
    public String toString() {
        return this.toJSONObject().toString();
    }
}
