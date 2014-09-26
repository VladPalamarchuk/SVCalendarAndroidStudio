package bcalendargroups;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import bcalendargroups.pager.GroupEvents;

import com.daimajia.androidanimations.library.attention.ShakeAnimator;
import com.example.calendar.MainActivity;
import com.example.calendar.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class SimpleEditText extends AlertDialog.Builder implements
        QueryMaster.OnCompleteListener {

    private GroupEvents context;
    private String eventID;
    private String userID;
    private AlertDialog alertDialog;

    public SimpleEditText(GroupEvents context, String eventID, String userID) {
        super(context);
        this.context = context;
        this.eventID = eventID;
        this.userID = userID;
    }

    @Override
    public AlertDialog show() {
        LayoutInflater layoutInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final EditText editText = (EditText) layoutInflater.inflate(
                R.layout.simple_edit_text, null);
        editText.setHint(MainActivity.getInstance().language.ENTER_TEXT_NOTES);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(editText);
        /**
         * positive handler beside
         */
        builder.setPositiveButton("Ok", null);
        builder.setNegativeButton(MainActivity.getInstance().language.CANCEL,
                null);

        alertDialog = builder.create();
        alertDialog.show();

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String text = editText.getText().toString();
                        if (text.isEmpty()) {
                            ShakeAnimator animator = new ShakeAnimator();
                            animator.animate(editText);
                        } else {
                            send(text);
                        }
                    }
                });
        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
        return null;
    }

    @Override
    public void complete(String serverResponse) {
        try {
            JSONObject json = new JSONObject(serverResponse);
            if (QueryMaster.isSuccess(json)) {
                QueryMaster.toast(context,
                        MainActivity.getInstance().language.ADDED);

                context.getSectionsPagerAdapter().notifyDataSetChanged();
                alertDialog.dismiss();

            } else {
                QueryMaster.toast(context, MainActivity.getInstance().language.ERROR);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            QueryMaster.alert(context, QueryMaster.SERVER_RETURN_INVALID_DATA);
            alertDialog.dismiss();
        }
    }

    @Override
    public void error(int errorCode) {
        QueryMaster.alert(context, QueryMaster.ERROR_MESSAGE);
    }

    private void send(String text) {

        try {
            GroupAssignmentActions.addUserNote(context, this, eventID, userID,
                    text);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }
}
