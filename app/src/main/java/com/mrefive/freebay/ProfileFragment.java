package com.mrefive.freebay;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.FragmentActivity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SlidingPaneLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.mrefive.freebay.OwnOffersDB.OwnOffersDatabase;
import com.mrefive.freebay.OwnOffersDB.OwnOffersSync;
import com.mrefive.freebay.OwnOffersDB.OwnOffersSyncImage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ProfileFragment extends Fragment {

    //variables
    private int countOfPosts;

    private TextView showCountOfPosts;
    private LinearLayout linearLayoutProfFrag;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ListView listvieownoffers;
    private ScrollView scrollView;

    //instances
    private SharedPreferences prefs;
    private View view;

    private boolean onresumecalled = false;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //getsharedprefs
        prefs = getContext().getSharedPreferences("com.mrefive.freebay", Context.MODE_PRIVATE);
        countOfPosts= prefs.getInt("countOfPosts", 0);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_profile, container, false);
        linearLayoutProfFrag = (LinearLayout) view.findViewById(R.id.LinLayProfileFrag);
        listvieownoffers = (ListView) view.findViewById(R.id.profileofferlistview);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout_profile);
        //SwipeRefreshLayout.LayoutParams slp = new SwipeRefreshLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        //swipeRefreshLayout.setLayoutParams(slp);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateListView();
                swipeRefreshLayout.setRefreshing(false);
                Log.d("swiperefreshlayout", "SWIPESREFRESH TRIGGERED");
            }
        });

        /*
        swipeRefreshLayout.setColorSchemeColors(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        */
        //TODO SWIPETOREFRESH SET UP LISTVIEW NEW AND ONLY UPDATE CONTENT WITH ADAPTER adapter = new adapter , listview.setAdapter(adapter)...
        //linearLayoutProfFrag.addView(swipeRefreshLayout);

        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
        updateListView();
        //wen das fragment wieder aufgerufen wird hier rein
    }

    @Override
    public void onPause() {
        super.onPause();
        //when fragment.hide hier rein
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getActivity().getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.BLUE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        updateListView();
        syncofferswithserver();
        /*
        if(onresumecalled) {
            updateListView();
        }
        onresumecalled=true;
        */
    }

    @Override
    public void onStop() {
        super.onStop();
        //wenn activity onstop ausgeführt wird
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //mListener = null;
    }

    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(Uri uri);
    }

    public String getTitle() {
        return "Profile";
    }

    private void updateListView() {
        OwnOffersDatabase ownOffersDatabase = new OwnOffersDatabase(getContext());
        if (ownOffersDatabase.checklocaldbforentries(ownOffersDatabase)) {

            OwnOffersListView owndbtlv = new OwnOffersListView(getContext());
            owndbtlv.fillListView(listvieownoffers);
            /*linearLayoutProfFrag.removeAllViews();
            linearLayoutProfFrag.addView(owndbtlv.getListView());
            */
            //swipeRefreshLayout.addView(owndbtlv.getListView());

            owndbtlv.updateListEntry();

            //TODO MOVE ONCLICKLISteNER TO OWNOFFERSLISTVIEWADAPTER AND TRY IF IT WORKS
            owndbtlv.getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Log.d("ProfileFragment", " List item clicked : #" + position + "id: " + id);
                    Bundle bundle = new Bundle();
                    bundle.putInt("listViewPosition", position);

                    Intent intent = new Intent(getContext(), OfferMenuActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });

        } else {
            Toast.makeText(getContext(), "No Offers. Please create one.", Toast.LENGTH_LONG).show();
        }
    }

    public void syncofferswithserver() {
        //add check for internet !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

        //download offers from server
        try {
            OwnOffersSync ownOffersSync = new OwnOffersSync(getContext()) {
                @Override
                protected void onPostExecute(String s) {
                    super.onPostExecute(s);
                    if(!super.imagenames.isEmpty()) {
                        int i=0;
                        while(i<super.imagenames.size()){
                            downloadandsaveimagesAsynctask(super.imagenames.get(i));
                            i++;
                        }
                        updateListView();
                    } else {

                    }
                }
            };
            ownOffersSync.execute(ownOffersSync.createUOIDJSON());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void downloadandsaveimagesAsynctask(String imagename){
        OwnOffersSyncImage ownOffersSyncImage = new OwnOffersSyncImage() {
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                updateListView();
            }
        };
        ownOffersSyncImage.execute(MainActivity.ANDROID_ID, imagename);
    }
}
