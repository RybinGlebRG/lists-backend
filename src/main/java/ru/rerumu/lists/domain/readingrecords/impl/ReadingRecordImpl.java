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
import ru.rerumu.lists.dao.readingrecord.ReadingRecordsRepository;
import ru.rerumu.lists.domain.RecordStatusEnum;
import ru.rerumu.lists.domain.bookstatus.BookStatusRecord;
import ru.rerumu.lists.domain.readingrecords.ReadingRecord;
import ru.rerumu.lists.domain.readingrecords.ReadingRecordDTO;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Slf4j
@ToString(callSuper = true, doNotUseGetters = true)
public class ReadingRecordImpl implements ReadingRecord {

    @Getter
    private final Long recordId;

    private final Long bookId;

    private BookStatusRecord bookStatus;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime startDate;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime endDate;

    private final Boolean isMigrated;

    private Long lastChapter;

    /**
     * Update date of the record
     */
    @Getter
    private LocalDateTime updateDate;

    @ToString.Exclude
    private final ReadingRecordsRepository readingRecordsRepository;
    @ToString.Exclude
    private final DateFactory dateFactory;


    ReadingRecordImpl(
            Long recordId,
            Long bookId,
            BookStatusRecord bookStatus,
            LocalDateTime startDate,
            LocalDateTime endDate,
            Boolean isMigrated,
            Long lastChapter,
            ReadingRecordsRepository readingRecordsRepository,
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
        this.readingRecordsRepository = readingRecordsRepository;
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
    public void save(){
        readingRecordsRepository.update(this.toDTO());
    }

    @Override
    public void delete() {
        readingRecordsRepository.delete(recordId);
    }

    @Override
    public void update(
            @NonNull BookStatusRecord bookStatusRecord,
            @NonNull LocalDateTime startDate,
            LocalDateTime endDate,
            Long lastChapter
    ) {

        // Update end date
        if (endDate != null) {
            this.endDate = endDate;
        }
        // or close record if status changed to "Completed"
        else if (bookStatusRecord.statusId().equals(RecordStatusEnum.COMPLETED.getId().intValue())) {
            this.endDate = dateFactory.getLocalDateTime();
        }

        this.bookStatus = bookStatusRecord;
        this.startDate = startDate;
        this.lastChapter = lastChapter;
        this.updateDate = dateFactory.getLocalDateTime();
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
                readingRecordsRepository,
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
}
