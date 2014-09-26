package bcalendargroups;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import bcalendargroups.QueryMaster.OnCompleteListener;

import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public final class GroupAssignmentActions {

    /**
     * create new group and send response to listener
     *
     * @param context
     * @param listener
     * @param groupName
     * @param adminId
     * @throws java.io.UnsupportedEncodingException
     */
    public static void createGroup(final Context context,
                                   OnCompleteListener listener, String groupName,
                                   String adminId) throws UnsupportedEncodingException {
        // create group:
        // data={"action":"users.create-group","data":{"title":"test","admin":"2"}}

        Map<String, Object> jsonMap = new HashMap<String, Object>();
        Map<String, Object> data = new HashMap<String, Object>();

        data.put("title", groupName);
        data.put("admin", adminId);

        jsonMap.put("action", "users.g-Group-create");
        jsonMap.put("data", new JSONObject(data));

        JSONObject jsonObject = new JSONObject(jsonMap);

        createGroup(context, jsonObject, listener);
    }

    public static void confirmMyTask(Context context,
                                     OnCompleteListener listener, String id_user,
                                     String status, String id_event, String noteID, String groupID)
            throws UnsupportedEncodingException {
        String url = ServerRequest.DOMAIN;
        // data={"action":"users.g-Group-status-confirmation","data":{"id_event":"1","id_user":"3","status":"1"}}

        Map<String, Object> jsonMap = new HashMap<String, Object>();
        Map<String, Object> data = new HashMap<String, Object>();

        data.put("id_user", id_user);
        data.put("status", status);
        data.put("id_event", id_event);
        data.put("id_note", noteID);
        data.put("id_group", groupID);


        jsonMap.put("action", "users.g-Group-note-status-confirmation");
        jsonMap.put("data", new JSONObject(data));

        JSONObject jsonObject = new JSONObject(jsonMap);

        query(context, jsonObject, url, listener);
    }

    /**
     * @param context    just context
     * @param jsonObject in format
     *                   {"action":"users.create-group","data":{"title":"test"
     *                   ,"admin":"2"}}
     * @param listener   QueryMaster.OnCompleteListener
     * @throws java.io.UnsupportedEncodingException
     */
    private static void createGroup(final Context context,
                                    JSONObject jsonObject, OnCompleteListener listener)
            throws UnsupportedEncodingException {

        // url and post/get query type
        String url = ServerRequest.USER_CREATE_GROUP;

        query(context, jsonObject, url, listener);
    }

    public static void removeUserNote(final Context context, String noteID, String userID,
                                      final OnCompleteListener listener) throws UnsupportedEncodingException {

        Map<String, Object> jsonMap = new HashMap<String, Object>();
        Map<String, Object> data = new HashMap<String, Object>();

        data.put("id_note", noteID);
        data.put("id_user", userID);

        jsonMap.put("action", "users.g-Group-delete-note");
        jsonMap.put("data", new JSONObject(data));

        JSONObject jsonObject = new JSONObject(jsonMap);

        query(context, jsonObject, ServerRequest.DOMAIN, listener);
    }

    public static void updateUserNote(final Context context, String noteID, String noteBody,
                                      final OnCompleteListener listener) throws UnsupportedEncodingException {

        Map<String, Object> jsonMap = new HashMap<String, Object>();
        Map<String, Object> data = new HashMap<String, Object>();

        data.put("id_note", noteID);
        data.put("note", noteBody);

        jsonMap.put("action", "users.g-Group-update-note");
        jsonMap.put("data", new JSONObject(data));

        JSONObject jsonObject = new JSONObject(jsonMap);

        query(context, jsonObject, ServerRequest.DOMAIN, listener);
    }

    public static void updateGroupTitle(final Context context, String groupTitle, String groupID,
                                        final OnCompleteListener listener) throws UnsupportedEncodingException {
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        Map<String, Object> data = new HashMap<String, Object>();

        data.put("id_group", groupID);
        data.put("title", groupTitle);

        jsonMap.put("action", "users.g-Group-update-title-group");
        jsonMap.put("data", new JSONObject(data));

        JSONObject jsonObject = new JSONObject(jsonMap);

        query(context, jsonObject, ServerRequest.DOMAIN, listener);

//        data={"action":"users.g-Group-update-title-group","data":{"id_group":"1","title":"sdhkjasdg"}}
    }

    public static void remindUserNote(final Context context, String userID, String groupID, String noteID,
                                      final OnCompleteListener listener) throws UnsupportedEncodingException {


        Map<String, Object> jsonMap = new HashMap<String, Object>();
        Map<String, Object> data = new HashMap<String, Object>();

        data.put("id_group", groupID);
        data.put("id_user", userID);
        data.put("id_note", noteID);

        jsonMap.put("action", "users.g-Group-reminder-user");

        jsonMap.put("data", new JSONObject(data));

        JSONObject jsonObject = new JSONObject(jsonMap);

        query(context, jsonObject, ServerRequest.DOMAIN, listener);

        //data={"action":"users.g-Group-reminder-user","data":{"id_group":"1","id_user":"7","id_note":"1"}}
    }


    public static void addUserToGroup(final Context context,
                                      final String groupId, final ArrayList<Integer> usersId,
                                      final OnCompleteListener listener)
            throws UnsupportedEncodingException {
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        Map<String, Object> data = new HashMap<String, Object>();

        jsonMap.put("action", "users.g-Group-add-people-in-group");

        final JSONArray friends = new JSONArray();
        for (Integer i : usersId) {
            final Map<String, Object> friend = new HashMap<String, Object>();
            friend.put("id", i);
            friend.put("id_group", groupId);

            final JSONObject friendJSON = new JSONObject(friend);
            friends.put(friendJSON);
        }
        Map<String, Object> id_users = new HashMap<String, Object>();
        id_users.put("friends", friends);

        data.put("id_users", new JSONObject(id_users));

        jsonMap.put("data", new JSONObject(data));

        JSONObject jsonObject = new JSONObject(jsonMap);

        query(context, jsonObject, ServerRequest.DOMAIN, listener);

        /**
         * data={"action":"users.g-add-people-in-group",
         * "data":{"id_users":{"friends":[{"id": "2","id_group": "2"},{"id":
         * "18","id_group": "2"}]}}}
         */
    }

    public static void searchUsers(final Context context,
                                   JSONObject jsonObject, OnCompleteListener listener)
            throws UnsupportedEncodingException {

        String url = ServerRequest.SEARCH_USERS;

        query(context, jsonObject, url, listener);
    }


    public static void getMyGroups(Context context, String adminId,
                                   OnCompleteListener listener)
            throws UnsupportedEncodingException, JSONException {
        Map<String, Object> json = new HashMap<String, Object>();
        Map<String, Object> data = new HashMap<String, Object>();

        data.put("id", adminId);

        json.put("action", "users.g-Group-my-groups-with-me");
        json.put("data", new JSONObject(data));

        JSONObject jsonObject = new JSONObject(json);

        getMyGroups(context, jsonObject, listener);

    }

    private static void getMyGroups(Context context, JSONObject jsonObject,
                                    OnCompleteListener listener)
            throws UnsupportedEncodingException {
        String url = ServerRequest.DOMAIN;

        query(context, jsonObject, url, listener);
    }

    private static void query(final Context context, JSONObject jsonObject,
                              String url, OnCompleteListener listener)
            throws UnsupportedEncodingException {

        Log.e("", "query.jsonObject -> " + jsonObject);

        MultipartEntity multipartEntity = new MultipartEntity();

        StringBody stringBody = new StringBody(jsonObject.toString(),
                Charset.forName("UTF-8"));

        multipartEntity.addPart("data", stringBody);

        int queryType = QueryMaster.QUERY_POST;

        QueryMaster queryMaster = new QueryMaster(context, url, queryType,
                multipartEntity);
        queryMaster.setProgressDialog();

        // handle result
        queryMaster.setOnCompleteListener(listener);

        queryMaster.setPriority(QueryMaster.MIN_PRIORITY);
        queryMaster.start();
    }

    private static void queryOutProgress(final Context context,
                                         JSONObject jsonObject, String url,
                                         OnCompleteListener listener)
            throws UnsupportedEncodingException {

        Log.e("", "query.jsonObject -> " + jsonObject);

        MultipartEntity multipartEntity = new MultipartEntity();

        // StringBody stringBody = new StringBody(jsonObject.toString(),
        // Charset.forName("UTF-8"));

        multipartEntity.addPart("data", new StringBody(jsonObject.toString()));

        int queryType = QueryMaster.QUERY_POST;

        QueryMaster queryMaster = new QueryMaster(context, url, queryType,
                multipartEntity);

        // handle result
        queryMaster.setOnCompleteListener(listener);

        queryMaster.setPriority(QueryMaster.MIN_PRIORITY);
        queryMaster.start();
    }

    public static void getGroupMembers(Context context, String groupId,
                                       OnCompleteListener listener)
            throws UnsupportedEncodingException, JSONException {
        String url = ServerRequest.DOMAIN;

        Map<String, Object> jsonMap = new HashMap<String, Object>();
        Map<String, Object> data = new HashMap<String, Object>();

        data.put("id_group", groupId);

        jsonMap.put("action", "users.g-Group-members-group");
        jsonMap.put("data", new JSONObject(data));

        JSONObject jsonObject = new JSONObject(jsonMap);

        query(context, jsonObject, url, listener);
    }

    public static void deleteUserFromGroup(Context context, String deleteUserID, String groupID, OnCompleteListener listener) throws UnsupportedEncodingException {

        Map<String, Object> jsonMap = new HashMap<String, Object>();
        Map<String, Object> data = new HashMap<String, Object>();

        data.put("id_group", groupID);
        data.put("id_user", deleteUserID);

        jsonMap.put("action", "users.g-Group-delete-users-from-group");
        jsonMap.put("data", new JSONObject(data));

        JSONObject jsonObject = new JSONObject(jsonMap);

        query(context, jsonObject, ServerRequest.DOMAIN, listener);
    }

    public static void removeUserFromEvent(Context context, String eventID, String userID, OnCompleteListener listener) throws UnsupportedEncodingException {

//        data={"action":"users.g-Group-delete-from-group-event","data":{"id_group_event":"1","id_user":"7"}}

        Map<String, Object> jsonMap = new HashMap<String, Object>();
        Map<String, Object> data = new HashMap<String, Object>();

        data.put("id_user", userID);
        data.put("id_group_event", eventID);

        jsonMap.put("action", "users.g-Group-delete-from-group-event");
        jsonMap.put("data", new JSONObject(data));

        JSONObject jsonObject = new JSONObject(jsonMap);

        query(context, jsonObject, ServerRequest.DOMAIN, listener);
    }


    /**
     * Create test event in group
     *
     * @param context
     * @param listener
     * @param title
     * @param color
     * @param start_date_year
     * @param start_date_mounth
     * @param start_date_day
     * @param start_date_hour
     * @param start_date_minute
     * @param end_date_year
     * @param end_date_mounth
     * @param end_date_day
     * @param end_date_hour
     * @param end_date_minute
     * @param time_push
     * @param category
     * @param location
     * @param info
     * @param file_path
     * @param groupId
     * @throws java.io.UnsupportedEncodingException
     * @throws org.json.JSONException
     */
    public static void groupEventJSON(Context context,
                                      OnCompleteListener listener,
            /* int root, */String title, int color, int start_date_year,
            int start_date_mounth, int start_date_day, int start_date_hour,
            int start_date_minute, int end_date_year, int end_date_mounth,
            int end_date_day, int end_date_hour, int end_date_minute,
            String time_push, int category, int location, String info,
            String file_path, String groupId)
            throws UnsupportedEncodingException, JSONException {

        Map<String, Object> jsonMap = new HashMap<String, Object>();

        // jsonMap.put("root", root);
        jsonMap.put("title", String.valueOf(title));
        jsonMap.put("color", String.valueOf(color));
        jsonMap.put("start_date_day", String.valueOf(start_date_day));
        jsonMap.put("start_date_mounth", String.valueOf(start_date_mounth));
        jsonMap.put("start_date_year", String.valueOf(start_date_year));
        jsonMap.put("start_date_hour", String.valueOf(start_date_hour));
        jsonMap.put("start_date_minute", String.valueOf(start_date_minute));

        jsonMap.put("end_date_year", String.valueOf(end_date_year));
        jsonMap.put("end_date_mounth", String.valueOf(end_date_mounth));
        jsonMap.put("end_date_day", String.valueOf(end_date_day));
        jsonMap.put("end_date_hour", String.valueOf(end_date_hour));
        jsonMap.put("end_date_minute", String.valueOf(end_date_minute));

        jsonMap.put("time_push", String.valueOf(time_push));
        jsonMap.put("category", String.valueOf(category));
        jsonMap.put("location", String.valueOf(location));
        jsonMap.put("info", String.valueOf(info));
        jsonMap.put("file_path", String.valueOf(file_path));

        jsonMap.put("id_group", groupId);

        JSONObject jsonObject = new JSONObject(jsonMap);

        addEventToGroup(context, jsonObject, listener);
    }

    /**
     * @param context
     * @param jsonObject from groupEventJSON method
     * @param listener
     * @throws java.io.UnsupportedEncodingException
     * @throws org.json.JSONException
     */
    private static void addEventToGroup(Context context, JSONObject jsonObject,
                                        OnCompleteListener listener)
            throws UnsupportedEncodingException, JSONException {

        String url = ServerRequest.DOMAIN;

        Map<String, Object> jsonMap = new HashMap<String, Object>();

        jsonMap.put("action", "users.g-Group-create-event");

        jsonMap.put("data", jsonObject);

        // recreate
        jsonObject = new JSONObject(jsonMap);

        // Log.e("", "jsonObject -> " + jsonObject);

        query(context, jsonObject, url, listener);
    }

    public static void getEventsInGroup(Context context,
                                        OnCompleteListener listener, String groupId)
            throws UnsupportedEncodingException, JSONException {
        String url = ServerRequest.DOMAIN;

        Map<String, Object> jsonMap = new HashMap<String, Object>();
        Map<String, Object> data = new HashMap<String, Object>();

        data.put("id_group", groupId);

        jsonMap.put("action", "users.get-groups-events");
        jsonMap.put("data", new JSONObject(data));

        JSONObject jsonObject = new JSONObject(jsonMap);

        query(context, jsonObject, url, listener);
    }

    /**
     * Добавление участников в событие с
     * примечанием
     *
     * @param jsonArray
     * @param context
     * @param listener
     * @throws java.io.UnsupportedEncodingException
     * @throws org.json.JSONException
     */

    public static void addPeopleToEventInGroup(JSONArray jsonArray,
                                               Context context, OnCompleteListener listener)
            throws UnsupportedEncodingException, JSONException {

        String url = ServerRequest.DOMAIN;

        Map<String, Object> jsonMap = new HashMap<String, Object>();
        Map<String, Object> data = new HashMap<String, Object>();
        Map<String, Object> friends = new HashMap<String, Object>();


        friends.put("friends", jsonArray);

        jsonMap.put("action", "users.g-Group-add-user-to-event");
        data.put("array", new JSONObject(friends));

        jsonMap.put("data", new JSONObject(data));

        JSONObject jsonObject = new JSONObject(jsonMap);

        /**
         * data={"action":"users.g-Group-add-user-to-event","data":{"array":{
         * "friends":[{"id":"2","id_group":"2", "id_event":
         * "2","notes":"note1123123123123"}]}}}
         */

        query(context, jsonObject, url, listener);
    }

    public static void addPeopleToEventInGroup(Context context, String userID, String groupID, String eventID, OnCompleteListener listener)
            throws UnsupportedEncodingException {

        Map<String, Object> jsonMap = new HashMap<String, Object>();
        Map<String, Object> data = new HashMap<String, Object>();
        Map<String, Object> friends = new HashMap<String, Object>();

        Map<String, Object> justValues = new HashMap<String, Object>();
        justValues.put("id", userID);
        justValues.put("id_group", groupID);
        justValues.put("id_event", eventID);
        justValues.put("notes", "");

        JSONArray jsonArray = new JSONArray();
        jsonArray.put(new JSONObject(justValues));

        friends.put("friends", jsonArray);

        jsonMap.put("action", "users.g-Group-add-user-to-event");
        data.put("array", new JSONObject(friends));

        jsonMap.put("data", new JSONObject(data));

        JSONObject jsonObject = new JSONObject(jsonMap);

        query(context, jsonObject, ServerRequest.DOMAIN, listener);
    }

    public static void getEventMembers(Context context,
                                       OnCompleteListener listener, String groupId,
                                       String eventId) throws UnsupportedEncodingException, JSONException {

        String url = ServerRequest.DOMAIN;

        Map<String, Object> jsonMap = new HashMap<String, Object>();
        Map<String, Object> data = new HashMap<String, Object>();

        jsonMap.put("action", "users.get-groups-notes");

        data.put("id_group", groupId);
        data.put("id_event", eventId);

        jsonMap.put("data", new JSONObject(data));

        JSONObject jsonObject = new JSONObject(jsonMap);

        query(context, jsonObject, url, listener);
    }

    public static void getEventComments(Context context,
                                        OnCompleteListener listener, String eventId)
            throws UnsupportedEncodingException, JSONException {

        String url = ServerRequest.DOMAIN;

        Map<String, Object> jsonMap = new HashMap<String, Object>();
        Map<String, Object> data = new HashMap<String, Object>();

        data.put("id_event", eventId);

        jsonMap.put("action", "users.get-comments-event");
        jsonMap.put("data", new JSONObject(data));

        JSONObject jsonObject = new JSONObject(jsonMap);

        query(context, jsonObject, url, listener);

    }

    public static void getEvents(Context context,
                                 OnCompleteListener listener, String groupId)
            throws UnsupportedEncodingException {
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        Map<String, Object> data = new HashMap<String, Object>();

        data.put("id_group", groupId);

        jsonMap.put("action", "users.g-Group-events");
        jsonMap.put("data", new JSONObject(data));

        JSONObject jsonObject = new JSONObject(jsonMap);
        query(context, jsonObject, ServerRequest.DOMAIN, listener);
    }

    public static void getEventInfo(Context context,
                                    OnCompleteListener listener, String eventId,
                                    String groupId) throws UnsupportedEncodingException {

        Map<String, Object> jsonMap = new HashMap<String, Object>();
        Map<String, Object> data = new HashMap<String, Object>();

        data.put("id", groupId);
        data.put("id_event", eventId);

        jsonMap.put("action", "users.g-Group-all-info");
        jsonMap.put("data", new JSONObject(data));

        JSONObject jsonObject = new JSONObject(jsonMap);
        queryOutProgress(context, jsonObject, ServerRequest.DOMAIN, listener);
    }

    /**
     * @param commentDate format 2014-09-01 12:15:10
     * @throws java.io.UnsupportedEncodingException
     * @throws org.json.JSONException
     */
    public static void addEventComment(Context context,
                                       OnCompleteListener addCommentComplete, String eventId,
                                       String userId, String message, String commentDate)
            throws UnsupportedEncodingException, JSONException {
        String url = ServerRequest.DOMAIN;

        Map<String, Object> jsonMap = new HashMap<String, Object>();
        Map<String, Object> data = new HashMap<String, Object>();

        data.put("id_event", eventId);
        data.put("id_user", userId);
        data.put("message", message);
        data.put("date", commentDate);

        jsonMap.put("action", ServerActions.ADD_COMMENT_EVENT);
        jsonMap.put("data", new JSONObject(data));

        JSONObject jsonObject = new JSONObject(jsonMap);

        query(context, jsonObject, url, addCommentComplete);
    }

    public static void addUserNote(Context context,
                                   OnCompleteListener listener, String id_event,
                                   String id_user, String note) throws UnsupportedEncodingException {

        // data={"action":"users.g-Group-add-user-note","data":{"id_event":"12","id_user":"3","note":"����� �������"}}

        Map<String, Object> jsonMap = new HashMap<String, Object>();
        Map<String, Object> data = new HashMap<String, Object>();

        jsonMap.put("action", "users.g-Group-add-user-note");

        data.put("id_event", id_event);
        data.put("id_user", id_user);
        data.put("note", note);

        jsonMap.put("data", new JSONObject(data));

        JSONObject jsonObject = new JSONObject(jsonMap);

        query(context, jsonObject, ServerRequest.DOMAIN, listener);
    }

    private static interface ServerActions {
        public static final String ADD_COMMENT_EVENT = "users.g-Group-comments-group-event";
    }

    // drstg

    public static void getMyTasks(Context context,
                                  OnCompleteListener listener, String id_user)
            throws UnsupportedEncodingException, JSONException {
        String url = ServerRequest.DOMAIN;

        Map<String, Object> jsonMap = new HashMap<String, Object>();
        Map<String, Object> data = new HashMap<String, Object>();

        data.put("id_user", id_user);

        jsonMap.put("action", "users.g-Group-get-my-events");
        jsonMap.put("data", new JSONObject(data));

        JSONObject jsonObject = new JSONObject(jsonMap);

        query(context, jsonObject, url, listener);
    }

    public static void confirmGroupParticipation(Context context,
                                                 OnCompleteListener listener, String groupID,
                                                 String userID, String status) throws UnsupportedEncodingException {

        Map<String, Object> jsonMap = new HashMap<String, Object>();
        Map<String, Object> data = new HashMap<String, Object>();

        data.put("id_user", userID);
        data.put("status", status);
        data.put("id_group", groupID);

        jsonMap.put("action", "users.g-Group-group-status-confirmation");
        jsonMap.put("data", new JSONObject(data));

        JSONObject jsonObject = new JSONObject(jsonMap);

        query(context, jsonObject, ServerRequest.DOMAIN, listener);
        // data={"action":"users.g-Group-status-confirmation","data":{"id_group":"1","id_user":"3","status":"1"}}
    }

    public static void confirmEventParticipation(Context context,
                                                 OnCompleteListener listener, String eventID,
                                                 String userID, String status) throws UnsupportedEncodingException {
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        Map<String, Object> data = new HashMap<String, Object>();

        data.put("id_user", userID);
        data.put("status", status);
        data.put("id_event", eventID);

        jsonMap.put("action", "users.g-Group-event-confirmation");
        jsonMap.put("data", new JSONObject(data));

        JSONObject jsonObject = new JSONObject(jsonMap);

        query(context, jsonObject, ServerRequest.DOMAIN, listener);

        // data={"action":"users.g-Group-status-confirmation","data":{"id_event":"1","id_user":"3","status":"1"}}
    }

    public static void confirmEventParticipation(Context context,
                                                 OnCompleteListener listener, String groupID,
                                                 String eventID, String userID, String noteID, String status)
            throws UnsupportedEncodingException {
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        Map<String, Object> data = new HashMap<String, Object>();

        data.put("status", status);
        data.put("id_event", eventID);
        data.put("id_user", userID);
        data.put("id_group", groupID);
        data.put("id_note", noteID);

        jsonMap.put("action", "users.g-Group-event-confirmation");
        jsonMap.put("data", new JSONObject(data));

        JSONObject jsonObject = new JSONObject(jsonMap);

        query(context, jsonObject, ServerRequest.DOMAIN, listener);

        // data={"action":"users.g-Group-status-confirmation","data":{"id_event":"1","id_user":"3","status":"1"}}
    }

}
