package com.mrefive.freebay.OwnOffersDB;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

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
public class DeleteOwnOfferFromServer extends AsyncTask <String, Void, Void> {


    private String json_url;
    private String UUID;
    private String UOID;

    private Context context;

    SharedPreferences sharedPreferences;

    public DeleteOwnOfferFromServer(Context context) {
        sharedPreferences = context.getSharedPreferences("com.mrefive.freebay", Context.MODE_PRIVATE);


        this.context = context;
    }

    @Override
    public void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    public Void doInBackground(String... params) {

        //UUID = params[0];
        UUID = params[0];
        UOID = params[1];

        json_url = "http://mrefive.bplaced.net/freeBay/deleteOwnOffer.php";


        try {
            //timer
            double currentTime=System.currentTimeMillis();

            //post info
            URL url = new URL(json_url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            OutputStream OS = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));
            String data =
                    URLEncoder.encode("UUID", "UTF-8") +"="+ URLEncoder.encode(UUID, "UTF-8")+"&"+
                    URLEncoder.encode("UOID", "UTF-8") +"="+ URLEncoder.encode(UOID, "UTF-8");
            bufferedWriter.write(data);
            bufferedWriter.flush();
            bufferedWriter.close();

            Log.d("DeleteOwnOfferFromSever", "Encoded String: " + data);

            OS.close();

            httpURLConnection.disconnect();

            //return data from db as string

            Log.d("DeleteOwnOfferFromSever", "Own offer with UOID: " + (System.currentTimeMillis()-currentTime)/1000 + "s successfully deleted!" );


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
    public void onPostExecute(Void v) {

        Toast.makeText(context,"Offer successfully deleted!", Toast.LENGTH_LONG).show();


    }


}
