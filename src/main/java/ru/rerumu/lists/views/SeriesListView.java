package ru.rerumu.lists.views;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.rerumu.lists.model.Book;
import ru.rerumu.lists.model.Series;
import ru.rerumu.lists.model.SeriesBookRelation;
import ru.rerumu.lists.model.SeriesItem;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class SeriesListView implements ResponseView{
    private final List<Series> seriesList;

    public SeriesListView(List<Series> seriesList, HashMap<Series, List<SeriesBookRelation>> seriesRelations) {
        if (seriesList == null){
            throw new RuntimeException("Series list is not supposed to be NULL");
        }
        this.seriesList = seriesList;
    }

    public void sort() {
        Comparator<Series> comparator = Comparator
                .comparing((Series series) -> {
                    Optional<LocalDateTime> maxDate = series.itemsList().stream()
                            .map(SeriesItem::getUpdateDate)
                            .max(LocalDateTime::compareTo);
                    return maxDate.orElse(LocalDateTime.MIN);
                }).reversed()
                .thenComparing(Series::title)
                .thenComparing(Series::seriesId);

        this.seriesList.sort(comparator);
    }

    // TODO: Rename field
    private int getBookCount(Series series){
        return series.itemsList().size();
    }

    @Override
    public JSONObject toJSONObject() {
        JSONObject obj = new JSONObject();
        JSONArray seriesArray = new JSONArray();
        for (Series item : this.seriesList) {
            JSONObject tmp = item.toJSONObject();
            tmp.put("bookCount",getBookCount(item));
            seriesArray.put(tmp);
        }
        obj.put("items", seriesArray);
        return obj;
    }

    @Override
    public String toString() {

        return this.toJSONObject().toString();
    }

    public static class Builder {
        private List<Series> seriesList;
        private HashMap<Series, List<SeriesBookRelation>> seriesRelations;

        public Builder() {
        }

        public Builder seriesList(List<Series> seriesList) {
            this.seriesList = seriesList;
            return this;
        }

        public Builder seriesRelations(HashMap<Series, List<SeriesBookRelation>> seriesRelations) {
            this.seriesRelations = seriesRelations;
            return this;
        }

        public SeriesListView build() {
            return new SeriesListView(seriesList, seriesRelations);
        }
    }

}
