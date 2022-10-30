package ru.rerumu.lists.model;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Objects;

public final class Series implements Cloneable{

    private final Long seriesId;
    private final Long seriesListId;
    private final String title;
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

//    public void setTitle(String title) {
//        this.title = title;
//    }

    public Long getSeriesListId() {
        return seriesListId;
    }

    public void setBookCount(Integer bookCount) {
        this.bookCount = bookCount;
    }

    public JSONObject toJSONObject(){
        JSONObject obj = new JSONObject();

        obj.put("seriesId", seriesId);
        obj.put("readListId", seriesListId);
        obj.put("title", title);
        obj.put("bookCount", bookCount);

        return obj;
    }

    @Override
    public String toString() {
        return this.toJSONObject().toString();
    }

    @Override
    public Series clone() {
        try {
            Series clone = (Series) super.clone();
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Series series = (Series) o;
        return Objects.equals(seriesId, series.seriesId) && Objects.equals(seriesListId, series.seriesListId) && Objects.equals(title, series.title) && Objects.equals(bookCount, series.bookCount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(seriesId, seriesListId, title, bookCount);
    }

    public final static class Builder{
        private long seriesId;
        private String title;
        private long readListId;

        public Builder seriesId(long seriesId){
            this.seriesId = seriesId;
            return this;
        }

        public Builder title(String title){
            this.title = title;
            return this;
        }

        public Builder readListId(long readListId){
            this.readListId = readListId;
            return this;
        }

        public Series build(){
            return new Series(seriesId,readListId,title);
        }
    }
}
