package com.example.calendar;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Stock {

    ArrayList<OneStock> stocks = new ArrayList<OneStock>();
    String src = "http://query.yahooapis.com/v1/public/yql?q=select%20%2a%20from%20yahoo.finance.quotes%20where%20symbol%20in%20%28%22SIRI%22%2C%22PBR%22%2C%22NCT%22%2C%22CSCO%22%2C%22SEAS%22%2C%22INTC%22%2C%22BAC%22%2C%22AAPL%22%2C%22AMD%22%2C%22KMI%22%2C%22FB%22%2C%22QQQ%22%2C%22F%22%2C%22JCP%22%2C%22MSFT%22%2C%22VALE%22%2C%22JDSU%22%2C%22GE%22%2C%22CSIQ%22%2C%22M%22%2C%22MU%22%2C%22KING%22%2C%22KATE%22%2C%22PFE%22%2C%22GRPN%22%2C%22RF%22%2C%22PBR-A%22%2C%22ABEV%22%2C%22S%22%2C%22P%22%2C%22YHOO%22%2C%22GFI%22%2C%22TWTR%22%2C%22T%22%2C%22FOXA%22%2C%22EMC%22%2C%22ITUB%22%2C%22RAD%22%2C%22PLUG%22%2C%22GILD%22%2C%22SUNE%22%2C%22EBAY%22%2C%22WFC%22%2C%22BBD%22%2C%22ITMN%22%2C%22TSM%22%2C%22MNKD%22%2C%22ZNGA%22%2C%22CREE%22%2C%22VZ%22%2C%22FCEL%22%2C%22AMAT%22%2C%22CMCSA%22%2C%22CPST%22%2C%22SFM%22%2C%22ARWR%22%2C%22HBAN%22%2C%22RFMD%22%2C%22JD%22%2C%22DNDN%22%2C%22TKMR%22%2C%22GLUU%22%2C%22ARCP%22%2C%22FTR%22%2C%22QCOM%22%2C%22DRYS%22%2C%22TSLA%22%2C%22SPLS%22%2C%22BBRY%22%2C%22MDRX%22%2C%22NVDA%22%2C%22OTIC%22%2C%22AAL%22%2C%22NVAX%22%2C%22LIVE%22%2C%22CTSH%22%2C%22BRCM%22%2C%22SYMC%22%2C%22HERO%22%2C%22TXN%22%2C%22SMH%22%2C%22NGD%22%2C%22INO%22%2C%22OIH%22%2C%22LNG%22%2C%22BTG%22%2C%22PIP%22%2C%22TXMD%22%2C%22GSAT%22%2C%22TPLM%22%2C%22GTE%22%2C%22ANV%22%2C%22NOG%22%2C%22ROX%22%2C%22VTG%22%2C%22PLX%22%2C%22ONVO%22%2C%22NG%22%2C%22CFP%22%2C%22GST%22%2C%22SAND%22%2C%22EOX%22%2C%22NAVB%22%2C%22VSR%22%2C%22SYRG%22%2C%22FAX%22%2C%22SYN%22%2C%22NML%22%2C%22GORO%22%2C%22CEF%22%2C%22LSG%22%2C%22DHY%22%2C%22URG%22%2C%22RIC%22%2C%22DNN%22%2C%22EVV%22%2C%22AMPE%22%2C%22MEA%22%2C%22NNVC%22%2C%22RBY%22%2C%22ISR%22%2C%22ORC%22%2C%22TGB%22%2C%22CUR%22%2C%22GGN%22%2C%22BTX%22%2C%22WYY%22%2C%22SVLC%22%2C%22NRO%22%2C%22LTS%22%2C%22MM%22%2C%22MRK%22%2C%22ORCL%22%2C%22KO%22%2C%22DE%22%2C%22AA%22%2C%22SD%22%2C%22ABBV%22%2C%22AIG%22%2C%22C%22%2C%22GGB%22%2C%22BSX%22%2C%22KMP%22%2C%22XOM%22%2C%22DAL%22%2C%22VALE-P%22%2C%22GM%22%2C%22FCX%22%2C%22PGN%22%2C%22JPM%22%2C%22WAG%22%29%0A%09%09&env=http%3A%2F%2Fdatatables.org%2Falltables.env&format=json";
    OnStocksLoadedListener listener;

    public void setListener(OnStocksLoadedListener listener) {
        this.listener = listener;
    }

    public Stock() {

    }

    public ArrayList<OneStock> getStocksByIndexes(ArrayList<OneStock> stocks,
                                                  ArrayList<Integer> indexes) {
        ArrayList<OneStock> res = new ArrayList<OneStock>();
        ArrayList<String> names = getNamesCompany(stocks);

        try {

            for (int i = 0; i < indexes.size(); i++) {
                String name = names.get(indexes.get(i));
                for (int j = 0; j < stocks.size(); j++) {
                    if (stocks.get(j).getName().equalsIgnoreCase(name))
                        res.add(stocks.get(j));
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
            Log.e("class:" + getClass().toString(), "catch:" + e);
        }
        return res;
    }

    public ArrayList<String> getNamesCompany(ArrayList<OneStock> stocks) {
        ArrayList<String> res = new ArrayList<String>();
        try {

            for (int i = 0; i < stocks.size(); i++) {
                res.add(stocks.get(i).getName());
            }

            Collections.sort(res);
        } catch (Exception e) {
            // TODO: handle exception
            Log.e("class:" + getClass().toString(), "catch:" + e);
        }
        return res;
    }

    public void loadedStocks() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub

                try {
                    JSONParser jParser = new JSONParser();
                    JSONObject jObj = jParser.getJSONFromUrl(src);

                    JSONArray jArray = jObj.getJSONObject("query")
                            .getJSONObject("results").getJSONArray("quote");

                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject j = jArray.getJSONObject(i);

                        if (j.getString("Ask") != null
                                && !j.getString("Ask").equalsIgnoreCase("null")) {

                            stocks.add(new OneStock(j.getString("symbol"), j
                                    .getString("Name"), j.getString("Ask"), j
                                    .getString("Volume"), j
                                    .getString("Change_PercentChange"), j
                                    .getString("Currency"), j
                                    .getString("BookValue")));
                        }
                    }

                } catch (Exception e) {
                    // TODO: handle exception
                    Log.e(getClass().toString()
                            + "line = "
                            + Thread.currentThread().getStackTrace()[2]
                            .getLineNumber(), "catch:" + e);
                }

                listener.OnLoaded();
            }
        }).start();

    }

    public ArrayList<OneStock> getAllStocks() {

        Collections.sort(stocks, new OneStockComparator());
        return stocks;
    }

    public interface OnStocksLoadedListener {
        public void OnLoaded();
    }

    public class OneStockComparator implements Comparator<OneStock> {
        @Override
        public int compare(OneStock o1, OneStock o2) {
            return o1.getName().compareTo(o2.getName());
        }
    }

}
