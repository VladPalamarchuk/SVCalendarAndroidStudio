package bcalendargroups;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;


public class QueryMaster extends Thread {

    public static final String ERROR_MESSAGE = "error message";

    public static final String SERVER_RETURN_INVALID_DATA = "SERVER_RETURN_INVALID_DATA";

    public static final int QUERY_MASTER_COMPLETE = 1;
    public static final int QUERY_MASTER_ERROR = 2;
    public static final int QUERY_MASTER_NETWORK_ERROR = 3;

    public static final int QUERY_GET = 23;
    public static final int QUERY_POST = 24;

    public static final String STATUS_SUCCESS = "success";


    public void setProgressDialog() {
        this.progressDialog = ProgressDialog.show(context, null, "BLABLA...");
    }

    private ProgressDialog progressDialog;

    private OnCompleteListener onCompleteListener;
    private Handler handler;

    private MultipartEntity entity;
    private Context context;

    private String serverResponse;
    private String url;

    private int queryType;

    public static int timeoutConnection = 10000;


    /**
     * @param context
     * @param url
     * @param queryType QueryMaster.QUERY_GET or QueryMaster.QUERY_POST
     * @param entity
     */
    public QueryMaster(Context context, String url, int queryType, MultipartEntity entity) {
        this(context, url, queryType);
        this.entity = entity;
    }

    public QueryMaster(Context context, String url, int queryType) {
        this.context = context;
        this.url = url;
        this.queryType = queryType;
        initHandler();
    }

    @Override
    public void run() {
        super.run();

        if (!isNetworkConnected()) {
            handler.sendEmptyMessage(QUERY_MASTER_NETWORK_ERROR);
            return;
        }
        HttpParams httpParams = new BasicHttpParams();
        httpParams.setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,
                timeoutConnection);


        DefaultHttpClient httpclient = new DefaultHttpClient(httpParams);
//        httpclient.setParams(httpParams);

        HttpPost httpPost;
        HttpGet httpGet;

        HttpResponse response = null;

        try {

            if (queryType == QUERY_GET) {
                httpGet = new HttpGet(url);

                response = httpclient.execute(httpGet);

            } else if (queryType == QUERY_POST) {

                httpPost = new HttpPost(url);
                if (entity != null) {
                    httpPost.setEntity(entity);
                }
                response = httpclient.execute(httpPost);
            }

            serverResponse = EntityUtils.toString(response.getEntity());

            Log.i("QueryMaster", "Url -> " + url);
            Log.i("QueryMaster", "serverResponse -> " + serverResponse);

            handler.sendEmptyMessage(QUERY_MASTER_COMPLETE);

        } catch (ClientProtocolException e) {
            e.printStackTrace();
            handler.sendEmptyMessage(QUERY_MASTER_ERROR);
        } catch (IOException e) {
            e.printStackTrace();
            handler.sendEmptyMessage(QUERY_MASTER_ERROR);
        } catch (NullPointerException e) {
            e.printStackTrace();
            handler.sendEmptyMessage(QUERY_MASTER_ERROR);
        }
    }

    public void setOnCompleteListener(OnCompleteListener onCompleteListener) {
        this.onCompleteListener = onCompleteListener;
    }

    /**
     * Show question dialog with message, expected buttons and call associated listeners
     *
     * @param context       not null!
     * @param message       message body ( not null! )
     * @param positiveText  text positive button
     * @param positiveClick positive click listener
     * @param negativeText  text negative button
     * @param negativeClick negative click listener
     */
    public static void question(Context context, String message,
                                String positiveText, final View.OnClickListener positiveClick,
                                String negativeText, final View.OnClickListener negativeClick) {

        if (message == null || context == null) {
            throw new RuntimeException("QueryMaster.question context and message must be not null");
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        /**
         * positive handler beside
         */
        if (positiveText != null) {
            builder.setPositiveButton(positiveText, null);
        }
        if (negativeText != null) {
            builder.setNegativeButton(negativeText, null);
        }

        builder.setMessage(message);


        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        if (positiveText != null) {
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (positiveClick != null) {
                        positiveClick.onClick(v);
                    }
                    alertDialog.dismiss();
                }
            });
        }
        if (negativeText != null) {
            alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (negativeClick != null) {
                        negativeClick.onClick(v);
                    }
                    alertDialog.dismiss();
                }
            });
        }
    }

    public interface OnCompleteListener {
        public void complete(String serverResponse);

        public void error(int errorCode);
    }

    private void initHandler() {
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (onCompleteListener != null) {
                    if (msg.what == QUERY_MASTER_COMPLETE) {
                        onCompleteListener.complete(serverResponse);
                    }
                    if (msg.what == QUERY_MASTER_ERROR) {
                        onCompleteListener.error(QUERY_MASTER_ERROR);
                    }
                    if (msg.what == QUERY_MASTER_NETWORK_ERROR) {
                        onCompleteListener.error(QUERY_MASTER_NETWORK_ERROR);
                    }
                }
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }

            }
        };
    }

    private boolean isNetworkConnected() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public Context getContext() {
        return context;
    }

    public static AlertDialog alert(Context context, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message);
        builder.setTitle("������");

        builder.setPositiveButton("Ok", null);
        return builder.show();
    }

    public static AlertDialog.Builder alertBuilder(Context context, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message);
        builder.setTitle("������");

        return builder;
    }

    public static void toast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void toast(Context context, int resource) {
        Toast.makeText(context, context.getString(resource),
                Toast.LENGTH_SHORT).show();
    }

    public static boolean isSuccess(JSONObject jsonObject) throws JSONException {
        String status = jsonObject.getString("status");
        return status.equalsIgnoreCase(STATUS_SUCCESS);
    }
}
