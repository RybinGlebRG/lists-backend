package ru.rerumu.lists.views;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.rerumu.lists.model.series.Series;
import ru.rerumu.lists.model.SeriesBookRelation;

import java.util.*;

public class BookSeriesView {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final Series series;
    private final List<SeriesBookRelation> seriesBookRelationList;


    private BookSeriesView(
            Series series,
            List<SeriesBookRelation> seriesBookRelationList
    ){
        this.series = series;
        this.seriesBookRelationList = new ArrayList<>(seriesBookRelationList);
    }

//    public void sort(){
//        logger.debug("Unsorted: " + seriesBookRelationList);
//        Comparator<SeriesBookRelation> comparator = Comparator.comparingLong(SeriesBookRelation::order);
//        this.seriesBookRelationList.sort(comparator);
//        logger.debug("Sorted: " + seriesBookRelationList);
//    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookSeriesView that = (BookSeriesView) o;
        return Objects.equals(series, that.series);
    }

    @Override
    public int hashCode() {
        return Objects.hash(series);
    }

    public JSONObject toJSONObject(){
        JSONObject obj = series.toJSONObject();
//        JSONArray books = new JSONArray();
//        seriesBookRelationList.forEach(item -> {
//            JSONObject book = new JSONObject();
//            book.put("bookId",item.getBook().getBookId());
//            books.put(book);
//        });
//        obj.put("books",books);
        return obj;
    }

    @Override
    public String toString() {
        return this.toJSONObject().toString();
    }

    public static class Builder{

        private Series series;

        private List<SeriesBookRelation> seriesBookRelationList = new ArrayList<>();

        public Builder(Series series){
            this.series = series;
        }

        public Builder seriesBookRelationList(List<SeriesBookRelation> seriesBookRelationList){
            this.seriesBookRelationList = seriesBookRelationList;
            return this;
        }

        public BookSeriesView build(){
            BookSeriesView bookSeriesView = new BookSeriesView(series, seriesBookRelationList);
//            bookSeriesView.sort();
            return bookSeriesView;
        }

    }

}
