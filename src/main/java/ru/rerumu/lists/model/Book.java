package ru.rerumu.lists.model;

import org.json.JSONObject;
import ru.rerumu.lists.exception.EmptyMandatoryParameterException;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public final class Book implements Cloneable{
    private final Long bookId;
    private final Long readListId;
    private final String title;
    private final Integer statusId;
    private final BookStatus bookStatus;

    private final Date insertDate;
    private final Date lastUpdateDate;
    private final Integer lastChapter;
    private final Long seriesId;
    private final Long authorId;
    private final Long seriesOrder;

    public Book(Long bookId,
                Long readListId,
                String title,
                Integer statusId,
                BookStatus bookStatus,
                Date insertDate,
                Date lastUpdateDate,
                Integer lastChapter,
                Long seriesId,
                Long authorId,
                Long seriesOrder) throws EmptyMandatoryParameterException {
        this.bookId = bookId;
        this.readListId = readListId;

        if (title == null) {
            throw new EmptyMandatoryParameterException("title is null");
        }
        this.title = title;

        if (statusId == null) {
            throw new EmptyMandatoryParameterException("statusId is null");
        }
        this.statusId = statusId;

        if (bookStatus == null){
            throw new EmptyMandatoryParameterException("bookStatus is null");
        }
        this.bookStatus = bookStatus;

        if (insertDate == null) {
            throw new EmptyMandatoryParameterException("insertDate is null");
        }
        this.insertDate = insertDate;

        if (lastUpdateDate == null) {
            throw new EmptyMandatoryParameterException("lastUpdateDate is null");
        }
        this.lastUpdateDate = lastUpdateDate;

        this.lastChapter = lastChapter;
        this.seriesId = seriesId;
        this.seriesOrder = seriesOrder;
        this.authorId = authorId;
    }

    public Long getReadListId() {
        return readListId;
    }

    public Long getBookId() {
        return bookId;
    }

    public String getTitle() {

        return title;
    }

    public Integer getStatusId() {
        return statusId;
    }

    public BookStatus getBookStatus() {
        return bookStatus;
    }

    public Date getInsertDate() {
        return insertDate;
    }

    public Date getLastUpdateDate() {
        return lastUpdateDate;
    }

    public Integer getLastChapter() {
        return lastChapter;
    }

    public Long getSeriesId() {
        return seriesId;
    }

    public Long getAuthorId() {
        return authorId;
    }

    //    public void setSeriesId(Long seriesId) {
//        this.seriesId = seriesId;
//    }
//
//    public void setAuthorId(Long authorId) {
//        this.authorId = authorId;
//    }
//
    public Long getSeriesOrder() {
        return seriesOrder;
    }
//
//    public void setSeriesOrder(Long seriesOrder) {
//        this.seriesOrder = seriesOrder;
//    }
//
//    public void setTitle(String title) throws EmptyMandatoryParameterException {
//        if (title == null) {
//            throw new EmptyMandatoryParameterException("title is null");
//        }
//        this.title = title;
//    }
//
//    public void setStatusId(Integer statusId) throws EmptyMandatoryParameterException {
//        if (statusId == null) {
//            throw new EmptyMandatoryParameterException("statusId is null");
//        }
//        this.statusId = statusId;
//    }
//
//    public void setInsertDate(Date insertDate) throws EmptyMandatoryParameterException {
//        if (insertDate == null) {
//            throw new EmptyMandatoryParameterException("insertDate is null");
//        }
//        this.insertDate = insertDate;
//    }
//
//    public void setLastUpdateDate(Date lastUpdateDate) throws EmptyMandatoryParameterException {
//        if (lastUpdateDate == null) {
//            throw new EmptyMandatoryParameterException("lastUpdateDate is null");
//        }
//        this.lastUpdateDate = lastUpdateDate;
//    }
//
//    public void setLastChapter(Integer lastChapter) {
//        this.lastChapter = lastChapter;
//    }

    public int getdd() {
        return Integer.parseInt(new SimpleDateFormat("dd").format(this.insertDate));
    }

    public int getMonth() {
        return Integer.parseInt(new SimpleDateFormat("MM").format(this.insertDate));
    }

    public int getyyyy() {
        return Integer.parseInt(new SimpleDateFormat("yyyy").format(this.insertDate));
    }

    public int getHH() {
        return Integer.parseInt(new SimpleDateFormat("HH").format(this.insertDate));
    }

    public int getmm() {
        return Integer.parseInt(new SimpleDateFormat("mm").format(this.insertDate));
    }

    public int getss() {
        return Integer.parseInt(new SimpleDateFormat("ss").format(this.insertDate));
    }


//    @Override
//    protected Object clone() throws CloneNotSupportedException {
//        return super.clone();
//    }

    @Override
    public Book clone() {
        try {
            Book clone = (Book) super.clone();
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    public JSONObject toJSONObject() {
        JSONObject obj = new JSONObject();

        obj.put("bookId", bookId);
        obj.put("readListId", readListId);
        obj.put("title", title);
        obj.put("statusId", statusId);
        JSONObject bookStatusJson = new JSONObject();
        bookStatusJson.put("statusId",bookStatus.getId());
        bookStatusJson.put("statusName",bookStatus.getNice());
        obj.put("bookStatus", bookStatusJson);
        obj.put(
                "insertDate",
                new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(insertDate)
        );
        obj.put(
                "lastUpdateDate",
                new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(lastUpdateDate)
        );
        obj.put("lastChapter", lastChapter);
        obj.put("seriesId", seriesId);
        obj.put("seriesOrder", seriesOrder);
        obj.put("authorId", authorId);
        obj.put("statusId", statusId);

        return obj;
    }



    @Override
    public String toString() {
        return this.toJSONObject().toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return Objects.equals(bookId, book.bookId) && Objects.equals(readListId, book.readListId)
                && Objects.equals(title, book.title) && Objects.equals(statusId, book.statusId)
                && Objects.equals(insertDate, book.insertDate) && Objects.equals(lastUpdateDate, book.lastUpdateDate)
                && Objects.equals(lastChapter, book.lastChapter) && Objects.equals(seriesId, book.seriesId)
                && Objects.equals(authorId, book.authorId) && Objects.equals(seriesOrder, book.seriesOrder);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bookId, readListId, title, statusId, insertDate, lastUpdateDate, lastChapter, seriesId, authorId, seriesOrder);
    }

    public final static class Builder {
        private Long bookId;
        private Long readListId;
        private String title;
        private Integer statusId;

        private BookStatus bookStatus;
        private Date insertDate;
        private Date lastUpdateDate;
        private Integer lastChapter;
        private Long seriesId;
        private Long authorId;
        private Long seriesOrder;

        public Builder() {
        }

        public Builder(Book book) {
            this.bookId = book.bookId;
            this.readListId = book.readListId;
            this.title = book.title;
            this.statusId = book.statusId;
            this.bookStatus = book.bookStatus;
            this.insertDate = book.insertDate;
            this.lastUpdateDate = book.lastUpdateDate;
            this.lastChapter = book.lastChapter;
            this.seriesId = book.seriesId;
            this.authorId = book.authorId;
            this.seriesOrder = book.seriesOrder;
        }

        public Builder bookId(Long bookId) {
            this.bookId = bookId;
            return this;
        }

        public Builder readListId(Long readListId) {
            this.readListId = readListId;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder statusId(Integer statusId) {
            this.statusId = statusId;
            return this;
        }

        public Builder bookStatus(BookStatus bookStatus){
            this.bookStatus = bookStatus;
            return this;
        }

        public Builder insertDate(Date insertDate) {
            this.insertDate = insertDate;
            return this;
        }

        public Builder lastUpdateDate(Date lastUpdateDate) {
            this.lastUpdateDate = lastUpdateDate;
            return this;
        }

        public Builder lastChapter(Integer lastChapter) {
            this.lastChapter = lastChapter;
            return this;
        }

        public Builder seriesId(Long seriesId) {
            this.seriesId = seriesId;
            return this;
        }

        public Builder authorId(Long authorId) {
            this.authorId = authorId;
            return this;
        }

        public Builder seriesOrder(Long seriesOrder) {
            this.seriesOrder = seriesOrder;
            return this;
        }


        public Book build() throws EmptyMandatoryParameterException {
            return new Book(
                    bookId,
                    readListId,
                    title,
                    statusId,
                    bookStatus,
                    insertDate,
                    lastUpdateDate,
                    lastChapter,
                    seriesId,
                    authorId,
                    seriesOrder
            );
        }
    }
}
