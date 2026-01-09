package ru.rerumu.lists.dao.series.impl;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ru.rerumu.lists.dao.series.SeriesItemRelationDTO;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Getter
public class SeriesBookRelationDto implements SeriesItemRelationDTO {

    private Long seriesId;
    private Long bookId;
    private Long userId;
    private Long seriesOrder;

    @Override
    public Long getOrder() {
        return seriesOrder;
    }
}
