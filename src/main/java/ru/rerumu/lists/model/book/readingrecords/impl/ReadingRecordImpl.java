package ru.rerumu.lists.model.book.readingrecords.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import ru.rerumu.lists.dao.book.readingrecord.ReadingRecordsRepository;
import ru.rerumu.lists.model.book.readingrecords.status.BookStatusRecord;
import ru.rerumu.lists.model.book.readingrecords.ReadingRecord;
import ru.rerumu.lists.model.book.readingrecords.ReadingRecordDTO;
import ru.rerumu.lists.crosscut.utils.LocalDateTimeSerializer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Slf4j
@ToString
public class ReadingRecordImpl implements ReadingRecord {

    @Getter
    private final Long recordId;

    private final Long bookId;

    private BookStatusRecord bookStatus;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime startDate;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime endDate;

    private Boolean isMigrated;

    private Long lastChapter;

    @ToString.Exclude
    private final ReadingRecordsRepository readingRecordsRepository;


    ReadingRecordImpl(
            Long recordId,
            Long bookId,
            BookStatusRecord bookStatus,
            LocalDateTime startDate,
            LocalDateTime endDate,
            Boolean isMigrated,
            Long lastChapter,
            ReadingRecordsRepository readingRecordsRepository
    ) {
        this.recordId = recordId;
        this.bookId = bookId;
        this.bookStatus = bookStatus;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isMigrated = isMigrated;
        this.lastChapter = lastChapter;
        this.readingRecordsRepository = readingRecordsRepository;
    }

    public JsonNode toJsonNode(){
        ObjectMapper objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule());
        JsonNode jsonNode = objectMapper.valueToTree(this);
        return jsonNode;
    }

    public JSONObject toJSONObject(){

        log.debug("toJSONObject: {}", this);

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
        obj.put("lastChapter", lastChapter);

        return obj;
    }

    @Override
    public Long getId() {
        return recordId;
    }

    @Override
    public Long getBookId() {
        return bookId;
    }

    @Override
    public void save(){
        readingRecordsRepository.update(this.toDTO());
    }

    @Override
    public void delete() {
        readingRecordsRepository.delete(recordId);
    }

    @Override
    public void setStatus(@NonNull BookStatusRecord bookStatusRecord) {
        bookStatus = bookStatusRecord;
    }

    @Override
    public void setStartDate(@NonNull LocalDateTime startDate) {
        this.startDate = startDate;
    }

    @Override
    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    @Override
    public void setLastChapter(Long lastChapter) {
        this.lastChapter = lastChapter;
    }

    @Override
    public boolean statusEquals(@NonNull Long statusId) {
        return Long.valueOf(bookStatus.statusId()).equals(statusId);
    }

    @Override
    public ReadingRecordDTO toDTO() {
        return new ReadingRecordDTO(
                recordId,
                bookId,
                bookStatus,
                startDate,
                endDate,
                isMigrated,
                lastChapter
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReadingRecordImpl that = (ReadingRecordImpl) o;
        return Objects.equals(recordId, that.recordId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(recordId);
    }

    @Override
    public int compareTo(@NonNull ReadingRecord o) {
        return startDate.compareTo(((ReadingRecordImpl) o).startDate);
    }
}
