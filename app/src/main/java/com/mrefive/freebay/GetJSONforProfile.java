
package com.mrefive.freebay;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.mrefive.freebay.OwnOffersDB.JSONtoLocalDB;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by mrefive on 1/7/16.
 */
public class GetJSONforProfile {

    String JSON_STRING;
    String receivedText;

    private Context context;

    SharedPreferences sharedPreferences;

    public GetJSONforProfile(Context context) {
        sharedPreferences = context.getSharedPreferences("com.mrefive.freebay", Context.MODE_PRIVATE);

        this.context = context;
    }

    public void getJSON() {
        System.out.println("-----------------------------------start of function GetJSONforProfile.getJSON");
        new BackgroundtaskGetJSON().execute();
    }

    //propably make own class for this (same as DBInterface
    //gets information json from db
    class BackgroundtaskGetJSON extends AsyncTask<Void,Void,String> {

        String json_url;

        @Override
        protected void onPreExecute() {
            json_url = "http://mrefive.bplaced.net/freeBay/json_get_data.php";
        }

        @Override
        protected String doInBackground(Void... params) {

            try {
                //timer
                double currentTime=System.currentTimeMillis();

                //set up http connection
                URL url = new URL(json_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                System.out.println("-------------------------------http connection :" + httpURLConnection.getResponseCode());
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                //stringbuilder saves all information from db
                StringBuilder stringBuilder = new StringBuilder();

                //request data from db into string
                while ((JSON_STRING = bufferedReader.readLine()) != null) {
                    stringBuilder.append(JSON_STRING + "\n");

                }

                //close http connection
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();

                //return data from db as string

                Log.d("GetJSONforProfile", "Time to retrieve JSON data: " + (System.currentTimeMillis()-currentTime)/1000 + "s" );

                return stringBuilder.toString().trim();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            receivedText = result;
            sharedPreferences.edit().putString("receivedText",result).apply();

            //save data to local database
            JSONtoLocalDB jsoNtoLocalDB = new JSONtoLocalDB(context);
            jsoNtoLocalDB.putJSONtoOwnOfferDB();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

    }

    public String getReceivedText() {
        return receivedText;
    }
}
