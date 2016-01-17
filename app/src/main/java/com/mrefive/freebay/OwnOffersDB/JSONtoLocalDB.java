package com.mrefive.freebay.OwnOffersDB;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.mrefive.freebay.OwnOffers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by mrefive on 1/12/16.
 */
public class JSONtoLocalDB {

    private JSONObject jsonObject;
    private JSONArray jsonArray;
    private SharedPreferences sharedPreferences;

    private Context context;

    private String receivedText;

    public JSONtoLocalDB(Context context) {
        sharedPreferences = context.getSharedPreferences("com.mrefive.freebay", Context.MODE_PRIVATE);
        receivedText = sharedPreferences.getString("receivedText", "");
        this.context = context;
    }

    public void putJSONtoOwnOfferDB() {

        //create databse
        OwnOffersDatabase ownOffersDatabase = new OwnOffersDatabase(context);
        ownOffersDatabase.deleteTable(ownOffersDatabase, OwnOffersTableInfo.TableInfo.TABLE_NAME);


        //fill databse with rows
        try {
            jsonObject = new JSONObject(receivedText);
            jsonArray = jsonObject.getJSONArray("server_response");

            int count = 0;
            String UUID, UOID, category, title, descr, timeput, dateput, timedue, datedue, lat, lng;

            while(count<jsonArray.length()) {
                JSONObject JO = jsonArray.getJSONObject(count);
                UUID = JO.getString("UUID");
                UOID = JO.getString("UOID");
                category= JO.getString("category");
                title = JO.getString("title");
                descr = JO.getString("descr");
                timeput= JO.getString("timeput");
                dateput = JO.getString("dateput");
                timedue = JO.getString("timedue");
                datedue = JO.getString("datedue");
                lat = JO.getString("lat");
                lng = JO.getString("lng");


                ownOffersDatabase.putInformation(ownOffersDatabase, UUID, UOID, category, title, descr, timeput, dateput, timedue, datedue, lat, lng);

                count++;

            }

            Log.d("JSONtoLocalDB",jsonArray.length() + "rows into local DB inserted");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
