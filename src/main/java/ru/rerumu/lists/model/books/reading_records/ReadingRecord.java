package ru.rerumu.lists.model.books.reading_records;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.Builder;
import org.json.JSONObject;
import ru.rerumu.lists.model.BookStatusRecord;
import ru.rerumu.lists.utils.LocalDateTimeSerializer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Builder(toBuilder = true)
public record ReadingRecord(
        Long recordId,
        Long bookId,
        BookStatusRecord bookStatus,
        @JsonSerialize(using = LocalDateTimeSerializer.class)
        LocalDateTime startDate,
        @JsonSerialize(using = LocalDateTimeSerializer.class)
        LocalDateTime endDate,
        Boolean isMigrated
) {

    public JsonNode toJsonNode(){
        ObjectMapper objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule());
        JsonNode jsonNode = objectMapper.valueToTree(this);
        return jsonNode;
    }

    public JSONObject toJSONObject(){
        JSONObject obj = new JSONObject();

        obj.put("recordId", recordId);
        obj.put("bookId", bookId);

        JSONObject recordStatusJson = new JSONObject();
        recordStatusJson.put("statusId", bookStatus.statusId());
        recordStatusJson.put("statusName", bookStatus.statusName());
        obj.put("bookStatus", recordStatusJson);

        obj.put("startDate", startDate.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        if (endDate != null) {
            obj.put("endDate", endDate.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        }
        obj.put("isMigrated", isMigrated);

        return obj;
    }
}
