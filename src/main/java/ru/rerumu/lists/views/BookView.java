package ru.rerumu.lists.views;


import org.json.JSONArray;
import org.json.JSONObject;
import ru.rerumu.lists.model.*;

import java.util.List;

public class BookView {
    private Book book;
    private BookStatus bookStatus;
    private Author author;
    private Series series;
    private List<AuthorBookRelation> authorBookRelationList;
    private List<SeriesBookRelation> seriesBookRelationList;

    private BookView(
            Book book,
            BookStatus bookStatus,
            Author author,
            Series series,
            List<AuthorBookRelation> authorBookRelationList,
            List<SeriesBookRelation> seriesBookRelationList
    ) {
        this.book = book;
        this.bookStatus = bookStatus;
        this.author = author;
        this.series = series;
        this.authorBookRelationList = authorBookRelationList;
        this.seriesBookRelationList = seriesBookRelationList;
    }

    public JSONObject toJSONObject() {
        JSONObject obj = book.toJSONObject();

        JSONArray authors = new JSONArray();
        if (authorBookRelationList != null) {
            for (AuthorBookRelation authorBookRelation : authorBookRelationList) {
                authors.put(authorBookRelation.getAuthor().toJSONObject());
            }
        }
//        obj.put("author", author != null ? author.toJSONObject() : null);
        obj.put("authors",authors);

        JSONArray seriesList = new JSONArray();
        if (seriesBookRelationList != null){
            for (SeriesBookRelation seriesBookRelation: seriesBookRelationList){
                seriesList.put(seriesBookRelation.getSeries().toJSONObject());
            }
        }
        obj.put("status", bookStatus != null ? bookStatus.getNice() : null);
        obj.put("series", seriesList);
        return obj;
    }

    @Override
    public String toString() {
        return this.toJSONObject().toString();
    }

    public static class Builder {

        private Book book;
        private BookStatus bookStatus;
        private Author author;
        private Series series;
        private List<AuthorBookRelation> authorBookRelationList;

        private List<SeriesBookRelation> seriesBookRelationList;


        public Builder book(Book book) {
            this.book = book;
            return this;
        }

        public Builder book(BookStatus bookStatus) {
            this.bookStatus = bookStatus;
            return this;
        }

        public Builder author(Author author) {
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

        public BookView build() {
            return new BookView(book, bookStatus, author, series,authorBookRelationList, seriesBookRelationList);
        }
    }
}
