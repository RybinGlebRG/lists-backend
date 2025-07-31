package ru.rerumu.lists.domain.series;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

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
