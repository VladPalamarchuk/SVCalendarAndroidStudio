package com.example.calendar;//package com.example.calendar;
//
//import java.lang.reflect.Array;
//import java.util.ArrayList;
//
//import org.apache.http.HttpEntity;
//import org.apache.http.HttpResponse;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.impl.client.DefaultHttpClient;
//import org.json.JSONArray;
//import org.json.JSONObject;
//
//import android.util.Log;
//
//public class Currency {
//
//	public Currency() {
//	}
//
//	public ArrayList<OneCurrency> getCurrencies() {
//		ArrayList<OneCurrency> res = new ArrayList<OneCurrency>();
//		try {
//
//			String src = "http://bank-ua.com/export/exchange_rate_cash.json";
//
//			DefaultHttpClient httpClient = new DefaultHttpClient();
//			HttpPost httpPost = new HttpPost(src);
//			HttpResponse httpResponse = httpClient.execute(httpPost);
//			String response = MainActivity.getInstance().convertStreamToString(
//					httpResponse);
//
//			JSONArray jArray = new JSONArray(response); 
//   
//			for (int i = 0; i < jArray.length(); i++) {
//
//				res.add(new OneCurrency(jArray.getJSONObject(i).getString(
//						"bankName"), jArray.getJSONObject(i).getString("date"),
//				 		jArray.getJSONObject(i).getString("rateBuy"), jArray
//								.getJSONObject(i).getString("rateSale"), jArray
//								.getJSONObject(i).getString("codeAlpha")));
//
//			}
//
//			return res;
//		} catch (Exception e) {
//			// TODO: handle exception
//			return res;
//		}
//	}
//}
