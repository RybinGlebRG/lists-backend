package ru.rerumu.lists.dao.series.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ru.rerumu.lists.dao.base.impl.CrudRepositoryDtoImpl;
import ru.rerumu.lists.dao.book.readingrecord.mapper.ReadingRecordMapper;
import ru.rerumu.lists.dao.series.mapper.SeriesMapper;
import ru.rerumu.lists.model.book.readingrecords.ReadingRecord;
import ru.rerumu.lists.model.book.readingrecords.impl.ReadingRecordFactory;
import ru.rerumu.lists.model.book.BookDTO;
import ru.rerumu.lists.model.series.SeriesDTO;
import ru.rerumu.lists.model.series.SeriesFactory;
import ru.rerumu.lists.dao.series.SeriesRepository;

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
    private final ReadingRecordFactory readingRecordFactory;

    @Autowired
    public SeriesRepositoryImpl(
            SeriesMapper seriesMapper, ReadingRecordMapper readingRecordMapper, SeriesFactory seriesFactory, ReadingRecordFactory readingRecordFactory) {
        super(seriesMapper);
        this.seriesMapper = seriesMapper;
        this.readingRecordMapper = readingRecordMapper;
        this.seriesFactory = seriesFactory;
        this.readingRecordFactory = readingRecordFactory;
    }

    @Deprecated
    @Override
    public SeriesDTO getOne(Long readListId, Long seriesId) {
        SeriesDTO seriesDTO = seriesMapper.getOne(readListId, seriesId);

        List<Long> bookIds = seriesDTO.seriesItemOrderDTOList.stream()
                .filter(seriesItemOrderDTO -> seriesItemOrderDTO.itemDTO instanceof BookDTO)
                .map(seriesItemOrderDTO -> ((BookDTO)seriesItemOrderDTO.itemDTO).getBookId())
                .collect(Collectors.toCollection(ArrayList::new));

        List<ReadingRecord> readingRecords = readingRecordFactory.findByBookIds(bookIds);

        Map<Long, List<ReadingRecord>> bookId2ReadingRecordMap = readingRecords.stream()
                .collect(Collectors.groupingBy(
                        ReadingRecord::getBookId,
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

            bookDTO.setReadingRecords(
                    records.stream()
                            .map(ReadingRecord::toDTO)
                            .collect(Collectors.toCollection(ArrayList::new))
            );
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

            List<ReadingRecord> readingRecords = readingRecordMapper.findByBookIds(bookIds).stream()
                    .map(readingRecordFactory::fromDTO)
                    .collect(Collectors.toCollection(ArrayList::new));

            Map<Long, List<ReadingRecord>> bookId2ReadingRecordMap = readingRecords.stream()
                    .collect(Collectors.groupingBy(
                            ReadingRecord::getBookId,
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

                bookDTO.setReadingRecords(
                        records.stream()
                                .map(ReadingRecord::toDTO)
                                .collect(Collectors.toCollection(ArrayList::new))
                );
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
