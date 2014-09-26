package dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.daimajia.androidanimations.library.attention.ShakeAnimator;
import com.example.calendar.R;

import java.io.UnsupportedEncodingException;

import bcalendargroups.GroupAssignmentActions;
import bcalendargroups.QueryMaster;
import bcalendargroups.adapters.ExpandableListAdapterNew;

public class NoteConfigurationDialog extends AlertDialog.Builder {

    private ScreenUpdateListener screenUpdateListener;

    public void setScreenUpdateListener(ScreenUpdateListener screenUpdateListener) {
        this.screenUpdateListener = screenUpdateListener;
    }


    private final ExpandableListAdapterNew.SingleUserNote singleUserNote;
    private final Context context;
    private final String userID;
    private final String groupID;

    public NoteConfigurationDialog(Context context, ExpandableListAdapterNew.SingleUserNote singleUserNote,
                                   String userID, String groupID) {
        super(context);

        this.singleUserNote = singleUserNote;
        this.context = context;
        this.userID = userID;
        this.groupID = groupID;
    }

    @NonNull
    @Override
    public AlertDialog show() {

        setTitle("Note configuration");

        setPositiveButton("Update note", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                updateNoteDialog();
            }
        });
        setNeutralButton("Remove note", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                removeNote();
            }
        });
        setNegativeButton("Remind", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                remind();
            }
        });

        return super.show();
    }


    private void removeNote() {
        final QueryMaster.OnCompleteListener onCompleteListener = new QueryMaster.OnCompleteListener() {
            @Override
            public void complete(String serverResponse) {
                notifyScreenUpdateListener();
            }

            @Override
            public void error(int errorCode) {
                QueryMaster.alert(context, QueryMaster.ERROR_MESSAGE);
            }
        };

        try {
            GroupAssignmentActions.removeUserNote(context, singleUserNote.id_note, userID, onCompleteListener);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private void updateNoteDialog() {

        LayoutInflater layoutInflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final EditText editText = (EditText) layoutInflater.inflate(R.layout.simple_edit_text, null);
        editText.setHint("Enter new note body");

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(editText);
        /**
         * positive handler beside
         */
        builder.setPositiveButton("Update", null);
        builder.setNegativeButton("Cancel", null);

        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!editText.getText().toString().isEmpty()) {

                    updateNote(editText.getText().toString());
                    alertDialog.dismiss();
                } else {
                    new ShakeAnimator().animate(alertDialog.getWindow().getDecorView().findViewById(android.R.id.content));
                }
            }
        });

        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }

    private void updateNote(String noteBody) {

        final QueryMaster.OnCompleteListener onCompleteListener = new QueryMaster.OnCompleteListener() {
            @Override
            public void complete(String serverResponse) {
                notifyScreenUpdateListener();
            }

            @Override
            public void error(int errorCode) {
                QueryMaster.alert(context, QueryMaster.ERROR_MESSAGE);
            }
        };

        try {
            GroupAssignmentActions.updateUserNote(context, singleUserNote.id_note, noteBody, onCompleteListener);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private void remind() {
        final QueryMaster.OnCompleteListener onCompleteListener = new QueryMaster.OnCompleteListener() {
            @Override
            public void complete(String serverResponse) {

                // when we make just remind we don't need update screen
//                notifyScreenUpdateListener();
            }

            @Override
            public void error(int errorCode) {
                QueryMaster.alert(context, QueryMaster.ERROR_MESSAGE);
            }
        };
        try {
            GroupAssignmentActions.remindUserNote(context, userID, groupID, singleUserNote.id_note, onCompleteListener);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void notifyScreenUpdateListener() {
        if (screenUpdateListener != null) {
            screenUpdateListener.update();
        }
    }

    public static interface ScreenUpdateListener {
        public void update();
    }
}
