package ru.rerumu.lists.model;

import org.json.JSONArray;
import org.json.JSONObject;
import ru.rerumu.lists.exception.EmptyMandatoryParameterException;
import ru.rerumu.lists.model.dto.SeriesBookDTO;
import ru.rerumu.lists.model.dto.SeriesDTO;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public record Series(
        Long seriesId,
        Long seriesListId,
        String title,
        Integer bookCount,
        List<SeriesItem> itemsList
) implements Cloneable{

//    private final Long seriesId;
//    private final Long seriesListId;
//    private final String title;
//    private Integer bookCount;
//    private final List<?> itemsList;

//    private LocalDateTime lastUpdateDate;

    public Series{
        if (itemsList==null){
            throw new IllegalArgumentException();
        } else {
            itemsList = new ArrayList<>(itemsList);
        }
    }

    public Series(Long seriesId, Long seriesListId, String title){
        this(seriesId,seriesListId,title,0,new ArrayList<>());
    }

//    private Series(Long seriesId, Long seriesListId, String title, int bookCount, List<?> itemsList){
//        this.seriesId = seriesId;
//        this.seriesListId = seriesListId;
//        this.title = title;
//        this.bookCount = bookCount;
//        this.itemsList = new ArrayList<>(itemsList);
////        this.lastUpdateDate = lastUpdateDate;
//    }

    @Deprecated
    public Long getSeriesId() {
        return seriesId;
    }

    @Deprecated
    public String getTitle() {
        return title;
    }

//    public void setTitle(String title) {
//        this.title = title;
//    }

    @Deprecated
    public Long getSeriesListId() {
        return seriesListId;
    }

//    public LocalDateTime getLastUpdateDate() {
//        return lastUpdateDate;
//    }

    @Deprecated
    public Integer getBookCount() {
        return bookCount;
    }

//    public void setBookCount(Integer bookCount) {
//        this.bookCount = bookCount;
//    }

    @Deprecated
    public List<?> getItemsList() {
        return new ArrayList<>(itemsList);
    }

    @Override
    public List<SeriesItem> itemsList() {
        return new ArrayList<>(itemsList);
    }

    public JSONObject toJSONObject(){
        JSONObject obj = new JSONObject();

        obj.put("seriesId", seriesId);
        obj.put("readListId", seriesListId);
        obj.put("title", title);
        JSONArray jsonArray = new JSONArray();
        for (Object item: itemsList){
            if (item instanceof Book book){
                jsonArray.put(book.toJSONObject());
            } else {
                throw new IllegalArgumentException();
            }
        }
        obj.put("items",jsonArray);

//        obj.put("bookCount", bookCount);

        return obj;
    }

    public JSONObject toJSONObject(String... fields){
        JSONObject obj = new JSONObject();
        for(String field: fields){
            switch (field){
                case "seriesId"-> obj.put("seriesId", seriesId);
                case "readListId" -> obj.put("readListId", seriesListId);
                case "title" -> obj.put("title", title);
                case "items" ->{
                    JSONArray jsonArray = new JSONArray();
                    for (Object item: itemsList){
                        if (item instanceof Book){
                            jsonArray.put(((Book) item).toJSONObject());
                        } else {
                            throw new IllegalArgumentException();
                        }
                    }
                    obj.put("items",jsonArray);
                }
                default -> throw new IllegalArgumentException();
            }
        }
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

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        Series series = (Series) o;
//        return Objects.equals(seriesId, series.seriesId) && Objects.equals(seriesListId, series.seriesListId) && Objects.equals(title, series.title) && Objects.equals(bookCount, series.bookCount);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(seriesId, seriesListId, title, bookCount);
//    }

    public final static class Builder{
        private long seriesId;
        private String title;
        private long readListId;

        private int bookCount;

        private LocalDateTime lastUpdateDate;

        private List<SeriesItem> itemList = new ArrayList<>();

        public Builder(){};

        public Builder(Series series){
            this.seriesId = series.getSeriesId();
            this.title = series.getTitle();
            this.readListId = series.getSeriesListId();
            this.bookCount = series.getBookCount() != null ? series.getBookCount()  : 0;
//            this.lastUpdateDate = series.getLastUpdateDate();
        }

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

        public Builder bookCount(int bookCount){
            this.bookCount = bookCount;
            return this;
        }

        public Builder itemList(List<SeriesItem> itemList){
            this.itemList = itemList;
            return this;
        }

        public Series build(){
            return new Series(seriesId,readListId,title, bookCount, itemList);
        }
    }
}
