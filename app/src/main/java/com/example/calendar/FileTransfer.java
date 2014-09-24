package com.example.calendar;

import android.content.Context;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

public class FileTransfer {

    private ArrayList<NameValuePair> queryBody = null;

    public FileTransfer() {

    }

    public FileTransfer(ArrayList<NameValuePair> queryBody) {
        this.queryBody = queryBody;
    }

    public void uploadFile(Context context, String user_id, String filePath) {
        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost postRequest = new HttpPost(
                    "http://calendar.studiovision.com.ua/calen/upload.php");
            MultipartEntity reqEntity = new MultipartEntity(
                    HttpMultipartMode.BROWSER_COMPATIBLE);

            if (filePath != null && !filePath.isEmpty()) {

                FileBody filebodyVideo = new FileBody(new File(filePath),
                        "application/octet-stream");
                reqEntity.addPart("uploadedfile", filebodyVideo);
            }
            if (queryBody != null) {
                for (NameValuePair iterable_element : queryBody) {
                    reqEntity.addPart(iterable_element.getName(),
                            new StringBody(iterable_element.getValue()));
                }
            }
            postRequest.setEntity(reqEntity);

            HttpResponse response = httpClient.execute(postRequest);

            String resp = MainActivity.getInstance().convertStreamToString(
                    response);

            String src = new JSONObject(resp).getString("baseurl");
            MainActivity.getInstance().SendIdByFile(user_id, src);

        } catch (Exception e) {
            Log.e("", "upload file error: " + e.toString());
        }
    }
}
/**
 * php code to receive file <?php
 *
 * $email = $_POST['email']; $message = $_POST['message'];
 *
 * $target_path = "uploads/";
 *
 * / Add the original filename to our target path. Result is
 * "uploads/filename.extension" / $target_path = $target_path .
 * basename($_FILES['uploadedfile']['name']);
 *
 * ------------------------------
 * if(move_uploaded_file($_FILES['uploadedfile']['tmp_name'], $target_path)) {
 * echo "The file ". basename( $_FILES['uploadedfile']['name']).
 * " has been uploaded"; } -------------------------------- else{ echo
 * "There was an error <span id='IL_AD4' class='IL_AD'>uploading</span> the file, please try again!"
 * ; echo "filename: " . basename( $_FILES['uploadedfile']['name']); echo
 * "target_path: " .$target_path; } ?>
 */
