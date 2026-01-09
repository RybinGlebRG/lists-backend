package ru.rerumu.lists.dao.series.item;

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
