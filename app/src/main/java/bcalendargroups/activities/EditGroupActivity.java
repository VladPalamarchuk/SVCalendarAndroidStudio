package bcalendargroups.activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import com.example.calendar.MainActivity;
import com.example.calendar.R;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Fullscreen;
import org.androidannotations.annotations.NoTitle;
import org.androidannotations.annotations.ViewById;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import bcalendargroups.GroupAssignmentActions;
import bcalendargroups.QueryMaster;
import bcalendargroups.SearchPeople;
import bcalendargroups.adapters.GroupMembersAdapter;
import bcalendargroups.pager.GroupEvents;

@NoTitle
@Fullscreen
@EActivity(R.layout.activity_edit_group)
public class EditGroupActivity extends Activity {

    public static final String GROUP_ID = "GROUP_ID";

    private String groupID;

    private GroupMembersAdapter groupMembersAdapter;


    @ViewById(R.id.activityEditGroupMembersListView)
    protected ListView activityEditGroupMembersListView;

    @Click(R.id.activityEditGroupAddMember)
    protected void clickAddGroupMember() {
        new SearchPeople(this, groupID).searchPeopleDialog();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        groupID = getIntent().getExtras().getString(GROUP_ID);
        groupMembersAdapter = new GroupMembersAdapter(this);

    }

    @Override
    protected void onStart() {
        super.onStart();

        activityEditGroupMembersListView.setAdapter(groupMembersAdapter);

        loadGroupMembers(groupID);
    }

    /**
     * Load group members
     *
     * @param groupId id of group
     */
    private void loadGroupMembers(String groupId) {
        try {
            GroupAssignmentActions.getGroupMembers(this, groupId,
                    onLoadGroupMembersComplete);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (JSONException e) {
            e.printStackTrace();
            QueryMaster.alert(this, QueryMaster.SERVER_RETURN_INVALID_DATA);
        }
    }

    private QueryMaster.OnCompleteListener onLoadGroupMembersComplete = new QueryMaster.OnCompleteListener() {
        @Override
        public void complete(String serverResponse) {
            try {
                JSONObject jsonObject = new JSONObject(serverResponse);
                if (QueryMaster.isSuccess(jsonObject)) {
                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    groupMembersAdapter.clear();
                    groupMembersAdapter.add(jsonArray);
                } else {
                    QueryMaster.toast(EditGroupActivity.this,
                            MainActivity.getInstance().language.NO_MEMBERS);
                }

            } catch (JSONException e) {
                e.printStackTrace();
                QueryMaster.alert(EditGroupActivity.this, QueryMaster.SERVER_RETURN_INVALID_DATA);
            }
        }

        @Override
        public void error(int errorCode) {
            QueryMaster.alert(EditGroupActivity.this, QueryMaster.ERROR_MESSAGE);
        }
    };
}
