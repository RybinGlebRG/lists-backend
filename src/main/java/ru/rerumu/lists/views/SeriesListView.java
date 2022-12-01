package ru.rerumu.lists.views;

import org.json.JSONArray;
import org.json.JSONObject;
import ru.rerumu.lists.model.Series;
import ru.rerumu.lists.model.Title;

import java.util.Comparator;
import java.util.List;

public class SeriesListView {
    private final List<Series> seriesList;
    public SeriesListView(List<Series> seriesList){
        this.seriesList = seriesList;
    }

    public void sort(){
        Comparator<Series> comparator = Comparator
                .comparing(Series::getLastUpdateDate).reversed()
                .thenComparing(Series::getTitle)
                .thenComparing(Series::getSeriesId);

        this.seriesList.sort(comparator);
    }

    public JSONObject toJSONObject(){
        JSONObject obj = new JSONObject();
        JSONArray seriesArray = new JSONArray();
        for (Series item: this.seriesList){
            seriesArray.put(item.toJSONObject());
        }
        obj.put("items",seriesArray);
        return obj;
    }

    @Override
    public String toString() {

        return this.toJSONObject().toString();
    }
}
