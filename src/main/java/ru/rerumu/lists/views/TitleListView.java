package ru.rerumu.lists.views;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import ru.rerumu.lists.domain.movie.Movie;
import ru.rerumu.lists.domain.TitlesList;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public final class TitleListView implements ResponseView{

    private final List<Movie> movies;

    public TitleListView(List<Movie> movies) {
        this.movies = movies;
    }

    private List<Movie> sort(List<Movie> movies){
        Comparator<Movie> comparator = Comparator.comparing(Movie::getCreateDateLocal).reversed()
                .thenComparing(Movie::getName)
                .thenComparingLong(Movie::getTitleId);

        return movies.stream().sorted(comparator).collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public JSONObject toJSONObject() {
        List<Movie> sortedMovies = sort(movies);

        JSONObject obj = new JSONObject();
        JSONArray array = new JSONArray();
        for (Movie item : sortedMovies) {
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
            TitleListView titleListView = new TitleListView(titlesList.getMovies());
            log.debug("titleListView: {}", titleListView);
            return titleListView;
        }
    }
}
