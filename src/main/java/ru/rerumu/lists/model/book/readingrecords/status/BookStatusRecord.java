package ru.rerumu.lists.model.book.readingrecords.status;

import org.json.JSONObject;

import java.util.Objects;

public record BookStatusRecord(Integer statusId, String statusName) {
    public BookStatusRecord{
        Objects.requireNonNull(statusId,"Status id cannot be null");
        Objects.requireNonNull(statusName, "Status name cannot be null");
    }

    public JSONObject toJsonObject(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("statusId",statusId);
        jsonObject.put("statusName", statusName);
        return jsonObject;
    }

    @Override
    public String toString() {
        return toJsonObject().toString();
    }
}
