package com.mrefive.freebay;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.mrefive.freebay.OwnOffersDB.OwnOffersDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by mrefive on 1/7/16.
 */
public class JSONtoListView {

    private Context context;

    //for listview
    String JSON_STRING;
    String json_string;
    String receivedText;
    JSONObject jsonObject;
    JSONArray jsonArray;
    OwnOffersAdapter ownOffersAdapter;
    ListView listView;

    SharedPreferences sharedPreferences;


    public JSONtoListView(Context context) {
        System.out.println("--------------------------------------------------------start of JSONtoListView");
        this.context = context;

    }

    public void createListView() {
        //getsharedprefs
        sharedPreferences = context.getSharedPreferences("com.mrefive.freebay", Context.MODE_PRIVATE);
        receivedText=sharedPreferences.getString("receivedText", "");

        System.out.println(receivedText);


        ownOffersAdapter = new OwnOffersAdapter(context, R.layout.profile_list_view_layout);

        listView = new ListView(context);
        RelativeLayout.LayoutParams lpListView = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        listView.setLayoutParams(lpListView);
        listView.setClickable(true);



        if(listView==null){
            System.out.println("listview == null ffs huuuuuuuuuuuuuuuuuuuuuuuuuuuuuueresääähn");
        }

        if(ownOffersAdapter==null){
            System.out.println("ownOfferAdapter == null ffs huuuuuuuuuuuuuuuuuuuuuuuuuuuuuueresääähn");
        }

        listView.setAdapter(ownOffersAdapter);


        //TEST listview from local database: working fine ----------------------000000000

        OwnOffersDatabase ownOffersDatabase = new OwnOffersDatabase(context);
        Cursor cursor = ownOffersDatabase.getInformation(ownOffersDatabase);

        //set curson on first row
        cursor.moveToFirst();

        String title = "";
        String descr = "";

        do{
            title=cursor.getString(3);
            descr=cursor.getString(4);

            OwnOffers ownOffers = new OwnOffers(title, descr);
            ownOffersAdapter.add(ownOffers);

        } while(cursor.moveToNext());

        //TEST listview from local database: working fine ----------------------0000000000


        /* ---------------original getting JSON data into listview! ----------------------
        try {
            jsonObject = new JSONObject(receivedText);
            jsonArray = jsonObject.getJSONArray("server_response");

            int count = 0;
            String name, descr;

            while(count<jsonArray.length()) {
                JSONObject JO = jsonArray.getJSONObject(count);
                name = JO.getString("title");
                descr = JO.getString("descr");
                OwnOffers ownOffers = new OwnOffers(name, descr);
                ownOffersAdapter.add(ownOffers);

                count++;

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
        -----------------------------------------------------------------------------------------
        */

    }

    public void setReceivedText(String receivedText) {
        this.receivedText = receivedText;
    }

    public ListView getListView() {
        return listView;
    }
}
