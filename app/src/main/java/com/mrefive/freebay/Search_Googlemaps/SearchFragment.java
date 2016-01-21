package com.mrefive.freebay.Search_Googlemaps;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mrefive.freebay.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;

/**
 * Created by mrefive on 11/4/15.
 */
public class SearchFragment extends Fragment implements OnMapReadyCallback {

    //variables
    double lat,lng;

    private String UUID, UOID, category, title, descr, timeput, dateput, timedue, datedue, lati, lngi;

    private String offerJSONobject;

    private ArrayList<String> ReceivedOffers;

    //com.google.android.gms.maps.MapFragment mapFragment;
    SupportMapFragment mapFragment;
    FragmentManager myFragmentManager;

    GetMapMarkers getMapMarkers;

    //Bundle argsSearchfragment;
    //Intent in;
    //gps tracker for latlng?

    //android.support.v4.app.Fragment mapFragment;



    //private static final String TAG_MYMAPFRAGMENT = "TAG_MyMapFragment";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        lat = getArguments().getDouble("lat");
        lng = getArguments().getDouble("lng");
        //FragmentManager myFragmentManager = getContext().getSupportFragmentManager();
        myFragmentManager = getFragmentManager();
        //argsSearchfragment = this.getArguments();
        //System.out.println("------------------------------------SearchFragment" + argsSearchfragment.getDouble("lat"));
        //System.out.println("------------------------------------SearchFragment" + getArguments().getDouble("lat"));
        System.out.println("------------------------------------SearchFragment" + lng);
        //mapFragment= (MapFragment) myFragmentManager.findFragmentByTag(TAG_MYMAPFRAGMENT);
        //mapFragment= (SupportMapFragment) myFragmentManager.findFragmentByTag(TAG_MYMAPFRAGMENT);

        /*
        if(mapFragment == null) {
            mapFragment = SupportMapFragment.newInstance();
            FragmentTransaction fragmentTransaction = myFragmentManager.beginTransaction();
            fragmentTransaction.add(android.R.id.content, mapFragment, TAG_MYMAPFRAGMENT);
            fragmentTransaction.commit();
        }
        */
        if(mapFragment == null) {
            mapFragment = SupportMapFragment.newInstance();
            //mapFragment.setArguments(argsSearchfragment);
            FragmentTransaction fragmentTransaction = myFragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container2, mapFragment);
            fragmentTransaction.commit();
        }
        mapFragment.getMapAsync(this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_mapsfragment, container, false);

        return view;
    }
    @Override
    public void onResume() {
        super.onResume();
        System.out.println("SearchFragment onResume");
        //myFragmentManager.beginTransaction().show(mapFragment).commit();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onMapReady(GoogleMap map) {
        System.out.println("------------------------SearchFragment onMapready");



        //right upper corner, location layer activated
        map.setMyLocationEnabled(true);

        //get latlng from mainActivity
        //lat=this.getArguments().getDouble("lat");
        //lng=this.getArguments().getDouble("lng");
        //lat=argsSearchfragment.getLong("lat");
        //lng=argsSearchfragmechronized between threadnt.getLong("lng");
        //lat=gpsTracker.getLatitude();
        //lng=gpsTracker.getLongitude();

        Log.d("SearchFragment", "Location is: "+Double.toString(lat));
        LatLng myLocation = new LatLng(lat,lng);
        //map.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 10));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 11));

        //asynctask
        SetMarkers setMarkers = new SetMarkers();
        setMarkers.execute(map);

        System.out.println("------------------------SearchFragment onMapready Ends");

    }



    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    public class SetMarkers extends AsyncTask<GoogleMap, Void, GoogleMap> {

        @Override
        protected GoogleMap doInBackground(GoogleMap... maps) {
            try {
                getMapMarkers = new GetMapMarkers(getContext());
                offerJSONobject = getMapMarkers.getOffersInRechJSONstring(lat,lng);
                Log.d("SearchFragment","offerJSONstring: " + offerJSONobject);

                return maps[0];

            } catch (Exception e) {

            }

            return null;
        }

        @Override
        protected void onPostExecute(GoogleMap maps) {

            try {
                JSONObject jsonObject = new JSONObject(offerJSONobject);
                JSONArray jsonArray = jsonObject.getJSONArray("server_response");

                int count = 0;

                while(count<jsonArray.length()) {
                    JSONObject JO = jsonArray.getJSONObject(count);
                    UUID = JO.getString("UUID");
                    UOID = JO.getString("UOID");
                    category= JO.getString("category");
                    title = JO.getString("title");
                    descr = JO.getString("descr");
                    timeput= JO.getString("timeput");
                    dateput = JO.getString("dateput");
                    datedue = JO.getString("datedue");
                    lati = JO.getString("lat");
                    lngi = JO.getString("lng");

                    //create Markers
                    maps.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(lati), Double.parseDouble(lngi))).snippet(title).title(title));
                    Log.d("SearchFragment", "Marker created " + title);

                    count++;




                }
                Log.d("SearchFragment", jsonArray.length() + " marker created!!");

                //Log.d("JSONtoLocalDB",jsonArray.length() + "rows into local DB inserted");

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }



}
