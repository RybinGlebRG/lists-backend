package ru.rerumu.lists.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.rerumu.lists.exception.EntityNotFoundException;
import ru.rerumu.lists.exception.UserIsNotOwnerException;
import ru.rerumu.lists.model.Book;
import ru.rerumu.lists.model.books.Search;
import ru.rerumu.lists.model.books.reading_records.ReadingRecord;
import ru.rerumu.lists.services.ReadingRecordService;
import ru.rerumu.lists.views.BookListView;
import ru.rerumu.lists.views.ReadingRecordAddView;
import ru.rerumu.lists.views.ReadingRecordListView;
import ru.rerumu.lists.views.ReadingRecordUpdateView;

import java.util.List;

@CrossOrigin
@RestController
public class ReadingRecordsController {

    private final ReadingRecordService readingRecordService;

    public ReadingRecordsController(ReadingRecordService readingRecordService) {
        this.readingRecordService = readingRecordService;
    }

    @PostMapping(value = "/api/v0.2/books/{bookId}/readingRecords",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<String> addOne(
            @PathVariable Long bookId,
            @RequestBody ReadingRecordAddView readingRecordAddView
    ) {
        readingRecordService.addRecord(bookId, readingRecordAddView);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping(value = "/api/v0.2/readingRecords/{readingRecordId}",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<String> addOne(
            @PathVariable Long readingRecordId,
            @RequestBody ReadingRecordUpdateView readingRecordUpdateView
    ) {
        readingRecordService.updateRecord(readingRecordId, readingRecordUpdateView);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(value = "/api/v0.2/readingRecords/{readingRecordId}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<String> deleteOne(
            @PathVariable Long readingRecordId
    ){
        readingRecordService.deleteRecord(readingRecordId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping(
            value = "/api/v0.2/books/{bookId}/readingRecords",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<String> searchBooks(@PathVariable Long bookId) {
        List<ReadingRecord> readingRecords = readingRecordService.getReadingRecords(bookId);
        ReadingRecordListView readingRecordListView = new ReadingRecordListView(readingRecords);
        return new ResponseEntity<>(readingRecordListView.toString(), HttpStatus.OK);
    }
}
