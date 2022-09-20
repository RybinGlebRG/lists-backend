package ru.rerumu.lists.views;

public class BookUpdateView {
    private final Long bookId;
    private final Long readListId;
    private final String title;
    private final Long authorId;
    private final int status;
    private final Long seriesId;
    private final Long order;

    public BookUpdateView(
            Long bookId,
            Long readListId,
            String title,
            Long authorId,
            int status,
            Long seriesId,
            Long order
    ){
        this.bookId = bookId;
        this.readListId = readListId;
        this.title = title;
        this.status = status;
        this.authorId = authorId;
        this.seriesId = seriesId;
        this.order = order;
    }

    public Long getBookId() {
        return bookId;
    }

    public Long getReadListId() {
        return readListId;
    }

    public int getStatus() {
        return status;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public Long getOrder() {
        return order;
    }

    public Long getSeriesId() {
        return seriesId;
    }

    public String getTitle() {
        return title;
    }

}
