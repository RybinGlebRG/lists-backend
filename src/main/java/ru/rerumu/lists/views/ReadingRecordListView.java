package ru.rerumu.lists.views;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.json.JSONArray;
import org.json.JSONObject;
import ru.rerumu.lists.model.book.reading_records.ReadingRecord;
import ru.rerumu.lists.model.book.reading_records.ReadingRecordImpl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public record ReadingRecordListView(
        List<ReadingRecord> readingRecordList
) {

    public ReadingRecordListView {
        readingRecordList = new ArrayList<>(readingRecordList);
    }

    public JsonNode toJsonNode(){
        ObjectMapper objectMapper = new ObjectMapper();
        ArrayNode arrayNode = readingRecordList.stream()
                .map(readingRecord -> ((ReadingRecordImpl) readingRecord).toJsonNode())
                .collect(
                        ()->objectMapper.createArrayNode(),
                        (arrayNode1,jsonNode)->arrayNode1.add(jsonNode),
                        (arrayNode1,arrayNode2)->arrayNode1.addAll(arrayNode2)
                );
        ObjectNode res = objectMapper.createObjectNode();
        res.putIfAbsent("items",arrayNode);
        return res;
    }

    public JSONObject toJSONObject() {
        Comparator<ReadingRecord> readingRecordComparator = ReadingRecord::compareTo;
        readingRecordList.sort(readingRecordComparator);

        JSONObject obj = new JSONObject();
        JSONArray bookArray = readingRecordList.stream()
                .map(ReadingRecord::toJSONObject)
                .collect(JSONArray::new, JSONArray::put, JSONArray::putAll);
        obj.put("items", bookArray);
        return obj;
    }

    @Override
    public String toString() {
//        ObjectMapper objectMapper = new ObjectMapper()
//                .registerModule(new JavaTimeModule());
//        JsonNode jsonNode = toJsonNode();
//        try {
//            return objectMapper.writeValueAsString(jsonNode);
//        } catch (JsonProcessingException e) {
//            throw new RuntimeException(e);
//        }
        return this.toJSONObject().toString();
    }
}
