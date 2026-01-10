package ru.rerumu.lists.domain.readingrecords.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import ru.rerumu.lists.crosscut.utils.DateFactory;
import ru.rerumu.lists.crosscut.utils.LocalDateTimeSerializer;
import ru.rerumu.lists.domain.readingrecords.ReadingRecord;
import ru.rerumu.lists.domain.readingrecordstatus.ReadingRecordStatuses;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Slf4j
@ToString(callSuper = true, doNotUseGetters = true)
public class ReadingRecordImpl implements ReadingRecord {

    @Getter
    private final Long recordId;

    @Getter
    private final Long bookId;

    @Getter
    private ReadingRecordStatuses bookStatus;

    @Getter
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime startDate;

    @Getter
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime endDate;

    @Getter
    private final Boolean isMigrated;

    @Getter
    private Long lastChapter;

    /**
     * Update date of the record
     */
    @Getter
    private LocalDateTime updateDate;

    @ToString.Exclude
    private final DateFactory dateFactory;


    ReadingRecordImpl(
            Long recordId,
            Long bookId,
            ReadingRecordStatuses bookStatus,
            LocalDateTime startDate,
            LocalDateTime endDate,
            Boolean isMigrated,
            Long lastChapter,
            DateFactory dateFactory,
            LocalDateTime updateDate
    ) {
        this.recordId = recordId;
        this.bookId = bookId;
        this.bookStatus = bookStatus;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isMigrated = isMigrated;
        this.lastChapter = lastChapter;
        this.dateFactory = dateFactory;
        this.updateDate = updateDate;
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
    public boolean update(
            @NonNull ReadingRecordStatuses bookStatusRecord,
            @NonNull LocalDateTime startDate,
            LocalDateTime endDate,
            Long lastChapter
    ) {
        boolean isChanged = false;

        // Update end date
        if (endDate != null) {
            this.endDate = endDate;
            isChanged = true;
        }
        // or close record if status changed to "Completed"
        else if (bookStatusRecord.equals(ReadingRecordStatuses.COMPLETED)) {
            this.endDate = dateFactory.getLocalDateTime();
            isChanged = true;
        }

        if (!this.bookStatus.equals(bookStatusRecord)) {
            this.bookStatus = bookStatusRecord;
            isChanged = true;
        }

        if (!this.startDate.equals(startDate)) {
            this.startDate = startDate;
            isChanged = true;
        }

        if (!Objects.equals(this.lastChapter, lastChapter)) {
            this.lastChapter = lastChapter;
            isChanged = true;
        }

        if (isChanged) {
            this.updateDate = dateFactory.getLocalDateTime();
        }

        return isChanged;
    }

    @Override
    public boolean statusEquals(@NonNull Long statusId) {
        return bookStatus.getId().equals(statusId);
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
    public ReadingRecordImpl deepCopy() {
        return new ReadingRecordImpl(
                recordId,
                bookId,
                bookStatus,
                startDate,
                endDate,
                isMigrated,
                lastChapter,
                dateFactory,
                updateDate
        );
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
        recordStatusJson.put("statusId", bookStatus.getId());
        recordStatusJson.put("statusName", bookStatus.getName());
        obj.put("bookStatus", recordStatusJson);

        obj.put("startDate", startDate.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        if (endDate != null) {
            obj.put("endDate", endDate.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        }
        obj.put("isMigrated", isMigrated);
        obj.put("lastChapter", lastChapter);

        return obj;
    }
}
