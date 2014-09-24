package bcalendargroups;//package bcalendargroups;
//
//import android.app.Activity;
//import android.app.AlertDialog;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ListView;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.io.UnsupportedEncodingException;
//
//
//public class DrawerListActivity extends Activity implements DeviceKey.OnKeyRegisteredListener {
//
//    private ListView groupTasksList;
//    private GroupTaskListAdapter groupTaskListAdapter;
//
//    private Button createNewGroup;
//
//    /**
//     * UNIQUE USER ID IN CALENDAR SERVER SYSTEM
//     */
//    private String userId = "1";
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_drawer_list);
//
//
//        createNewGroup = (Button) findViewById(R.id.createNewGroup);
//        createNewGroup.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                addGroupDialog();
//            }
//        });
//
//
//        groupTasksList = (ListView) findViewById(R.id.groupTasksList);
//        groupTaskListAdapter = new GroupTaskListAdapter(this, R.layout.assignment_list_item);
//
//        groupTaskListAdapter.setOnGroupTaskClickListener(new GroupTaskListAdapter.OnGroupTaskClickListener() {
//            @Override
//            public void onClick(GroupTaskListAdapter.AssignmentListItem assignmentListItem) {
////                startSingleGroupAssignmentActivity(assignmentListItem);
//                startActivity(new Intent(getActivity(), GroupEvents.class));
//            }
//        });
//
//        groupTasksList.setAdapter(groupTaskListAdapter);
//
//        // create group example
//        try {
//            loadGroups();
//        } catch (JSONException e) {
//            e.printStackTrace();
//            throw new RuntimeException(e);
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//            throw new RuntimeException(e);
//        }
//
//        saveUserId(getActivity(), userId);
//    }
//
//    public static void saveUserId(Context context, String userId) {
//        SharedPreferences sharedPreferences = context.getSharedPreferences(SingleGroup.class.getName(),
//                Context.MODE_PRIVATE);
//        sharedPreferences.edit().putString(SharedPrefKeys.USER_ID_KEY, userId).apply();
//    }
//
//    public static String getGroupId(Context context) {
//        SharedPreferences sharedPreferences = context.getSharedPreferences(SingleGroup.class.getName(),
//                Context.MODE_PRIVATE);
//
//        return sharedPreferences.getString(SharedPrefKeys.USER_ID_KEY, null);
//    }
//
//    /**
//     * Load all groups by current userId
//     *
//     * @throws JSONException
//     * @throws UnsupportedEncodingException
//     */
//    private void loadGroups() throws JSONException, UnsupportedEncodingException {
//        GroupAssignmentActions.getMyGroups(getActivity(), userId,
//                onLoadGroupsComplete);
//    }
//
//    private QueryMaster.OnCompleteListener onLoadGroupsComplete = new QueryMaster.OnCompleteListener() {
//        @Override
//        public void complete(String serverResponse) {
//            try {
//                JSONObject jsonObject = new JSONObject(serverResponse);
//                if (QueryMaster.isSuccess(jsonObject)) {
//                    JSONArray jsonArray = jsonObject.getJSONArray("data");
//
//                    groupTaskListAdapter.clear();
//
//                    groupTaskListAdapter.add(jsonArray);
//                }
//
//                groupTaskListAdapter.insertAsFirst(new GroupTaskListAdapter.GroupTaskDivider("Мои группы"));
//                groupTaskListAdapter.add(new GroupTaskListAdapter.GroupTaskDivider("Группы с моим участием"));
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//                throw new RuntimeException(e);
//            }
//        }
//
//        @Override
//        public void error(int errorCode) {
//            QueryMaster.alert(getActivity(), "error");
//        }
//    };
//
////    private void startSingleGroupAssignmentActivity(GroupTaskListAdapter.AssignmentListItem assignmentListItem) {
////
////        startSingleGroupAssignmentActivity(assignmentListItem.title,
////                assignmentListItem.id);
////    }
//
////    private void startSingleGroupAssignmentActivity(String title, String id) {
////        Intent intent = new Intent(this, SingleGroup.class);
////        intent.putExtra(SingleGroup.BundleKeys.TITLE_BUNDLE_KEY, title);
////        intent.putExtra(SingleGroup.BundleKeys.ID_BUNDLE_KEY, id);
////
////        startActivity(intent);
////    }
//
//    @Override
//    public void keyRegistered(String key) {
//        QueryMaster.toast(this, key);
//    }
//
//    @Override
//    public void GooglePlayServicesUnavailable() {
//
//    }
//
//    @Override
//    public void UnableObtainKey() {
//
//    }
//
//    private Activity getActivity() {
//        return DrawerListActivity.this;
//    }
//
//    private void addGroupDialog() {
//        Context context = getActivity();
//        LayoutInflater layoutInflater = (LayoutInflater)
//                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        final EditText dialogViewEditText = (EditText) layoutInflater.inflate(R.layout.simple_edit_text, null);
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//        builder.setView(dialogViewEditText);
//        builder.setPositiveButton("Добавить", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                createGroup(dialogViewEditText.getText().toString(), userId);
//            }
//        });
//        builder.setNegativeButton("Отмена", null);
//        builder.show();
//    }
//
//    private void createGroup(String title, String adminId) {
//
//        QueryMaster.OnCompleteListener listener = new QueryMaster.OnCompleteListener() {
//            @Override
//            public void complete(String serverResponse) {
//                try {
//                    loadGroups();
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                    throw new RuntimeException(e);
//                } catch (UnsupportedEncodingException e) {
//                    e.printStackTrace();
//                    throw new RuntimeException(e);
//                }
//            }
//
//            @Override
//            public void error(int errorCode) {
//                QueryMaster.alert(getActivity(), "ERROR");
//            }
//        };
//        try {
//            GroupAssignmentActions.createGroup(getActivity(), listener, title, adminId);
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//            throw new RuntimeException(e);
//        }
//    }
//
//    private static interface SharedPrefKeys {
//        public static final String USER_ID_KEY = "ID_BUNDLE_KEY";
//    }
//}
