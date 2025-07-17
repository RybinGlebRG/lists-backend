package ru.rerumu.lists.domain.dto;

import ru.rerumu.lists.domain.series.item.SeriesItemDTO;

public class SeriesItemOrderDTO {

    public SeriesItemDTO itemDTO;
    public Long order;

    public Long getOrder() {
        return order;
    }

    public SeriesItemDTO getItemDTO() {
        return itemDTO;
    }
}
