package ru.rerumu.lists.views;


import org.json.JSONArray;
import org.json.JSONObject;
import ru.rerumu.lists.controller.author.out.AuthorView;
import ru.rerumu.lists.model.author.impl.AuthorImpl;
import ru.rerumu.lists.model.AuthorBookRelation;
import ru.rerumu.lists.model.BookStatus;
import ru.rerumu.lists.model.SeriesBookRelation;
import ru.rerumu.lists.model.book.impl.BookImpl;
import ru.rerumu.lists.model.series.Series;

import java.util.List;

public class BookView {
    private BookImpl book;
    private BookStatus bookStatus;
    private AuthorImpl author;
    private Series series;
    private List<AuthorBookRelation> authorBookRelationList;
    private List<SeriesBookRelation> seriesBookRelationList;
    private List<Series> seriesList;

    private BookView(
            BookImpl book,
            BookStatus bookStatus,
            AuthorImpl author,
            Series series,
            List<AuthorBookRelation> authorBookRelationList,
            List<SeriesBookRelation> seriesBookRelationList,
            List<Series> seriesList
    ) {
        this.book = book;
        this.bookStatus = bookStatus;
        this.author = author;
        this.series = series;
        this.authorBookRelationList = authorBookRelationList;
        this.seriesBookRelationList = seriesBookRelationList;
        this.seriesList = seriesList;
    }

    private JSONArray formatSeriesList(){
        JSONArray arr = new JSONArray();
        for(Series series: seriesList){
            arr.put(series.toJSONObject("seriesId","title"));
        }
        return arr;
    }

    public JSONObject toJSONObject() {
        JSONObject obj = book.toJSONObject();

        JSONArray authors = new JSONArray();
        if (authorBookRelationList != null) {
            for (AuthorBookRelation authorBookRelation : authorBookRelationList) {
                authors.put(new AuthorView(authorBookRelation.getAuthor()).toJSONObject());
            }
        }
//        obj.put("author", author != null ? author.toJSONObject() : null);
        obj.put("authors",authors);

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
        private Series series;
        private List<AuthorBookRelation> authorBookRelationList;

        private List<SeriesBookRelation> seriesBookRelationList;
        private List<Series> seriesList;


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

        public Builder series(Series series) {
            this.series = series;
            return this;
        }

        public Builder authorBookRelation(List<AuthorBookRelation> authorBookRelationList){
            this.authorBookRelationList = authorBookRelationList;
            return this;
        }

        public Builder seriesBookRelation(List<SeriesBookRelation> seriesBookRelationList){
            this.seriesBookRelationList = seriesBookRelationList;
            return this;
        }

        public Builder seriesList(List<Series> seriesList){
            this.seriesList = seriesList;
            return this;
        }

        public BookView build() {
            return new BookView(
                    book,
                    bookStatus,
                    author,
                    series,
                    authorBookRelationList,
                    seriesBookRelationList,
                    seriesList
            );
        }
    }
}
