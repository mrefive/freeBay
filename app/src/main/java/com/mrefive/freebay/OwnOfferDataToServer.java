package com.mrefive.freebay;

import android.content.Context;
import android.os.AsyncTask;
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
 * Created by mrefive on 5/20/16.
 */
public class OwnOfferDataToServer extends AsyncTask<String, Void, String>{

    Context ctx;
    OwnOfferDataToServer(Context ctx) {
        this.ctx = ctx;

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }


    @Override
    protected String doInBackground(String... params) {

        String post_url= "https://sommer.kdt-hosting.ch/freebay/dataupload.php";

        String UOID = params[0];
        String UUID = params[1];
        String title = params[2];
        String description = params[3];
        String category = params[4];
        String dateput = params[5];
        String dateend = params[6];
        String lat = params[7];
        String lng = params[8];
        String imgname = params[9];

            try {
                //post info
                URL url = new URL(post_url);
                HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url.openConnection();
                httpsURLConnection.setRequestMethod("POST");
                httpsURLConnection.setDoOutput(true);
                httpsURLConnection.setDoInput(true);
                OutputStream OS = httpsURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));
                String data =
                        URLEncoder.encode("UOID", "UTF-8") +"="+ URLEncoder.encode(UOID, "UTF-8")+"&"+
                        URLEncoder.encode("UUID", "UTF-8") +"="+ URLEncoder.encode(UUID, "UTF-8")+"&"+
                        URLEncoder.encode("title", "UTF-8") +"="+ URLEncoder.encode(title, "UTF-8")+"&"+
                        URLEncoder.encode("description", "UTF-8") +"="+ URLEncoder.encode(description, "UTF-8")+"&"+
                        URLEncoder.encode("category", "UTF-8") +"="+ URLEncoder.encode(category, "UTF-8")+"&"+
                        URLEncoder.encode("dateput", "UTF-8") +"="+ URLEncoder.encode(dateput, "UTF-8")+"&"+
                        URLEncoder.encode("dateend", "UTF-8") +"="+ URLEncoder.encode(dateend, "UTF-8")+"&"+
                        URLEncoder.encode("lat", "UTF-8") +"="+ URLEncoder.encode(lat, "UTF-8")+"&"+
                        URLEncoder.encode("lng", "UTF-8") +"="+ URLEncoder.encode(lng, "UTF-8")+"&"+
                        URLEncoder.encode("imgname", "UTF-8") +"="+URLEncoder.encode(imgname, "UTF-8");
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

                return  getScriptResponse;
                // the return goes to onPostExecute-----------------------------

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        return "error uploading - no script response";
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String result) {
        if(result.equals("Post Success...")) {
            Toast.makeText(ctx,result,Toast.LENGTH_LONG).show();

        } else {

        }
    }
}
