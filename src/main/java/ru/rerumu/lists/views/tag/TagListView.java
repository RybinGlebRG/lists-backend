package ru.rerumu.lists.views.tag;

import org.json.JSONArray;
import org.json.JSONObject;
import ru.rerumu.lists.model.tag.Tag;

import java.util.List;
import java.util.stream.Collectors;

public class TagListView {

    private final List<Tag> tagList;

    public TagListView(List<Tag> tagList) {
        this.tagList = tagList;
    }

    public JSONObject toJSONObject() {
        JSONObject obj = new JSONObject();
        JSONArray bookArray = tagList.stream()
                .map(Tag::toJSONObject)
                .collect(JSONArray::new, JSONArray::put, JSONArray::putAll);
        obj.put("items", bookArray);

        return obj;
    }

    @Override
    public String toString() {
        return toJSONObject().toString();
    }
}
