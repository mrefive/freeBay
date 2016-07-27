package com.mrefive.freebay.Search_Googlemaps;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.mrefive.freebay.MainActivity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by mrefive on 6/14/16.
 */
public class GetMapOffers extends AsyncTask<String, Void, String> {


    private String urllink;
    private String UUID;
    private String lat, lng;

    private Context context;

    SharedPreferences sharedPreferences;

    public GetMapOffers(Context context) {
        this.context = context;
    }

    @Override
    public String doInBackground(String... params) {

        lat = params[0];
        lng = params[1];

        urllink = "https://sommer.kdt-hosting.ch/freebay/getoffersinreach.php";

        try {
            //timer
            double currentTime=System.currentTimeMillis();

            //post info
            URL url = new URL(urllink);
            HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url.openConnection();

            httpsURLConnection.setRequestMethod("POST");
            httpsURLConnection.setDoOutput(true);
            httpsURLConnection.setDoInput(true);

            OutputStream OS = httpsURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));
            String data =
                    URLEncoder.encode("UUID", "UTF-8") + "=" + URLEncoder.encode(MainActivity.ANDROID_ID, "UTF-8")+"&"+
                    URLEncoder.encode("lat", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(lat), "UTF-8")+"&"+
                    URLEncoder.encode("lng", "UTF-8") +"="+URLEncoder.encode(String.valueOf(lng), "UTF-8");
            bufferedWriter.write(data);
            bufferedWriter.flush();
            bufferedWriter.close();
            OS.close();

            //get answer
            InputStream inputStream = httpsURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder = new StringBuilder();

            String getScriptResponse = "";

            while ((getScriptResponse = bufferedReader.readLine()) != null) {
                stringBuilder.append(getScriptResponse + "\n");
            }

            //close http connection
            bufferedReader.close();
            inputStream.close();
            httpsURLConnection.disconnect();

            getScriptResponse = stringBuilder.toString().trim();

            Log.d("Delete_own_offer_server", "URL connection response code:" + httpsURLConnection.getResponseCode());

            httpsURLConnection.disconnect();

            return getScriptResponse;

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

    }
}
