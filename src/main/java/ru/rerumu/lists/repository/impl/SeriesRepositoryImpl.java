package ru.rerumu.lists.repository.impl;

import org.springframework.stereotype.Component;

import ru.rerumu.lists.mappers.ReadingRecordMapper;
import ru.rerumu.lists.mappers.SeriesMapper;
import ru.rerumu.lists.model.*;
import ru.rerumu.lists.model.books.reading_records.ReadingRecord;
import ru.rerumu.lists.model.dto.BookDTO;
import ru.rerumu.lists.model.dto.BookOrderedDTO;
import ru.rerumu.lists.model.dto.SeriesDTO;
import ru.rerumu.lists.repository.SeriesRepository;
import ru.rerumu.lists.services.MonitoringService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class SeriesRepositoryImpl extends CrudRepositoryDtoImpl<Series,Long> implements SeriesRepository{

    private final SeriesMapper seriesMapper;
    private final ReadingRecordMapper readingRecordMapper;

    public SeriesRepositoryImpl(
            SeriesMapper seriesMapper, ReadingRecordMapper readingRecordMapper) {
        super(seriesMapper);
        this.seriesMapper = seriesMapper;
        this.readingRecordMapper = readingRecordMapper;
    }

    @Deprecated
    @Override
    public Series getOne(Long readListId, Long seriesId) {
        SeriesDTO seriesDTO = seriesMapper.getOne(readListId, seriesId);

        List<Long> bookIds = seriesDTO.seriesItemOrderDTOList.stream()
                .filter(seriesItemOrderDTO -> seriesItemOrderDTO.itemDTO instanceof BookDTO)
                .map(seriesItemOrderDTO -> ((BookDTO)seriesItemOrderDTO.itemDTO).getBookId())
                .collect(Collectors.toCollection(ArrayList::new));

        List<ReadingRecord> readingRecords = readingRecordMapper.findByBookIds(bookIds);

        Map<Long, List<ReadingRecord>> bookId2ReadingRecordMap = readingRecords.stream()
                .collect(Collectors.groupingBy(
                        ReadingRecord::bookId,
                        HashMap::new,
                        Collectors.toCollection(ArrayList::new)
                ));

        List<BookDTO> bookDTOList = seriesDTO.seriesItemOrderDTOList.stream()
                .filter(seriesItemOrderDTO -> seriesItemOrderDTO.itemDTO instanceof BookDTO)
                .map(seriesItemOrderDTO -> (BookDTO)seriesItemOrderDTO.itemDTO)
                .collect(Collectors.toCollection(ArrayList::new));

        for(BookDTO bookDTO: bookDTOList){
            List<ReadingRecord> records = bookId2ReadingRecordMap.get(bookDTO.getBookId());

            if (records == null){
                records = new ArrayList<>();
            }

            bookDTO.setReadingRecords(records);
        }

        return seriesDTO.toSeries();
    }

    @Override
    public List<Series> getAll(Long seriesListId) {

        try {
            List<SeriesDTO> res = seriesMapper.getAll(seriesListId);

            List<Long> bookIds = res.stream()
                    .flatMap(seriesDTO -> seriesDTO.seriesItemOrderDTOList.stream())
                    .filter(seriesItemOrderDTO -> seriesItemOrderDTO.itemDTO instanceof BookDTO)
                    .map(seriesItemOrderDTO -> ((BookDTO)seriesItemOrderDTO.itemDTO).getBookId())
                    .collect(Collectors.toCollection(ArrayList::new));

            List<ReadingRecord> readingRecords = readingRecordMapper.findByBookIds(bookIds);

            Map<Long, List<ReadingRecord>> bookId2ReadingRecordMap = readingRecords.stream()
                    .collect(Collectors.groupingBy(
                            ReadingRecord::bookId,
                            HashMap::new,
                            Collectors.toCollection(ArrayList::new)
                    ));

            List<BookDTO> bookDTOList = res.stream()
                    .flatMap(seriesDTO -> seriesDTO.seriesItemOrderDTOList.stream())
                    .filter(seriesItemOrderDTO -> seriesItemOrderDTO.itemDTO instanceof BookDTO)
                    .map(seriesItemOrderDTO -> (BookDTO)seriesItemOrderDTO.itemDTO)
                    .collect(Collectors.toCollection(ArrayList::new));

            for(BookDTO bookDTO: bookDTOList){
                List<ReadingRecord> records = bookId2ReadingRecordMap.get(bookDTO.getBookId());

                if (records == null){
                    records = new ArrayList<>();
                }

                bookDTO.setReadingRecords(records);
            }

            List<Series> resList = res.stream()
                    .map(SeriesDTO::toSeries)
                    .collect(Collectors.toCollection(ArrayList::new));
            return resList;
        } catch (Exception e){
            throw new RuntimeException(e);
        }

    }

    @Override
    public int getBookCount(Long readListId, Long seriesId) {
        return seriesMapper.getBookCount(readListId, seriesId);
    }

    @Override
    public Long getNextId() {
        return seriesMapper.nextval();
    }

    @Override
    public void add(Series series) {
        seriesMapper.add(
                series.seriesListId(),
                series.seriesId(),
                series.title()
        );
    }

    @Override
    public void delete(long seriesId) {
        seriesMapper.delete(seriesId);
    }
}
