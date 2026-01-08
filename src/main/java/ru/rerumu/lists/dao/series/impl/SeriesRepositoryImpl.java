package ru.rerumu.lists.dao.series.impl;

import com.jcabi.aspects.Loggable;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.rerumu.lists.dao.base.impl.CrudRepositoryDtoImpl;
import ru.rerumu.lists.dao.readingrecord.ReadingRecordsRepository;
import ru.rerumu.lists.dao.series.SeriesRepository;
import ru.rerumu.lists.dao.series.mapper.SeriesBookMapper;
import ru.rerumu.lists.dao.series.mapper.SeriesMapper;
import ru.rerumu.lists.domain.book.BookDTO;
import ru.rerumu.lists.domain.readingrecords.ReadingRecord;
import ru.rerumu.lists.domain.readingrecords.ReadingRecordDTO;
import ru.rerumu.lists.domain.series.SeriesBookRelationDto;
import ru.rerumu.lists.domain.series.SeriesDTO;
import ru.rerumu.lists.domain.series.SeriesDTOv2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Slf4j
public class SeriesRepositoryImpl extends CrudRepositoryDtoImpl<SeriesDTOv2,Long> implements SeriesRepository{

    private final SeriesMapper seriesMapper;
    private final SeriesBookMapper seriesBookMapper;
    private final ReadingRecordsRepository readingRecordsRepository;

    @Autowired
    public SeriesRepositoryImpl(
            SeriesMapper seriesMapper,
            SeriesBookMapper seriesBookMapper, ReadingRecordsRepository readingRecordsRepository
    ) {
        super(seriesMapper);
        this.seriesMapper = seriesMapper;
        this.seriesBookMapper = seriesBookMapper;
        this.readingRecordsRepository = readingRecordsRepository;
    }

    @Deprecated
    @Override
    public SeriesDTO getOne(Long readListId, Long seriesId) {
        SeriesDTO seriesDTO = seriesMapper.getOne(readListId, seriesId);

        List<Long> bookIds = seriesDTO.seriesItemOrderDTOList.stream()
                .filter(seriesItemOrderDTO -> seriesItemOrderDTO.itemDTO instanceof BookDTO)
                .map(seriesItemOrderDTO -> ((BookDTO)seriesItemOrderDTO.itemDTO).getBookId())
                .collect(Collectors.toCollection(ArrayList::new));

        List<ReadingRecord> readingRecords = readingRecordsRepository.findByBookIds(bookIds);

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
                            .map(ReadingRecordDTO::fromDomain)
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

            List<ReadingRecord> readingRecords = readingRecordsRepository.findByBookIds(bookIds);

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
                                .map(ReadingRecordDTO::fromDomain)
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

    @Override
    public List<SeriesDTOv2> findByBook(@NonNull Long bookId, @NonNull Long userId) {
        return seriesMapper.findByBook(bookId, userId);
    }

    @Override
    public List<SeriesDTOv2> findByIds(@NonNull List<Long> seriesIds, @NonNull Long userId) {
        return seriesMapper.findByIds(seriesIds, userId);
    }

    @Override
    public void update(SeriesDTOv2 entity) {
        seriesMapper.update(entity);

        for (SeriesBookRelationDto seriesBookRelationDto: entity.getSeriesBookRelationDtoList()) {
            seriesBookMapper.merge(seriesBookRelationDto);
        }
    }

    @Override
    @Loggable(value = Loggable.TRACE, prepend = true, trim = false)
    public void update(SeriesDTOv2 originalEntity, SeriesDTOv2 currentEntity) {
        seriesMapper.update(currentEntity);

        List<SeriesBookRelationDto> relationsToRemove = originalEntity.getSeriesBookRelationDtoList().stream()
                .filter( item -> !currentEntity.getSeriesBookRelationDtoList().contains(item))
                .collect(Collectors.toCollection(ArrayList::new));
        log.trace("relationsToRemove: {}", relationsToRemove);

        // Remove deleted relations
        for (SeriesBookRelationDto seriesBookRelationDto: relationsToRemove) {
            seriesBookMapper.delete(seriesBookRelationDto);
        }

        // Add or update relations
        for (SeriesBookRelationDto seriesBookRelationDto: currentEntity.getSeriesBookRelationDtoList()) {
            seriesBookMapper.merge(seriesBookRelationDto);
        }

    }
}
