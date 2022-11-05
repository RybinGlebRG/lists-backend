package ru.rerumu.lists.views;

import org.json.JSONArray;
import org.json.JSONObject;
import ru.rerumu.lists.model.Book;
import ru.rerumu.lists.model.Series;
import ru.rerumu.lists.model.books.SearchOrder;
import ru.rerumu.lists.model.books.SortItem;

import java.util.Comparator;
import java.util.List;

public class BookListView {

    private final List<Book> bookList;

    public BookListView(List<Book> bookList) {
        this.bookList = bookList;
    }

    public void sort() {
        Comparator<Book> comparator = new Comparator<Book>() {
            @Override
            public int compare(Book o1, Book o2) {
                int res = Integer.compare(o1.getyyyy(), o2.getyyyy());
                if (res != 0) {
                    return res;
                }
                res = Integer.compare(o1.getMonth(), o2.getMonth());
                if (res != 0) {
                    return res;
                }

                res = Integer.compare(o1.getdd(), o2.getdd());
                if (res != 0) {
                    return res;
                }

                res = Integer.compare(o1.getHH(), o2.getHH());
                if (res != 0) {
                    return res;
                }

                res = Integer.compare(o1.getmm(), o2.getmm());
                if (res != 0) {
                    return res;
                }

                res = Integer.compare(o1.getss(), o2.getss());
                if (res != 0) {
                    return res;
                }

                res = o1.getTitle().compareTo(o2.getTitle());
                if (res != 0) {
                    return res;
                }
                res = Long.compare(o1.getBookId(), o2.getBookId());
                return res;
            }
        };

        this.bookList.sort(comparator);
    }

    public void sort(List<SortItem> sortItemList) {
        Comparator<Book> comparator = (o1, o2) -> {

            for (SortItem sortItem : sortItemList) {

                if (sortItem.getSortField().equals("createDate")){
                    int res = Integer.compare(o1.getyyyy(), o2.getyyyy());
                    if (sortItem.getSearchOrder()== SearchOrder.DESC){
                        res*=-1;
                    }
                    if (res != 0) {
                        return res;
                    }
                    res = Integer.compare(o1.getMonth(), o2.getMonth());
                    if (sortItem.getSearchOrder()== SearchOrder.DESC){
                        res*=-1;
                    }
                    if (res != 0) {
                        return res;
                    }

                    res = Integer.compare(o1.getdd(), o2.getdd());
                    if (sortItem.getSearchOrder()== SearchOrder.DESC){
                        res*=-1;
                    }
                    if (res != 0) {
                        return res;
                    }

                    res = Integer.compare(o1.getHH(), o2.getHH());
                    if (sortItem.getSearchOrder()== SearchOrder.DESC){
                        res*=-1;
                    }
                    if (res != 0) {
                        return res;
                    }

                    res = Integer.compare(o1.getmm(), o2.getmm());
                    if (sortItem.getSearchOrder()== SearchOrder.DESC){
                        res*=-1;
                    }
                    if (res != 0) {
                        return res;
                    }

                    res = Integer.compare(o1.getss(), o2.getss());
                    if (sortItem.getSearchOrder()== SearchOrder.DESC){
                        res*=-1;
                    }
                    if (res != 0) {
                        return res;
                    }

                    return 0;
                }
            }

            return 0;
        };

        this.bookList.sort(comparator);
    }

    public JSONObject toJSONObject() {
        JSONObject obj = new JSONObject();
        JSONArray bookArray = new JSONArray();
        for (Book item : this.bookList) {
            bookArray.put(item.toJSONObject());
        }
        obj.put("items", bookArray);
        return obj;
    }

    @Override
    public String toString() {

        return this.toJSONObject().toString();
    }
}
