package ru.rerumu.lists.model;

import org.json.JSONObject;
import ru.rerumu.lists.exception.EmptyMandatoryParameterException;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Book {
    private final Long bookId;
    private final Long readListId;
    private String title;
    private Integer statusId;
    private Date insertDate;
    private Date lastUpdateDate;
    private Integer lastChapter;
    private Long seriesId;
    private Long authorId;
    private Long seriesOrder;
    private BookStatus bookStatus;
    private Author author;

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

    public void setSeriesId(Long seriesId) {
        this.seriesId = seriesId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public Long getSeriesOrder() {
        return seriesOrder;
    }

    public void setSeriesOrder(Long seriesOrder) {
        this.seriesOrder = seriesOrder;
    }

    public void setTitle(String title) throws EmptyMandatoryParameterException {
        if (title == null){
            throw new EmptyMandatoryParameterException("title is null");
        }
        this.title = title;
    }
    public void setStatusId(Integer statusId) throws EmptyMandatoryParameterException {
        if (statusId == null){
            throw new EmptyMandatoryParameterException("statusId is null");
        }
        this.statusId =statusId;
    }
    public void setInsertDate(Date insertDate) throws EmptyMandatoryParameterException {
        if (insertDate == null){
            throw new EmptyMandatoryParameterException("insertDate is null");
        }
        this.insertDate = insertDate;
    }
    public void setLastUpdateDate(Date lastUpdateDate) throws EmptyMandatoryParameterException {
        if (lastUpdateDate == null){
            throw new EmptyMandatoryParameterException("lastUpdateDate is null");
        }
        this.lastUpdateDate = lastUpdateDate;
    }
    public void setLastChapter(Integer lastChapter){
        this.lastChapter = lastChapter;
    }

    public int getdd(){
        return Integer.parseInt(new SimpleDateFormat("dd").format(this.insertDate));
    }

    public int getMonth(){
        return Integer.parseInt(new SimpleDateFormat("MM").format(this.insertDate));
    }

    public int getyyyy(){
        return Integer.parseInt(new SimpleDateFormat("yyyy").format(this.insertDate));
    }

    public int getHH(){
        return Integer.parseInt(new SimpleDateFormat("HH").format(this.insertDate));
    }

    public int getmm(){
        return Integer.parseInt(new SimpleDateFormat("mm").format(this.insertDate));
    }

    public int getss() {
        return Integer.parseInt(new SimpleDateFormat("ss").format(this.insertDate));
    }



    public Book(Long bookId,
                Long readListId,
                String title,
                Integer statusId,
                Date insertDate,
                Date lastUpdateDate,
                Integer lastChapter,
                Long seriesId,
                Long authorId,
                Long seriesOrder) {
        this.bookId = bookId;
        this.readListId = readListId;
        this.title=title;
        this.statusId = statusId;
        this.insertDate = insertDate;
        this.lastUpdateDate = lastUpdateDate;
        this.lastChapter = lastChapter;
        this.seriesId = seriesId;
        this.seriesOrder = seriesOrder;
        this.authorId = authorId;
    }

    public JSONObject toJSONObject(){
        JSONObject obj = new JSONObject();

        obj.put("bookId", bookId);
        obj.put("readListId", readListId);
        obj.put("title", title);
        obj.put("statusId", statusId);
        obj.put("insertDate", new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(insertDate));
        obj.put("lastUpdateDate", new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(lastUpdateDate));
        obj.put("lastChapter", lastChapter);
        obj.put("seriesId", seriesId);
        obj.put("seriesOrder", seriesOrder);
        obj.put("authorId", authorId);
        obj.put("bookStatus", bookStatus.toJSONObject());
        obj.put("author", author != null ?author.toJSONObject(): null);

        return obj;
    }

    @Override
    public String toString() {
        return this.toJSONObject().toString();
    }
}
