package com.mrefive.freebay;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.mrefive.freebay.OwnOffersDB.ServerToJSON;
import com.mrefive.freebay.Search_Googlemaps.SearchFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    SharedPreferences sharedPreferences;
    private NavigationDrawerFragment mNavigationDrawerFragment;
    //
    GPSTracker gpsTracker;

    GetJSONforProfile getJSONforProfile;

    //fragments
    Bundle argsSearchfragment;
    OfferFragment offerFragment2 = new OfferFragment();
    SearchFragment searchFragment = new SearchFragment();
    ProfileFragment profileFragment = new ProfileFragment();

    //variables
    private GoogleApiClient mGoogleApiClient;
    private double lat,lng;

    private boolean stateOfferFragment=false;
    private boolean stateProfileFragment=false;
    private boolean stateSearchFragment=false;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    public static String ANDROID_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //window options
        //getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        //supportRequestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);


        sharedPreferences = this.getSharedPreferences("com.mrefive.freebay", this.MODE_PRIVATE);




        //getLatLng
        gpsTracker = new GPSTracker(this);
        argsSearchfragment = new Bundle();
        System.out.println("--------------------------------- lat= " + gpsTracker.getLatitude() + "lng = " + gpsTracker.getLongitude());
        argsSearchfragment.putDouble("lat", gpsTracker.getLatitude());
        argsSearchfragment.putDouble("lng", gpsTracker.getLongitude());
        searchFragment.setArguments(argsSearchfragment);


        /*
        //get JSON
        getJSONforProfile = new GetJSONforProfile(this);
        getJSONforProfile.getJSON();

       */


        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        //getActionBar().setStackedBackgroundDrawable(new ColorDrawable(Color.parseColor("#55000000")));


        //save Android ID
        ANDROID_ID = Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID); ;
        Log.d("Mainactivity", "Android ID = "+ ANDROID_ID);

        //retrieve JSON from Server
        ServerToJSON serverToJSON = new ServerToJSON(this);
        serverToJSON.execute(ANDROID_ID);

    }


    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments

        //set tmpFragment
        android.support.v4.app.Fragment tmpFragment = null;



        switch(position) {
            case 0:
                System.out.println("switch case o");
                //tmpFragment = new OfferFragment2();
                //next step: commit/hide/show !! <--------------------------------------------------------------------------!!!!!!
                if(stateSearchFragment) {
                    getSupportFragmentManager().beginTransaction().hide(searchFragment).commit();
                }
                if(stateProfileFragment) {
                    getSupportFragmentManager().beginTransaction().hide(profileFragment).commit();
                }
                if(stateOfferFragment) {
                    getSupportFragmentManager().beginTransaction().show(offerFragment2).commit();
                } else {
                    getSupportFragmentManager().beginTransaction().add(R.id.container, offerFragment2).commit();
                    stateOfferFragment=true;
                }
                break;
            case 1:
                System.out.println("switch case 1");

                if(stateOfferFragment) {
                    getSupportFragmentManager().beginTransaction().hide(offerFragment2).commit();
                }
                if(stateProfileFragment) {
                    getSupportFragmentManager().beginTransaction().hide(profileFragment).commit();
                }
                if(stateSearchFragment) {
                    getSupportFragmentManager().beginTransaction().show(searchFragment).commit();
                } else {
                    getSupportFragmentManager().beginTransaction().add(R.id.container, searchFragment).commit();
                    stateSearchFragment=true;
                }
                break;
            case 2:
                System.out.println("switch case 2");
                if(stateSearchFragment) {
                    getSupportFragmentManager().beginTransaction().hide(searchFragment).commit();
                }
                if(stateOfferFragment) {
                    getSupportFragmentManager().beginTransaction().hide(offerFragment2).commit();
                }
                if(stateProfileFragment) {
                    getSupportFragmentManager().beginTransaction().show(profileFragment).commit();
                } else {
                    if(sharedPreferences.getString("receivedText", null)!=null) {
                        getSupportFragmentManager().beginTransaction().add(R.id.container, profileFragment).commit();
                        stateProfileFragment=true;

                        getSupportActionBar().setTitle("Profile");
                    } else {
                        Toast.makeText(this, "Your data is not ready yet..", Toast.LENGTH_LONG);
                    }
                }
                break;
        }

    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }



}
