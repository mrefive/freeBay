package com.mrefive.freebay;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
    private Button buttonMinusOne;
    private LinearLayout linearLayoutProfFrag;
    private ListView listViewOffers;

    //JSON
    private String receivedText;

    private JSONtoListView jsoNtoListView;

    //instances
    private OnFragmentInteractionListener mListener;
    private SharedPreferences prefs;
    private View view;

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

        receivedText=prefs.getString("receivedText", "");

        System.out.println(receivedText);

        jsoNtoListView = new JSONtoListView(getContext());
        jsoNtoListView.createListView();

        linearLayoutProfFrag.addView(jsoNtoListView.getListView());


        //make listView klickable
        jsoNtoListView.getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println("CLICK LIST ITEM position: " + position + "id: " + id);


                Bundle bundle = new Bundle();
                bundle.putInt("listViewPosition", position);

                Intent intent = new Intent(getContext(), OfferMenuActivity.class);

                intent.putExtras(bundle);
                startActivity(intent);

                //OfferMenuFragment offerMenuFragment = new OfferMenuFragment();
                //linearLayoutProfFrag.removeAllViews();
                //getActivity().getSupportFragmentManager().beginTransaction().add(R.id.LinLayProfileFrag,offerMenuFragment).addToBackStack(null).commit();
                //setViewLayout(R.layout.offer_menu);


            }
        });

        Log.d("Profile Fragment", "Create View executed");

        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
        //wen das fragment wieder aufgerufen wird hier rein
    }

    @Override
    public void onPause() {
        super.onPause();
        //when fragment.hide hier rein
    }

    @Override
    public void onResume() {
        super.onResume();
        jsoNtoListView = new JSONtoListView(getContext());
        jsoNtoListView.createListView();

        linearLayoutProfFrag.removeAllViews();
        linearLayoutProfFrag.addView(jsoNtoListView.getListView());

        //make listView klickable
        jsoNtoListView.getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println("CLICK LIST ITEM position: " + position + "id: " + id);

                Bundle bundle = new Bundle();
                bundle.putInt("listViewPosition", position);

                Intent intent = new Intent(getContext(), OfferMenuActivity.class);

                intent.putExtras(bundle);
                startActivity(intent);

            }
        });

        Log.d("Profile Fragment", "OnResume executed");

    }

    @Override
    public void onStop() {
        super.onStop();
        //wenn activity onstop ausgeführt wird
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(Uri uri);
    }

    public void openMenu(View view) {
        /*
        FrameLayout offerMenu = new FrameLayout(getContext());
        FrameLayout.LayoutParams lpOfferMenu = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        offerMenu.setLayoutParams(lpOfferMenu);
        */

        setViewLayout(R.layout.offer_menu);


    }

    private void setViewLayout(int id){
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(id, null);
        ViewGroup rootView = (ViewGroup) getView();
        rootView.removeAllViews();
        rootView.addView(view);

    }

}
