package com.mrefive.freebay;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;

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
 * Created by mrefive on 5/12/16.
 */
public class CreateUOID extends AsyncTask<Void, Void, String> {

    Context ctx;
    Activity caller;

    String getScriptResponse = "";
    String get_url = "https://sommer.kdt-hosting.ch/freebay/createnewUOID.php";


    CreateUOID(Activity caller) {
        this.caller = caller;
        this.ctx = ctx;
        Log.d("CreateUOID", "constructor call");
    }


    @Override
    protected String doInBackground(Void... params) {

        Log.d("CreateUOID", "DEBUG: do in background before try");

        try {

            Log.d("CreateUOID", "Asking for new UOID...");

            //get info
            URL url = new URL(get_url);
            HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url.openConnection();

            httpsURLConnection.setConnectTimeout(20000);
            httpsURLConnection.setReadTimeout(20000);
            //httpsURLConnection.setRequestMethod("GET");
            httpsURLConnection.setDoInput(true);

            InputStream inputStream = httpsURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder = new StringBuilder();

            //stringbuilder saves all information from server

            //request data from db into string
            while ((getScriptResponse = bufferedReader.readLine()) != null) {
                stringBuilder.append(getScriptResponse + "\n");
            }

            //close http connection
            bufferedReader.close();
            inputStream.close();

            httpsURLConnection.disconnect();

            getScriptResponse = stringBuilder.toString().trim();

            if(getScriptResponse!=null) {
                Log.d("CreateeUOID", "New UOID Created: " +getScriptResponse);
            } else {
                Log.d("CreateeUOID", "Error creating UOID");
            }

        } catch (MalformedURLException e) {
            Log.d("ImageToServer","MalformedURLException");
            e.printStackTrace();
        } catch (IOException e) {
            Log.d("ImageToServer","IOException");
            e.printStackTrace();
        }
        return getScriptResponse;
    }


    @Override
    protected void onPostExecute(String s) {
        //override in fragment
    }


}