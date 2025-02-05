//package ru.rerumu.lists.model;
//
//import lombok.extern.slf4j.Slf4j;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.TestInfo;
//import ru.rerumu.lists.model.book.BookImpl;
//import ru.rerumu.lists.model.books.reading_records.ReadingRecord;
//
//import java.time.LocalDateTime;
//
//@Slf4j
//public class BookTest {
//
//    @Test
//    public void shouldAddRecord(TestInfo testInfo) throws Exception{
//        log.info("Test: {}", testInfo.getDisplayName());
//
//        /*
//        Given
//         */
//        BookImpl book = new BookImpl.Builder()
//                .build();
//        Long anyReadingRecordId = 1L;
//        BookStatusRecord anyBookStatusRecord = new BookStatusRecord(2, "test");
//        LocalDateTime anyStartDate = LocalDateTime.now();
//        LocalDateTime anyEndDate = LocalDateTime.now();
//
//        /*
//        When
//         */
//        ReadingRecord readingRecord = book.addReadingRecord(null, anyReadingRecordId, anyBookStatusRecord, anyStartDate, anyEndDate);
//
//
//        /*
//        Then
//         */
//        ReadingRecord expectedRecord = ReadingRecord.builder()
//                .recordId(anyReadingRecordId)
//                .bookStatus(anyBookStatusRecord)
//                .startDate(anyStartDate)
//                .endDate(anyEndDate)
//                .build();
//
//        Assertions.assertEquals(expectedRecord, readingRecord, "Added record should be equal to expected");
//
//    }
//}
