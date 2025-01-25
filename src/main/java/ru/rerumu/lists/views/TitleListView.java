package ru.rerumu.lists.views;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import ru.rerumu.lists.model.title.Title;
import ru.rerumu.lists.model.TitlesList;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public final class TitleListView implements ResponseView{

    private final List<Title> titles;

    public TitleListView(List<Title> titles) {
        this.titles = titles;
    }

    private List<Title> sort(List<Title> titles){
        Comparator<Title> comparator = Comparator.comparing(Title::getCreateDateLocal).reversed()
                .thenComparing(Title::getName)
                .thenComparingLong(Title::getTitleId);

        return titles.stream().sorted(comparator).collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public JSONObject toJSONObject() {
        List<Title> sortedTitles = sort(titles);

        JSONObject obj = new JSONObject();
        JSONArray array = new JSONArray();
        for (Title item : sortedTitles) {
            JSONObject tmp = item.toJSONObject();
            array.put(tmp);
        }
        obj.put("titles", array);
        return obj;
    }

    @Override
    public String toString() {
        return this.toJSONObject().toString();
    }

    public static Builder builder(){
        return new Builder();
    }

    @Slf4j
    public static class Builder{
        private TitlesList titlesList;

        public Builder titlesList(TitlesList titlesList){
            this.titlesList = titlesList;
            return this;
        }

        public TitleListView build(){
            TitleListView titleListView = new TitleListView(titlesList.getTitles());
            log.debug("titleListView: {}", titleListView);
            return titleListView;
        }
    }
}
