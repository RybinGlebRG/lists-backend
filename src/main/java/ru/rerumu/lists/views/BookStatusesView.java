package ru.rerumu.lists.views;

import org.json.JSONArray;
import org.json.JSONObject;
import ru.rerumu.lists.domain.bookstatus.BookStatusRecord;

import java.util.List;

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
