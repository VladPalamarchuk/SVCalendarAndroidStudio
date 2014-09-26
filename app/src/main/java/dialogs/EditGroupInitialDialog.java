package dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;


public class EditGroupInitialDialog extends AlertDialog.Builder {

    private static final String IS_SHOWED_BEFORE = "IS_SHOWED_BEFORE";

    private SharedPreferences sharedPreferences;
    private Context context;

    public EditGroupInitialDialog(Context context, SharedPreferences sharedPreferences) {
        super(context);
        this.sharedPreferences = sharedPreferences;
        this.context = context;
    }

    @Override
    public AlertDialog show() {
        if (sharedPreferences.getBoolean(IS_SHOWED_BEFORE, false)) {
            return null;
        }
        setTitle("Group Edit Information");
        setMessage("To remove user from group touch and wait at user. " +
                "To add user - click associated button.");

        setPositiveButton("ok", null);

        /**
         * store value to not show this dialog again
         */
        sharedPreferences.edit().putBoolean(IS_SHOWED_BEFORE, true).apply();

        return super.show();
    }
}
