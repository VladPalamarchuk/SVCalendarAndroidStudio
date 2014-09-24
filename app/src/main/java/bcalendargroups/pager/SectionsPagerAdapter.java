package bcalendargroups.pager;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
//import android.support.v13.app.FragmentPagerAdapter;
import bcalendargroups.GroupAssignmentActions;
import bcalendargroups.QueryMaster;
import bcalendargroups.fragments.EventFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.calendar.MainActivity;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * A {@link android.support.v13.app.FragmentPagerAdapter} that returns a
 * fragment corresponding to one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

	private Context context;

	public void setEventsList(JSONArray jsonArray) throws JSONException {
		this.eventsList = SingleEvent.get(jsonArray);
	}

	private ArrayList<SingleEvent> eventsList;

	public SectionsPagerAdapter(Context context, FragmentManager fragmentManager) {
		super(fragmentManager);
		this.context = context;
	}

	@Override
	public Fragment getItem(int position) {
		// getItem is called to instantiate the fragment for the given page.
		// Return a PlaceholderFragment (defined as a static inner class below).
		return EventFragment.newInstance(eventsList.get(position));
	}

	@Override
	public int getItemPosition(Object object) {
		// TODO Auto-generated method stub
		return POSITION_NONE;
	}

	@Override
	public int getCount() {
		return eventsList != null ? eventsList.size() : 0;
	}

	public CharSequence getPageTitle(int position) {
		return eventsList.get(position).title;
	}

	private QueryMaster.OnCompleteListener onUpdate = new QueryMaster.OnCompleteListener() {
		@Override
		public void complete(String serverResponse) {
			// QueryMaster.alert(context, serverResponse);

			try {
				JSONObject jsonObject = new JSONObject(serverResponse);
				if (QueryMaster.isSuccess(jsonObject)) {
					JSONArray data = jsonObject.getJSONArray("data");
					eventsList = SingleEvent.get(data);
					//
					// QueryMaster
					// .alert(context,
					// "QueryMaster.OnCompleteListener onUpdate and notify");

					/**
					 * update
					 */
					notifyDataSetChanged();

					((GroupEvents) context).updateTabs();

				} else {
					// QueryMaster.alert(context, "Not data");
				}
			} catch (JSONException e) {
				e.printStackTrace();
				QueryMaster.alert(context,
						QueryMaster.SERVER_RETURN_INVALID_DATA);
			}
		}

		@Override
		public void error(int errorCode) {
			QueryMaster.alert(context, QueryMaster.ERROR_MESSAGE);
		}
	};

	/**
	 * Parse events in current group
	 * 
	 * @param groupId
	 *            - groupId
	 * @throws java.io.UnsupportedEncodingException
	 */
	public void parse(String groupId, QueryMaster.OnCompleteListener listener)
			throws UnsupportedEncodingException {
		/**
		 * {@link #complete(String)} {@link #error(int)}
		 */
		GroupAssignmentActions.getEvents(context, listener, groupId);
	}

	public void update() throws UnsupportedEncodingException {
		if (context instanceof GroupEvents) {
			GroupAssignmentActions.getEvents(context, onUpdate,
					((GroupEvents) context).getGroupId());
		}
	}

	public void update(String groupID) throws UnsupportedEncodingException {

		GroupAssignmentActions.getEvents(context, onUpdate, groupID);
	}

}