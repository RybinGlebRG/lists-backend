package ru.rerumu.lists.repository.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ru.rerumu.lists.mappers.ReadingRecordMapper;
import ru.rerumu.lists.mappers.SeriesMapper;
import ru.rerumu.lists.model.books.reading_records.ReadingRecord;
import ru.rerumu.lists.model.book.BookDTO;
import ru.rerumu.lists.model.series.SeriesDTO;
import ru.rerumu.lists.model.series.SeriesFactory;
import ru.rerumu.lists.repository.SeriesRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class SeriesRepositoryImpl extends CrudRepositoryDtoImpl<SeriesDTO,Long> implements SeriesRepository{

    private final SeriesMapper seriesMapper;
    private final ReadingRecordMapper readingRecordMapper;
    private final SeriesFactory seriesFactory;

    @Autowired
    public SeriesRepositoryImpl(
            SeriesMapper seriesMapper, ReadingRecordMapper readingRecordMapper, SeriesFactory seriesFactory) {
        super(seriesMapper);
        this.seriesMapper = seriesMapper;
        this.readingRecordMapper = readingRecordMapper;
        this.seriesFactory = seriesFactory;
    }

    @Deprecated
    @Override
    public SeriesDTO getOne(Long readListId, Long seriesId) {
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



        return seriesDTO;
    }

    @Override
    public List<SeriesDTO> getAll(Long seriesListId) {

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

            List<SeriesDTO> resList = new ArrayList<>(res);
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
    public void add(SeriesDTO series) {
        seriesMapper.add(
                series.seriesListId,
                series.seriesId,
                series.title
        );
    }

    @Override
    public void delete(long seriesId) {
        seriesMapper.delete(seriesId);
    }
}
