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
        supportRequestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);

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


    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    *

    //-------------------------------------------------------------------------------------------------------------------/

    //after API client connected:
    private void handleNewLocation(Location location) {
    }

    @Override
    public void onConnected(Bundle bundle) {
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (location == null) {
            // Blank for a moment...
        }
        else {
            handleNewLocation(location);
            lat=location.getLatitude();
            lng=location.getLongitude();
        };
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    //-------------------------------------------------------------------------------------------------------------------

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

}
