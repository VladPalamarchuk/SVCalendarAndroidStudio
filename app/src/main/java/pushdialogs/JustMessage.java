package pushdialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.view.View;


public class JustMessage extends AlertDialog.Builder {

    private Context context;
    private final String message;


    public JustMessage(Context context, String message) {
        super(context);
        this.message = message;
        this.context = context;
    }

    @Override
    public AlertDialog show() {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message);
        builder.setTitle("Group events");
        /**
         * positive handler beside
         */
        builder.setPositiveButton("Ok", null);
        builder.setNegativeButton("Cancel", null);

        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        return null;
    }
}
