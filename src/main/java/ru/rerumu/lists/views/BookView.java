package ru.rerumu.lists.views;


import org.json.JSONObject;
import ru.rerumu.lists.model.*;

public class BookView {
    private Book book;
    private BookStatus bookStatus;
    private Author author;
    private Series series;

    private BookView(
            Book book,
            BookStatus bookStatus,
            Author author,
            Series series
    ) {
        this.book = book;
        this.bookStatus = bookStatus;
        this.author = author;
        this.series = series;
    }

    public JSONObject toJSONObject() {
        JSONObject obj = book.toJSONObject();
        obj.put("author", author != null ? author.toJSONObject() : null);
        obj.put("status", bookStatus.getNice());
        obj.put("series", series != null ? series.toJSONObject() : null);
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

        public BookView build() {
            return new BookView(book, bookStatus, author, series);
        }
    }
}
