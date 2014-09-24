package com.example.calendar;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;
import java.util.Locale;

public class MapsActivyty extends FragmentActivity {

    GoogleMap map;
    Marker marker;
    EditText mapSearchBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_activyty);

        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
                .getMap();

        map.setOnMapClickListener(new OnMapClickListener() {

            @Override
            public void onMapClick(LatLng point) {
                // TODO Auto-generated method stub
                try {

                    marker.remove();

                } catch (Exception e) {
                    // TODO: handle exception
                    Log.e(getClass().toString()
                            + "line = "
                            + Thread.currentThread().getStackTrace()[2]
                            .getLineNumber(), "catch:" + e);
                }

                marker = map.addMarker(new MarkerOptions().position(point)
                        .title(""));
            }
        });

        mapSearchBox = (EditText) findViewById(R.id.search_by_map);
        mapSearchBox
                .setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    public boolean onEditorAction(TextView v, int actionId,
                                                  KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_SEARCH
                                || actionId == EditorInfo.IME_ACTION_DONE
                                || actionId == EditorInfo.IME_ACTION_GO
                                || event.getAction() == KeyEvent.ACTION_DOWN
                                && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {

                            // hide virtual keyboard
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(
                                    mapSearchBox.getWindowToken(), 0);

                            new SearchClicked(mapSearchBox.getText().toString())
                                    .execute();
                            mapSearchBox.setText("",
                                    TextView.BufferType.EDITABLE);
                            return true;
                        }
                        return false;
                    }
                });

    }

    private class SearchClicked extends AsyncTask<Void, Void, Boolean> {
        private String toSearch;
        private Address address;

        public SearchClicked(String toSearch) {
            this.toSearch = toSearch;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {

            try {
                Geocoder geocoder = new Geocoder(getApplicationContext(),
                        Locale.UK);
                List<Address> results = geocoder.getFromLocationName(toSearch,
                        1);

                if (results.size() == 0) {
                    return false;
                }

                address = results.get(0);
                LatLng point = new LatLng((int) (address.getLatitude() * 1E6),
                        (int) (address.getLongitude() * 1E6));
                marker = map.addMarker(new MarkerOptions().position(point)
                        .title(""));


            } catch (Exception e) {
                Log.e("", "Something went wrong: ", e);
                return false;
            }
            return true;
        }
    }
}
