package com.mrefive.freebay;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.StrictMode;
import android.provider.Settings;
import android.support.design.widget.NavigationView;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.mrefive.freebay.OwnOffersDB.ServerToJSON;
import com.mrefive.freebay.Search_Googlemaps.SearchFragment;

public class MainActivity extends AppCompatActivity {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    SharedPreferences sharedPreferences;

    GPSTracker gpsTracker;

    //drawer toolbar
    Toolbar toolbar;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;

    NavigationView navigationView;

    //fragments
    Bundle argsSearchfragment;
    OfferFragment offerFragment = new OfferFragment();
    SearchFragment searchFragment = new SearchFragment();
    ProfileFragment profileFragment = new ProfileFragment();

    //views
    RelativeLayout mainContainer;

    //variables
    private GoogleApiClient mGoogleApiClient;
    private double lat,lng;

    private boolean stateOfferFragment=false;
    private boolean stateProfileFragment=false;
    private boolean stateSearchFragment=false;

    private CharSequence mTitle;

    public static String ANDROID_ID;
    public static int MAXOWNOFFERS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //window options
        //getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        //supportRequestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        //strictmode entfernt!!!!!!!!


        sharedPreferences = this.getSharedPreferences("com.mrefive.freebay", this.MODE_PRIVATE);

        //initialize container
        mainContainer = (RelativeLayout) findViewById(R.id.mainContainer);

        //set up action baar as toolbar with button
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(Color.parseColor("#B40C1CD4"));
        //toolbar.getBackground().setAlpha(80);

        setSupportActionBar(toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);


        //getLatLng and check if GPS is enabled
        final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );
        if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                            startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                            dialog.cancel();
                        }
                    });
            final AlertDialog alert = builder.create();
            alert.show();
        }

        GPSTracker gpsTracker = new GPSTracker(this);
        argsSearchfragment = new Bundle();
        System.out.println("--------------------------------- lat= " + gpsTracker.getLatitude() + "lng = " + gpsTracker.getLongitude());
        argsSearchfragment.putDouble("lat", gpsTracker.getLatitude());
        argsSearchfragment.putDouble("lng", gpsTracker.getLongitude());
        searchFragment.setArguments(argsSearchfragment);

        //sset static vars
        ANDROID_ID = Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        Log.d("Mainactivity", "Android ID = "+ ANDROID_ID);
        MAXOWNOFFERS = 20;

        /*only check online DB when opening profilefragment
        //retrieve JSON from Server
        ServerToJSON serverToJSON = new ServerToJSON(this);
        serverToJSON.execute(ANDROID_ID);
        */

        getSupportFragmentManager().beginTransaction().add(R.id.mainContainer, offerFragment).commit();
        getSupportActionBar().setTitle("Create an offer");


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                switch (menuItem.getItemId()) {
                    case R.id.offer_id:
                        showHideFragment(offerFragment, searchFragment, profileFragment);
                        getSupportActionBar().setTitle(R.string.title_section1);
                        menuItem.setChecked(true);
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.search_id:
                        showHideFragment(searchFragment, offerFragment, profileFragment);
                        getSupportActionBar().setTitle(R.string.title_section2);
                        menuItem.setChecked(true);
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.profile_id:
                        showHideFragment(profileFragment, searchFragment, offerFragment);
                        getSupportActionBar().setTitle(R.string.title_section3);
                        menuItem.setChecked(true);
                        drawerLayout.closeDrawers();
                        break;
                }
                return false;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        //check for internet connection
        if (!new CheckInternetConnection(this).isNetworkConnected()) {
            LayoutInflater inflater = getLayoutInflater();

            View layout = inflater.inflate(R.layout.toastnoconnection,
                    (ViewGroup) findViewById(R.id.toastnoconnection_id));

            // set a message
            //TextView text = (TextView) layout.findViewById(R.id.text);
            //text.setText("Button is clicked!");

            // Toast...
            Toast toast = new Toast(getApplicationContext());
            //toast.setGravity(Gravity.FILL, 0, 0);
            toast.setGravity(Gravity.FILL,0,0);
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setView(layout);
            toast.show();
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }



    public void showHideFragment(Fragment fragmentShow, Fragment fragmentHide1, Fragment fragmentHide2){

        if (fragmentHide1.isVisible()) {
            getSupportFragmentManager().beginTransaction().setCustomAnimations(android.R.anim.fade_in,
                    android.R.anim.fade_out).hide(fragmentHide1).commit();
        }
        if (fragmentHide2.isVisible()) {
            getSupportFragmentManager().beginTransaction().setCustomAnimations(android.R.anim.fade_in,
                    android.R.anim.fade_out).hide(fragmentHide2).commit();
        }


        if (!fragmentShow.isAdded()) {
            getSupportFragmentManager().beginTransaction().setCustomAnimations(android.R.anim.fade_in,
                    android.R.anim.fade_out).add(R.id.mainContainer, fragmentShow).addToBackStack("oh").commit();
        } else if (fragmentShow.isHidden()) {
            getSupportFragmentManager().beginTransaction().setCustomAnimations(android.R.anim.fade_in,
                    android.R.anim.fade_out).show(fragmentShow).addToBackStack("oh").commit();
        }
    }


}
