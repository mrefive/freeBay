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

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by mrefive on 1/7/16.
 */
public class DeleteOwnOfferFromServer extends AsyncTask <String, Void, String> {


    private String urllink;
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
    public String doInBackground(String... params) {

        UOID = params[0];

        urllink = "https://sommer.kdt-hosting.ch/freebay/deleteOffer.php";

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
                    URLEncoder.encode("UOID", "UTF-8") +"="+ URLEncoder.encode(UOID, "UTF-8");
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
