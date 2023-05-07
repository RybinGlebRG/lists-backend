package ru.rerumu.lists.views;

import org.json.JSONArray;
import org.json.JSONObject;
import ru.rerumu.lists.model.BookStatusRecord;
import ru.rerumu.lists.model.BookType;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BookStatusesView {

    private final List<BookStatusRecord> bookStatusRecordList;

    public BookStatusesView(List<BookStatusRecord> bookStatusRecordList) {
        this.bookStatusRecordList = bookStatusRecordList;
    }

    public JSONObject toJsonObject(){
        JSONObject obj = new JSONObject();
        JSONArray array = bookStatusRecordList.stream()
                .map(BookStatusRecord::toJsonObject)
                .collect(JSONArray::new,JSONArray::put,JSONArray::putAll);

        obj.put("items",array);
        return obj;
    }

    @Override
    public String toString(){
        return toJsonObject().toString();
    }
}
