package ru.rerumu.lists.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.rerumu.lists.exception.EmptyMandatoryParameterException;
import ru.rerumu.lists.services.book.ReadListService;
import ru.rerumu.lists.services.book.readingrecord.ReadingRecordService;
import ru.rerumu.lists.views.ReadingRecordAddView;
import ru.rerumu.lists.views.ReadingRecordListView;
import ru.rerumu.lists.views.ReadingRecordUpdateView;

@CrossOrigin
@RestController
public class ReadingRecordsController {

    private final ReadListService readListService;
    private final ReadingRecordService readingRecordService;

    @Autowired
    public ReadingRecordsController(ReadListService readListService, ReadingRecordService readingRecordService) {
        this.readListService = readListService;
        this.readingRecordService = readingRecordService;
    }

    @PostMapping(value = "/api/v0.2/books/{bookId}/readingRecords",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<String> addOne(
            @PathVariable Long bookId,
            @RequestBody ReadingRecordAddView readingRecordAddView
    ) throws EmptyMandatoryParameterException {
        readListService.addReadingRecord(bookId, readingRecordAddView);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping(value = "/api/v0.2/books/{bookId}/readingRecords/{readingRecordId}",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<String> updateOne(
            @PathVariable Long bookId,
            @PathVariable Long readingRecordId,
            @RequestBody ReadingRecordUpdateView readingRecordUpdateView
    ) throws EmptyMandatoryParameterException {
        readListService.updateReadingRecord(bookId, readingRecordId, readingRecordUpdateView);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(value = "/api/v0.2/books/{bookId}/readingRecords/{readingRecordId}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<String> deleteOne(
            @PathVariable Long bookId,
            @PathVariable Long readingRecordId
    ) throws EmptyMandatoryParameterException {
        readListService.deleteReadingRecord(bookId, readingRecordId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping(
            value = "/api/v0.2/books/{bookId}/readingRecords",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<String> searchBooks(@PathVariable Long bookId) {
        ReadingRecordListView readingRecordListView = new ReadingRecordListView(readingRecordService.getReadingRecords(bookId));
        return new ResponseEntity<>(readingRecordListView.toString(), HttpStatus.OK);
    }
}
