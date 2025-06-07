package ru.rerumu.lists.views;


import org.json.JSONArray;
import org.json.JSONObject;
import ru.rerumu.lists.model.BookStatus;
import ru.rerumu.lists.model.SeriesBookRelation;
import ru.rerumu.lists.model.author.impl.AuthorImpl;
import ru.rerumu.lists.model.book.impl.BookImpl;
import ru.rerumu.lists.model.series.impl.SeriesImpl;

import java.util.List;

public class BookView {
    private BookImpl book;
    private BookStatus bookStatus;
    private AuthorImpl author;
    private SeriesImpl series;
    private List<SeriesBookRelation> seriesBookRelationList;
    private List<SeriesImpl> seriesList;

    private BookView(
            BookImpl book,
            BookStatus bookStatus,
            AuthorImpl author,
            SeriesImpl series,
            List<SeriesBookRelation> seriesBookRelationList,
            List<SeriesImpl> seriesList
    ) {
        this.book = book;
        this.bookStatus = bookStatus;
        this.author = author;
        this.series = series;
        this.seriesBookRelationList = seriesBookRelationList;
        this.seriesList = seriesList;
    }

    private JSONArray formatSeriesList(){
        JSONArray arr = new JSONArray();
        for(SeriesImpl series: seriesList){
            arr.put(series.toJSONObject("seriesId","title"));
        }
        return arr;
    }

    public JSONObject toJSONObject() {
        JSONObject obj = book.toJSONObject();

//        JSONArray seriesList = new JSONArray();
//        if (seriesBookRelationList != null){
//            for (SeriesBookRelation seriesBookRelation: seriesBookRelationList){
//                JSONObject seriesEntry = seriesBookRelation.series().toJSONObject();
//                seriesEntry.put("seriesOrder",seriesBookRelation.order());
//                seriesList.put(seriesEntry);
//            }
//        }
        obj.put("status", bookStatus != null ? bookStatus.getNice() : null);
        obj.put("seriesList", formatSeriesList());
        return obj;
    }

    @Override
    public String toString() {
        return this.toJSONObject().toString();
    }

    public static class Builder {

        private BookImpl book;
        private BookStatus bookStatus;
        private AuthorImpl author;
        private SeriesImpl series;

        private List<SeriesBookRelation> seriesBookRelationList;
        private List<SeriesImpl> seriesList;


        public Builder bookStatus(BookImpl book) {
            this.book = book;
            return this;
        }

        public Builder bookStatus(BookStatus bookStatus) {
            this.bookStatus = bookStatus;
            return this;
        }

        public Builder author(AuthorImpl author) {
            this.author = author;
            return this;
        }

        public Builder series(SeriesImpl series) {
            this.series = series;
            return this;
        }

        public Builder seriesBookRelation(List<SeriesBookRelation> seriesBookRelationList){
            this.seriesBookRelationList = seriesBookRelationList;
            return this;
        }

        public Builder seriesList(List<SeriesImpl> seriesList){
            this.seriesList = seriesList;
            return this;
        }

        public BookView build() {
            return new BookView(
                    book,
                    bookStatus,
                    author,
                    series,
                    seriesBookRelationList,
                    seriesList
            );
        }
    }
}
