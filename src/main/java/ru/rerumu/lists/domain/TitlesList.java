package ru.rerumu.lists.domain;

import lombok.Getter;
import org.json.JSONArray;
import org.json.JSONObject;
import ru.rerumu.lists.domain.movie.Movie;

import java.util.Comparator;
import java.util.List;

public class TitlesList {

    @Getter
    private final List<Movie> movies;

    public TitlesList(List<Movie> movies){
        this.movies = movies;
    }


    public JSONObject toJSONObject(){
        JSONObject obj = new JSONObject();
        JSONArray titles = new JSONArray();
        for (Movie item: this.movies){
            titles.put(item.toJSONObject());
        }
        obj.put("titles",titles);
        return obj;
    }

    public void sort(){
        Comparator<Movie> comparator = new Comparator<Movie>() {
            @Override
            public int compare(Movie o1, Movie o2) {
                int res = Integer.compare(o1.getyyyy(), o2.getyyyy());
                if (res != 0) {
                    return res;
                }
                res = Integer.compare(o1.getMonth(), o2.getMonth());
                if (res != 0) {
                    return res;
                }

                res = Integer.compare(o1.getdd(), o2.getdd());
                if (res != 0) {
                    return res;
                }

                res = Integer.compare(o1.getHH(), o2.getHH());
                if (res != 0) {
                    return res;
                }

                res = Integer.compare(o1.getmm(), o2.getmm());
                if (res != 0) {
                    return res;
                }

                res = Integer.compare(o1.getss(), o2.getss());
                if (res != 0) {
                    return res;
                }

                res = o1.getName().compareTo(o2.getName());
                if (res != 0) {
                    return res;
                }

                res = Long.compare(o1.getId(), o2.getId());
                return res;
            }
        };

        this.movies.sort(comparator);
    }

    @Override
    public String toString() {
        return this.toJSONObject().toString();
    }
}
