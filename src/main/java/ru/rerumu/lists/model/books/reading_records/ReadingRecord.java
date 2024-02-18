package ru.rerumu.lists.model.books.reading_records;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.json.JSONObject;
import ru.rerumu.lists.model.BookStatusRecord;
import ru.rerumu.lists.utils.LocalDateTimeSerializer;

import java.time.LocalDateTime;

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
}
