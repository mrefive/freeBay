package com.mrefive.freebay.OwnOffersDB;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.mrefive.freebay.MainActivity;
import com.mrefive.freebay.OwnOffersDB.JSONtoLocalDB;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by mrefive on 1/7/16.
 */
public class ServerToJSON extends AsyncTask <String, Void, String> {

    String JSON_STRING;
    String receivedText;

    String json_url;
    String UUID;

    private Context context;

    SharedPreferences sharedPreferences;

    public ServerToJSON(Context context) {
        sharedPreferences = context.getSharedPreferences("com.mrefive.freebay", Context.MODE_PRIVATE);

        json_url = "http://mrefive.bplaced.net/freeBay/json_get_data.php";

        this.context = context;
    }

    @Override
    public void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    public String doInBackground(String... params) {

        //UUID = params[0];
        UUID = params[0];
        Log.d("ServerToJSON", "UUID = " +UUID);

        try {
            //timer
            double currentTime=System.currentTimeMillis();

            //post info
            URL url = new URL(json_url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

            //neuf
            httpURLConnection.setConnectTimeout(2000);
            httpURLConnection.setReadTimeout(2000);

            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);

            OutputStream OS = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));
            String data =
                    URLEncoder.encode("UUID", "UTF-8") +"="+ URLEncoder.encode(UUID, "UTF-8");
            bufferedWriter.write(data);
            bufferedWriter.flush();
            bufferedWriter.close();

            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            StringBuilder stringBuilder = new StringBuilder();


            //stringbuilder saves all information from db


            //request data from db into string
            while ((JSON_STRING = bufferedReader.readLine()) != null) {
                stringBuilder.append(JSON_STRING + "\n");
            }

            //close http connection
            bufferedReader.close();
            inputStream.close();


            OS.close();

            httpURLConnection.disconnect();

            //return data from db as string

            Log.d("GetJSONforProfile", "Time to retrieve JSON data: " + (System.currentTimeMillis()-currentTime)/1000 + "s" );
            Log.d("GetJSONforProfile", "JSON String: " +stringBuilder.toString().trim() );


            return stringBuilder.toString().trim();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    public void onPostExecute(String result) {

        receivedText = result;
        sharedPreferences.edit().putString("receivedText",result).apply();

        //save data to local database
        JSONtoLocalDB jsoNtoLocalDB = new JSONtoLocalDB(context);
        jsoNtoLocalDB.putJSONtoOwnOfferDB();
    }

}
