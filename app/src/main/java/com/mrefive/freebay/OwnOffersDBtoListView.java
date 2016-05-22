package com.mrefive.freebay;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.mrefive.freebay.OwnOffersDB.OwnOffersDatabase;

/**
 * Created by mrefive on 5/19/16.
 */
public class OwnOffersDBtoListView {
    private Context context;

    //for listview
    OwnOffersAdapter ownOffersAdapter;
    ListView listView;

    SharedPreferences sharedPreferences;


    public OwnOffersDBtoListView(Context context) {
        System.out.println("--------------------------------------------------------start of OwnOffersDBtoListView");
        this.context = context;

    }

    public void createListView() {

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

    }

    public ListView getListView() {
        return listView;
    }
}

