package com.mrefive.freebay;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.mrefive.freebay.OwnOffersDB.OwnOffersDatabase;

import java.util.List;

/**
 * Created by mrefive on 6/9/16.
 */
public class OwnOffersListView {

    private Context context;

    //for listview
    public OwnOffersListViewAdapter ownOffersAdapter;
    private ListView listView;

    SharedPreferences sharedPreferences;


    public OwnOffersListView(Context context) {
        this.context = context;
        ownOffersAdapter = new OwnOffersListViewAdapter(context, R.layout.profile_list_view_layout);

    }

    public void fillListView(ListView offerslist) {


        listView = offerslist;

        listView.setAdapter(ownOffersAdapter);

        View footerView = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.navigation_bar_height, null, false);
        listView.addFooterView(footerView);

        OwnOffersDatabase ownOffersDatabase = new OwnOffersDatabase(context);

        if(ownOffersDatabase.doesDatabaseExist(context)) {
            Cursor cursor = ownOffersDatabase.getInformation(ownOffersDatabase);
            cursor.moveToFirst();
            String title = "";
            String descr = "";
            String dateend = "";
            String imagepath = "";

            do {
                title = cursor.getString(3);
                descr = cursor.getString(4);
                dateend = cursor.getString(6);
                imagepath = cursor.getString(9);

                OwnOffers ownOffers = new OwnOffers(title, descr, dateend, imagepath);
                ownOffersAdapter.add(ownOffers);

            } while (cursor.moveToNext());

        }

        //ownOffersAdapter.notifyDataSetChanged();
        ownOffersDatabase.close();

        //setonclicklistener

        /*
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("ProfileFragment", " List item clicked : #" + position + "id: " + id);
                Bundle bundle = new Bundle();
                bundle.putInt("listViewPosition", position);

                Intent intent = new Intent(context, OfferMenuActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        */
    }

    public void updateListEntry() {
        ownOffersAdapter.notifyDataSetChanged();
    }

    public ListView getListView() {
        return listView;
    }
}
