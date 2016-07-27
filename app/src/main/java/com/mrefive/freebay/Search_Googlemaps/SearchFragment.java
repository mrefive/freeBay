package com.mrefive.freebay.Search_Googlemaps;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
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
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
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
import java.util.Iterator;

/**
 * Created by mrefive on 11/4/15.
 */
public class SearchFragment extends Fragment implements OnMapReadyCallback {

    //variables
    private double lat,lng;

    private String UUID, UOID, category, title, descr, timeput, dateput, timedue, datedue, lati, lngi;

    private String offerJSONobject;

    private GoogleMap googleMap;

    private ArrayList<String> ReceivedOffers;

    private ArrayList<OfferObject> tempoffers;
    private ArrayList<Marker> markeroffers;

    //com.google.android.gms.maps.MapFragment mapFragment;
    SupportMapFragment mapFragment;
    FragmentManager myFragmentManager;

    GetMapMarkers getMapMarkers;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tempoffers = new ArrayList<>();
        markeroffers = new ArrayList<>();

        myFragmentManager = getFragmentManager();

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

        System.out.println("SearchFragment onResume");
        Log.d("SearchFragment", "SearchFragment onResume");

        //right upper corner, location layer activated
        googleMap = map;
        googleMap.setMyLocationEnabled(true);

        //use UISettings for further buttons etc
        googleMap.getUiSettings().setZoomControlsEnabled(true);

        googleMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                Log.d("SearchFragment","CAMERACHANGELISTENER FTW");
                Log.d("SearchFragment", "ZOOM = "+googleMap.getCameraPosition().zoom);

                googleMap.clear();
                int count = 0;
                while(count<markeroffers.size()) {
                    googleMap.addMarker(getMarkerOptions(tempoffers.get(count), (long) googleMap.getCameraPosition().zoom));
                    count++;
                }
            }
        });

        //map.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 11));
        GetOwnLocation getOwnLocation = new GetOwnLocation() {
            @Override
            protected void onPostExecute(Location location) {
                lat = location.getLatitude();
                lng = location.getLongitude();
                //googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), 11));
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), 11));
                updateMap();
            }
        };
        getOwnLocation.execute();

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    private void updateMap() {
        GetMapOffers getMapOffers = new GetMapOffers(getContext()){
            @Override
            public void onPostExecute(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray jsonArray = jsonObject.getJSONArray("server_response");

                    int count = 0;
                    while(count<jsonArray.length()) {
                        boolean alreadyadded = false;
                        OfferObject offerObject = new OfferObject();
                        JSONObject JO = jsonArray.getJSONObject(count);

                        int helper;
                        for(helper=0; helper<tempoffers.size();helper++) {
                            if(tempoffers!=null) {
                                if(JO.getString("UOID").equals(tempoffers.get(helper).getUOID())){
                                    alreadyadded = true;
                                }
                            }
                        }

                        if(!alreadyadded) {
                            offerObject.setUOID(JO.getString("UOID"));
                            offerObject.setTitle(JO.getString("title"));
                            offerObject.setDescr(JO.getString("description"));
                            offerObject.setCategory(JO.getString("category"));
                            offerObject.setDateend(JO.getString("dateput"));
                            offerObject.setDateend(JO.getString("dateend"));
                            offerObject.setLat(JO.getString("lat"));
                            offerObject.setLng(JO.getString("lng"));
                            offerObject.setImagename(JO.getString("imagename"));
                            tempoffers.add(offerObject);
                            //googleMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(offerObject.getOfferthumbnail())));

                            Bitmap markericonbig = BitmapFactory.decodeResource(getResources(),R.drawable.beachicon);
                            //googleMap.getCameraPosition().zoom;
                            Bitmap markericon = Bitmap.createScaledBitmap(markericonbig, markericonbig.getWidth()/4, markericonbig.getHeight()/4,false);

                            Marker marker = googleMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(offerObject.getLat()),Double.parseDouble(offerObject.getLng()))).icon(BitmapDescriptorFactory.fromBitmap(markericon)));
                            markeroffers.add(marker);
                        }

                        //create Markers
                        //maps.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(lati), Double.parseDouble(lngi))).snippet(title).title(title));
                        Log.d("SearchFragment", "Marker created " + title);

                        count++;
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        Log.d("SearchFragment","lat and lng = "+lat+" "+lng);
        getMapOffers.execute(Double.toString(lat),Double.toString(lng));
    }

    private MarkerOptions getMarkerOptions(OfferObject offerObject,long zoom){

        double scaler;
        if(zoom>15){
            scaler = (float)1.5;
        } else if(zoom>6) {
            scaler=((((float)-1)*((float)5/(float)9))*(float)zoom)+(float)9.9;
        } else {
        //} else if(zoom<=5){
            scaler=(float)6.5;
        }
        Log.d("SearchFragment","SCALER = "+scaler);
        Bitmap markericontemp = BitmapFactory.decodeResource(getResources(),R.drawable.beachicon);
        MarkerOptions markerOptions= new MarkerOptions().position(new LatLng(Double.parseDouble(offerObject.getLat()), Double.parseDouble(offerObject.getLng()))).title(offerObject.getTitle()).icon(BitmapDescriptorFactory.fromBitmap(Bitmap.createScaledBitmap(markericontemp, (int)(markericontemp.getWidth()/scaler), (int)(markericontemp.getHeight()/scaler), false)));

        return markerOptions;
    }

    private Location getLastBestLocation() {

        Log.d("SearchFragment","GETLASTBESTLOCATION EXECUTED");
        LocationManager locationManager = (LocationManager)
                getActivity().getSystemService(Context.LOCATION_SERVICE);
        Location locationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        //Log.d("SearchFragment", "LAtITUDE GPS & LONGITUDE GPS "+locationGPS.getLatitude()+" & "+locationGPS.getLongitude());
        Location locationNet = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        //Log.d("SearchFragment", "LAtITUDE NET & LONGITUDE NET "+locationNet.getLatitude()+" & "+locationNet.getLongitude());
        Location bestLocation;

        long GPSLocationTime = 0;
        if (null != locationGPS) { GPSLocationTime = locationGPS.getTime(); }

        long NetLocationTime = 0;

        if (null != locationNet) {
            NetLocationTime = locationNet.getTime();
        }

        if ( 0 < GPSLocationTime - NetLocationTime ) {
            bestLocation = locationGPS;
        }
        else {
            bestLocation= locationNet;
        }
        return bestLocation;
    }

    private class GetOwnLocation extends AsyncTask<Void, Void, Location> {
        @Override
        protected Location doInBackground(Void... params) {
            Location location=null;

            while(location==null) {
                location=getLastBestLocation();
            }
            return location;
        }
    }

    public String getTitle() {
        return "Search for offers";
    }


}
