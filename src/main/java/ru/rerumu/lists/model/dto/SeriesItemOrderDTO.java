package ru.rerumu.lists.model.dto;

import ru.rerumu.lists.model.series.item.SeriesItemDTO;

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
