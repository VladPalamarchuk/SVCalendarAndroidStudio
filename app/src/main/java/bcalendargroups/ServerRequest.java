package bcalendargroups;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import org.apache.http.entity.mime.MultipartEntity;
import org.json.JSONArray;
import org.json.JSONObject;

public class ServerRequest {

	public static final String DOMAIN = "http://calendar.studiovision.com.ua/main.php";

	public static final String USER_CREATE_GROUP = DOMAIN;

	public static final String SEARCH_USERS = DOMAIN;
	public static final String ADD_USER_TO_GROUP = DOMAIN;

}
