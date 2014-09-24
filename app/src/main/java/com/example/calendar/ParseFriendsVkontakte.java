package com.example.calendar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ParseFriendsVkontakte {

    public String src;
    boolean isVideos = false;
    ArrayList<OneFriend> arrayOneFriend = new ArrayList<OneFriend>();
    int type;

    public ParseFriendsVkontakte(String src, int type) {
        this.src = src;
        this.type = type;

    }

    public ArrayList<OneFriend> Parse() {

        JSONParser jParser = new JSONParser();
        JSONObject jObject = jParser.getJSONFromUrl(src);


        JSONArray jArray = new JSONArray();
        try {
            jArray = jObject.getJSONArray("response");

            for (int i = 0; i < jArray.length(); i++) {

                JSONObject jLocalObject = jArray.getJSONObject(i);

                String first_name = jLocalObject.getString("first_name");
                String last_name = jLocalObject.getString("last_name");

                String bdate = "";
                String mobile_phone = "";
                String phone = "";
                String location = "";

                try {
                    bdate = jLocalObject.getString("bdate");
                    mobile_phone = jLocalObject.getString("mobile_phone");

                    String[] symbs = {"1", "2", "3", "4", "5", "6", "7", "8",
                            "9", "0"};
                    for (int j = 0; j < mobile_phone.length(); j++) {
                        String symb = mobile_phone.substring(j, j + 1);

                        for (int n = 0; n < symbs.length; n++) {
                            if (symb.equalsIgnoreCase(symbs[n]))
                                phone += symb;
                        }
                    }

                    int id_city = jLocalObject.getInt("city");

                    JSONObject jObj = jParser
                            .getJSONFromUrl("https://api.vk.com/method/getCities?cids="
                                    + id_city);
                    String city = jObj.getJSONArray("response")
                            .getJSONObject(0).getString("name");

                    location = ""
                            + MainActivity.getInstance().getIdCityByName(
                            MainActivity.getInstance().CITIES, city);


                } catch (Exception e) {
                    // TODO: handle exception

                }

                if (phone.length() >= 10 && phone.length() <= 12) {
                    if (Integer.parseInt((String) phone.subSequence(0, 1)) == 3)
                        phone = "+" + phone;

                }

                String photo = "";

                try {
                    photo = jLocalObject.getString("photo_200_orig");
                } catch (Exception e) {

                }


                arrayOneFriend.add(new OneFriend(0, first_name, last_name,
                        bdate, phone, type, location, photo));

            }
        } catch (Exception e) {
            e.printStackTrace();

        }

        return arrayOneFriend;

    }

}
