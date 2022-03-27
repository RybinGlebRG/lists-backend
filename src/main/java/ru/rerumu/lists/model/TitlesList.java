package ru.rerumu.lists.model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Comparator;
import java.util.List;

public class TitlesList {

    private final List<Title> titles;

    public TitlesList(List<Title> titles){
        this.titles=titles;
    }


    public JSONObject toJSONObject(){
        JSONObject obj = new JSONObject();
        JSONArray titles = new JSONArray();
        for (Title item: this.titles){
            titles.put(item.toJSONObject());
        }
        obj.put("titles",titles);
        return obj;
    }

    public void sort(){
        Comparator<Title> comparator = new Comparator<Title>() {
            @Override
            public int compare(Title o1, Title o2) {
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

                res = Long.compare(o1.getTitleId(), o2.getTitleId());
                return res;
            }
        };

        this.titles.sort(comparator);
    }

    @Override
    public String toString() {
        return this.toJSONObject().toString();
    }
}
