package ru.rerumu.lists.views;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.rerumu.lists.model.Book;
import ru.rerumu.lists.model.Series;
import ru.rerumu.lists.model.SeriesBookRelation;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class SeriesListView {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final List<Series> seriesList;

//    private final HashMap<Series, List<SeriesBookRelation>> seriesRelations;

    public SeriesListView(List<Series> seriesList, HashMap<Series, List<SeriesBookRelation>> seriesRelations) {
        if (seriesList == null){
            throw new RuntimeException("Series list is not supposed to be NULL");
        }
        this.seriesList = seriesList;
//        if (seriesRelations == null){
//            throw new RuntimeException("Relations are not supposed to be NULL");
//        }
//        this.seriesRelations = seriesRelations;
    }

//    public void sort() {
//        Comparator<Series> comparator = Comparator
//                .comparing((Series series) -> {
//                    Optional<LocalDateTime> maxDate = seriesRelations.get(series).stream()
//                            .map(seriesBookRelation -> seriesBookRelation.book().getLastUpdateDate_V2())
//                            .max(LocalDateTime::compareTo);
//                    return maxDate.orElse(LocalDateTime.MIN);
//                })
//                .reversed()
//                .thenComparing(Series::getTitle)
//                .thenComparing(Series::getSeriesId);
//
//        this.seriesList.sort(comparator);
//    }

    public void sort() {
        Comparator<Series> comparator = Comparator
                .comparing((Series series) -> {
                    Optional<LocalDateTime> maxDate = series.itemsList().stream()
                            .map(item->{
                                if (item instanceof Book book){
                                    return book.getLastUpdateDate_V2();
                                } else {
                                    throw new IllegalArgumentException();
                                }
                            })
                            .max(LocalDateTime::compareTo);
                    return maxDate.orElse(LocalDateTime.MIN);
                })
                .reversed()
                .thenComparing(Series::title)
                .thenComparing(Series::seriesId);

        this.seriesList.sort(comparator);
    }

    /* TODO: Test
    1. no book
 */
    private int getBookCount(Series series){
        return series.itemsList().size();
    }

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
