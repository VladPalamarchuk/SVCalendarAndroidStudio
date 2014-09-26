package bcalendargroups.activities;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ListView;
import android.widget.TextView;

import com.example.calendar.R;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Fullscreen;
import org.androidannotations.annotations.NoTitle;
import org.androidannotations.annotations.ViewById;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;

import bcalendargroups.GroupAssignmentActions;
import bcalendargroups.QueryMaster;
import bcalendargroups.SearchPeople;
import bcalendargroups.adapters.GroupMembersAdapter;
import bcalendargroups.pager.GroupEvents;
import dialogs.EditGroupInitialDialog;

@NoTitle
@Fullscreen
@EActivity(R.layout.activity_edit_group)
public class EditGroupActivity extends Activity {

    public static final String GROUP_ID = "GROUP_ID";
    public static final String GROUP_TITLE = "GROUP_TITLE";

    private static final String REMOVE_USER_MESSAGE = "Remove user from group?";

    private String groupID;
    private String groupTitle;

    private GroupMembersAdapter groupMembersAdapter;
    private ArrayList<GroupMembersAdapter.GroupMemberItem> groupMemberItems;

    private SharedPreferences sharedPreferences;


    @ViewById(R.id.activityEditGroupTitle)
    protected TextView activityEditGroupTitle;
//    @ViewById(R.id.activityEditGroupTitle)
//    protected EditText activityEditGroupTitle;

    @ViewById(R.id.activityEditGroupMembersListView)
    protected ListView activityEditGroupMembersListView;

    @Click(R.id.activityEditGroupAddMember)
    protected void clickAddGroupMember() {
        SearchPeople searchPeople = new SearchPeople(this, groupID);
        searchPeople.setOnCompleteListener(onAddPeopleListener);
        searchPeople.searchPeopleDialog();
    }

    @Click(R.id.activityEditGroupUpdateTitle)
    protected void updateTitle() {
        QueryMaster.toast(this, "updateTitle()");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getIntent().getExtras();

        groupID = args.getString(GROUP_ID);
        groupTitle = args.getString(GROUP_TITLE);

        groupMemberItems = (ArrayList<GroupMembersAdapter.GroupMemberItem>) args.getSerializable(GroupEvents.GROUP_MEMBERS);
        Collections.sort(groupMemberItems);

        groupMembersAdapter = new GroupMembersAdapter(this);
        groupMembersAdapter.setOnLongClickListener(onMemberLongClick);
        groupMembersAdapter.addAll(groupMemberItems);

        sharedPreferences = getSharedPreferences(EditGroupActivity.class.toString(),
                Context.MODE_PRIVATE);

    }

    @Override
    protected void onStart() {
        super.onStart();

        new EditGroupInitialDialog(this, sharedPreferences).show();

        activityEditGroupMembersListView.setAdapter(groupMembersAdapter);

        activityEditGroupTitle.setText(groupTitle);
    }

//    private TextView.OnEditorActionListener onTitleDone = new TextView.OnEditorActionListener() {
//        @Override
//        public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
//
//            if (i == EditorInfo.IME_ACTION_DONE) {
////                updateTitle(activityEditGroupTitle.getText().toString());
//                return true;
//            }
//            return false;
//        }
//    };

    private void updateTitle(String title) {
        final QueryMaster.OnCompleteListener onCompleteListener = new QueryMaster.OnCompleteListener() {
            @Override
            public void complete(String serverResponse) {
                QueryMaster.alert(EditGroupActivity.this, serverResponse);
            }

            @Override
            public void error(int errorCode) {
                QueryMaster.alert(EditGroupActivity.this, QueryMaster.ERROR_MESSAGE);
            }
        };

        try {
            GroupAssignmentActions.updateGroupTitle(this, title, groupID, onCompleteListener);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private GroupMembersAdapter.OnLongClickListener onMemberLongClick = new GroupMembersAdapter.OnLongClickListener() {
        @Override
        public void longClick(Object o) {

            final GroupMembersAdapter.GroupMemberItem groupMemberItem = (GroupMembersAdapter.GroupMemberItem) o;

            QueryMaster.question(EditGroupActivity.this, REMOVE_USER_MESSAGE,
                    "REMOVE", new RemoveUser(groupMemberItem),
                    "CANCEL", null);

        }
    };

    private class RemoveUser implements View.OnClickListener {

        private RemoveUser(GroupMembersAdapter.GroupMemberItem groupMemberItem) {
            this.groupMemberItem = groupMemberItem;
        }

        private final GroupMembersAdapter.GroupMemberItem groupMemberItem;

        @Override
        public void onClick(View view) {
            deleteMember(groupMemberItem);
        }
    }

    private void deleteMember(GroupMembersAdapter.GroupMemberItem memberItem) {
        try {
            OnMemberDeleteAction memberDeleteAction = new OnMemberDeleteAction(memberItem);

            GroupAssignmentActions.deleteUserFromGroup(this, memberItem.id,
                    groupID, memberDeleteAction);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private class OnMemberDeleteAction implements QueryMaster.OnCompleteListener {

        private final GroupMembersAdapter.GroupMemberItem groupMemberItem;

        private OnMemberDeleteAction(GroupMembersAdapter.GroupMemberItem groupMemberItem) {
            this.groupMemberItem = groupMemberItem;
        }

        @Override
        public void complete(String serverResponse) {
            groupMembersAdapter.remove(groupMemberItem);
            groupMembersAdapter.notifyDataSetChanged();
        }

        @Override
        public void error(int errorCode) {
            QueryMaster.alert(EditGroupActivity.this, QueryMaster.SERVER_RETURN_INVALID_DATA);
        }
    }


    /*
    * must to be called after add people to refresh page
    * not use because added user must confirm participation in group,
    * so update screen immediately no sense
    * */
    private SearchPeople.OnCompleteListener onAddPeopleListener = new SearchPeople.OnCompleteListener() {
        @Override
        public void complete() {
            QueryMaster.alert(EditGroupActivity.this, "Participation request was send to user!");
        }
    };
}
