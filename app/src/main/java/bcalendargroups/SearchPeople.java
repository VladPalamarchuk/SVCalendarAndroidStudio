package bcalendargroups;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ListView;

import bcalendargroups.adapters.SearchPeopleAdapter;

import com.example.calendar.MainActivity;
import com.example.calendar.R;

public class SearchPeople {
    Context context;
    SearchPeopleAdapter searchPeopleAdapter;
    String groupID;

    public SearchPeople(Context context, String groupID) {
        this.context = context;
        searchPeopleAdapter = new SearchPeopleAdapter(context,
                R.layout.search_people_item);

        this.groupID = groupID;
    }

    public void searchPeopleDialog() {

        LayoutInflater layoutInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        @SuppressLint("InflateParams")
        final EditText dialogView = (EditText) layoutInflater.inflate(
                R.layout.simple_edit_text, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle(MainActivity.getInstance().language.SEARCH_MEMBERS);
        dialogView.setHint(MainActivity.getInstance().language.SEARCH_PARAMS);

        builder.setView(dialogView);
        builder.setPositiveButton(MainActivity.getInstance().language.SEARCH,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        searchPeople(dialogView.getText().toString());
                    }
                });
        builder.setNegativeButton(MainActivity.getInstance().language.CANCEL,
                null);
        builder.show();
    }

    private void searchPeople(String params) {
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        Map<String, Object> data = new HashMap<String, Object>();

        data.put("search", params);

        jsonMap.put("data", new JSONObject(data));
        jsonMap.put("action", "users.search");

        JSONObject jsonObject = new JSONObject(jsonMap);

        try {
            GroupAssignmentActions.searchUsers(context, jsonObject,
                    onSearchCompleteListener);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private QueryMaster.OnCompleteListener onSearchCompleteListener = new QueryMaster.OnCompleteListener() {
        @Override
        public void complete(String serverResponse) {

            try {
                JSONObject jsonObject = new JSONObject(serverResponse);
                if (QueryMaster.isSuccess(jsonObject)) {
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    searchPeopleResultDialog(jsonArray,
                            onSearchAddedCompleteListener);

                } else {
                    QueryMaster.toast(context,
                            MainActivity.getInstance().language.SEARCH_NULL);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                // server return JSONObject instead JSONArray
                throw new RuntimeException(e);
            }
        }

        @Override
        public void error(int errorCode) {
            QueryMaster.alert(context, "ERROR");
        }
    };

    private QueryMaster.OnCompleteListener onSearchAddedCompleteListener = new QueryMaster.OnCompleteListener() {
        @Override
        public void complete(String serverResponse) {
            if (onCompleteListener != null) {
                onCompleteListener.complete();
            }
        }

        @Override
        public void error(int errorCode) {
            QueryMaster.alert(context, QueryMaster.SERVER_RETURN_INVALID_DATA);
        }
    };

    private void searchPeopleResultDialog(JSONArray jsonArray,
                                          final QueryMaster.OnCompleteListener listener) throws JSONException {
        LayoutInflater layoutInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        @SuppressLint("InflateParams")
        ListView dialogView = (ListView) layoutInflater.inflate(
                R.layout.simple_listview, null);
        dialogView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE_MODAL);
        searchPeopleAdapter.clear();
        dialogView.setAdapter(searchPeopleAdapter);

        searchPeopleAdapter.add(jsonArray);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(dialogView);
        builder.setPositiveButton(
                MainActivity.getInstance().language.ADD_TO_GROUPS,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//						String checkedIds = searchPeopleAdapter
//								.getCheckedIdsAsString();
                        ArrayList<Integer> checkedId = searchPeopleAdapter.getCheckedIds();
                        try {
                            addUsersToGroup(groupID, checkedId, listener);
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                            throw new RuntimeException(e);
                        }
                    }
                });
        builder.setNegativeButton(MainActivity.getInstance().language.CANCEL,
                null);
        builder.show();
    }

    private void addUsersToGroup(String groupId, ArrayList<Integer> checkedId,
                                 QueryMaster.OnCompleteListener listener)

            throws UnsupportedEncodingException {
//		Map<String, Object> jsonMap = new HashMap<String, Object>();
//		Map<String, String> data = new HashMap<String, String>();
//		data.put("id_group", groupId);
//		data.put("id_users", checkedId);
//
//		jsonMap.put("action", "users.add-user-group");
//		jsonMap.put("data", new JSONObject(data));
//
//		JSONObject jsonObject = new JSONObject(jsonMap);

        GroupAssignmentActions.addUserToGroup(context, groupId, checkedId, listener);
    }

    private OnCompleteListener onCompleteListener;

    public void setOnCompleteListener(OnCompleteListener onCompleteListener) {
        this.onCompleteListener = onCompleteListener;
    }

    public interface OnCompleteListener {
        public void complete();
    }
}