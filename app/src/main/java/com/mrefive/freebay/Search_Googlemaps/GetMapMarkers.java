package com.mrefive.freebay.Search_Googlemaps;

import android.content.Context;
import android.util.Log;

import com.mrefive.freebay.MainActivity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by mrefive on 1/16/16.
 */
public class GetMapMarkers {

    private Context context;

    public GetMapMarkers(Context context) {
        this.context = context;
    }


    public String getOffersInRechJSONstring(double lat, double lng) {

        String offersInReach;
        String JSON_STRING = "";
        String json_url = "http://mrefive.bplaced.net/freeBay/json_get_offers_in_reach.php";


        try {
            //timer
            double currentTime = System.currentTimeMillis();

            //post info
            URL url = new URL(json_url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);


            OutputStream OS = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));
            String data =
                    URLEncoder.encode("UUID", "UTF-8") + "=" + URLEncoder.encode(MainActivity.ANDROID_ID, "UTF-8")+"&"+
                    URLEncoder.encode("lat", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(lat), "UTF-8")+"&"+
                    URLEncoder.encode("lng", "UTF-8") +"="+URLEncoder.encode(String.valueOf(lng), "UTF-8");

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


            Log.d("GetMapMarkers", "Time to retrieve offersInReach: " + (System.currentTimeMillis() - currentTime) / 1000 + "s");
            Log.d("GetMapMarkers", "JSON String: " + stringBuilder.toString().trim());



            return stringBuilder.toString().trim();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;

    }
}
