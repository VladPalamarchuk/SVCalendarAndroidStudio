package dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

import bcalendargroups.GroupAssignmentActions;
import bcalendargroups.QueryMaster;
import bcalendargroups.adapters.ExpandableListAdapterNew;
import bcalendargroups.adapters.MyGroupTasksAdapter;
import bcalendargroups.adapters.MyGroupTasksAdapter.MyGroupTasksAdapterItem;
import bcalendargroups.adapters.MyGroupTasksAdapter.OnDeclineTaskListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.calendar.MainActivity;
import com.example.calendar.R;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class MyGroupTasks extends AlertDialog.Builder implements
        QueryMaster.OnCompleteListener, MyGroupTasksAdapter.OnClickListener,
        OnDeclineTaskListener {

    private MyGroupTasksAdapter myGroupTasksAdapter;
    private final String userID;
    private final String groupID;

    private OnScreenUpdateListener onScreenUpdateListener;

    public void setOnScreenUpdateListener(
            OnScreenUpdateListener onScreenUpdateListener) {
        this.onScreenUpdateListener = onScreenUpdateListener;
    }

    public MyGroupTasks(Context context, String userID, String groupID) {
        super(context);
        this.myGroupTasksAdapter = new MyGroupTasksAdapter(context);
        this.myGroupTasksAdapter.setOnClickListener(this);
        this.myGroupTasksAdapter.setOnDeclineTaskListener(this);
        this.userID = userID;
        this.groupID = groupID;
    }

    @Override
    public void complete(String serverResponse) {
        try {
            JSONObject json = new JSONObject(serverResponse);
            if (QueryMaster.isSuccess(json)) {
                JSONArray data = json.getJSONArray("data");

                this.myGroupTasksAdapter.clear();
                this.myGroupTasksAdapter.add(data);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            QueryMaster.alert(getContext(),
                    QueryMaster.SERVER_RETURN_INVALID_DATA);
        }
    }

    @Override
    public void error(int errorCode) {
        QueryMaster.alert(getContext(), QueryMaster.ERROR_MESSAGE);
    }

    @Override
    public AlertDialog show() {
        showView();
        return null;
    }

    public void load() {
        parse();
    }

    public ArrayList<Object> getList() {
        return myGroupTasksAdapter.getCollection();
    }

    public MyGroupTasksAdapter getAdapter() {
        return myGroupTasksAdapter;
    }

    private void parse() {
        try {
            GroupAssignmentActions.getMyTasks(getContext(), this, userID);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (JSONException e) {
            e.printStackTrace();
            QueryMaster.alert(getContext(),
                    QueryMaster.SERVER_RETURN_INVALID_DATA);
        }
    }

    private void showView() {
        if (myGroupTasksAdapter.isEmpty()) {
            parse();
        }

        Context context = getContext();
        LayoutInflater layoutInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ListView listView = (ListView) layoutInflater.inflate(
                R.layout.simple_listview, null);
        listView.setAdapter(myGroupTasksAdapter);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(listView);
        /**
         * positive handler beside
         */
        builder.setPositiveButton("Ok", null);
        builder.setNegativeButton(MainActivity.getInstance().language.CANCEL,
                null);

        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
    }

    private String getString(int res) {
        return getContext().getString(res);
    }

    // data={"action":"users.g-Group-status-confirmation","data":{"id_event":"1","id_user":"3","status":"1"}}
    private void confirmTask(String eventID, String noteID,
                             boolean confirmationStatus) {
        try {
            final String status = confirmationStatus ? "1" : "0";
            GroupAssignmentActions
                    .confirmMyTask(getContext(), onConfirmListener, userID,
                            status, eventID, noteID, groupID);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private void declineTask(String eventID, String noteID) {
        try {
            final String status = "-1";
            GroupAssignmentActions
                    .confirmMyTask(getContext(), onDeclineListener, userID,
                            status, eventID, noteID, groupID);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onClick(
            MyGroupTasksAdapterItem myGroupTasksAdapterItem,
            boolean isChecked) {

        this.confirmTask(myGroupTasksAdapterItem.id_event,
                myGroupTasksAdapterItem.id, isChecked);
    }

    public class ConfirmThisTask extends AlertDialog.Builder {

        private final ExpandableListAdapterNew.SingleUserNote userNote;
        private final String eventID;

        public ConfirmThisTask(ExpandableListAdapterNew.SingleUserNote userNote, String eventID) {
            super(MyGroupTasks.this.getContext());
            this.eventID = eventID;
            this.userNote = userNote;
        }

        public AlertDialog showDialog() {

            setTitle(MainActivity.getInstance().language.CONFIRM_EXECUTE_TASK);

            setPositiveButton(
                    MainActivity.getInstance().language.TASK_PERFORMED, null);
            setNeutralButton(MainActivity.getInstance().language.DECLINE_TASK,
                    null);
            setNegativeButton(MainActivity.getInstance().language.CANCEL, null);

            final AlertDialog alertDialog = create();
            alertDialog.show();

            // TASK_PERFORMED
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
                    .setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            MyGroupTasks.this.confirmTask(eventID,
                                    userNote.id_note, true);
                            alertDialog.dismiss();
                        }
                    });
            // DECLINE_TASK
            alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL)
                    .setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            MyGroupTasks.this.declineTask(eventID,
                                    userNote.id_note);
                            alertDialog.dismiss();
                        }
                    });

            return null;
        }

    }

    private final QueryMaster.OnCompleteListener onConfirmListener = new QueryMaster.OnCompleteListener() {
        @Override
        public void complete(String serverResponse) {
            // QueryMaster.alert(getContext(), serverResponse);
            try {
                JSONObject json = new JSONObject(serverResponse);
                if (QueryMaster.isSuccess(json)) {
                    parse();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                QueryMaster.alert(getContext(),
                        QueryMaster.SERVER_RETURN_INVALID_DATA);
            }
        }

        @Override
        public void error(int errorCode) {
            QueryMaster.alert(getContext(), QueryMaster.ERROR_MESSAGE);
        }
    };
    private final QueryMaster.OnCompleteListener onDeclineListener = new QueryMaster.OnCompleteListener() {

        @Override
        public void complete(String serverResponse) {
            try {
                JSONObject json = new JSONObject(serverResponse);
                if (QueryMaster.isSuccess(json)) {
                    parse();
                    QueryMaster
                            .toast(getContext(), "You was decline this task");
                }
            } catch (JSONException e) {
                e.printStackTrace();
                QueryMaster.alert(getContext(),
                        QueryMaster.SERVER_RETURN_INVALID_DATA);
            }
        }

        @Override
        public void error(int errorCode) {
            QueryMaster.alert(getContext(), QueryMaster.ERROR_MESSAGE);
        }
    };

    @Override
    public void decline(MyGroupTasksAdapterItem myGroupTasksAdapterItem) {
        this.declineTask(myGroupTasksAdapterItem.id_event,
                myGroupTasksAdapterItem.id);
    }

    /**
     * @author Called when different network query was done
     */
    public static interface OnScreenUpdateListener {
        public void updateScreen();
    }
}
