package bcalendargroups.pager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class SingleEvent implements Serializable {
    public String id;
    public String title;

    private SingleEvent(JSONObject jsonObject) throws JSONException {
        this.id = jsonObject.getString("id");
        this.title = jsonObject.getString("title");
    }

    public static ArrayList<SingleEvent> get(JSONArray jsonArray) throws JSONException {
        final ArrayList<SingleEvent> list = new ArrayList<SingleEvent>();
        for (int i = 0; i < jsonArray.length(); i++) {
            list.add(new SingleEvent(jsonArray.getJSONObject(i)));
        }
        return list;
    }
}
