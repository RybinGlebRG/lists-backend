package ru.rerumu.lists.views;

import org.json.JSONArray;
import org.json.JSONObject;
import ru.rerumu.lists.domain.booktype.BookType;

import java.util.List;

public class BookTypesListView {

    private final List<BookType> bookTypeList;

    public BookTypesListView(List<BookType> bookTypeList){
        this.bookTypeList = bookTypeList;
    }

    public JSONObject toJsonObject(){
        JSONObject obj = new JSONObject();
        JSONArray bookTypesArray = new JSONArray();
        for(BookType bookType: bookTypeList){
            bookTypesArray.put(bookType.toJsonObject());
        }
        obj.put("items",bookTypesArray);
        return obj;
    }

    @Override
    public String toString(){
        return toJsonObject().toString();
    }
}
