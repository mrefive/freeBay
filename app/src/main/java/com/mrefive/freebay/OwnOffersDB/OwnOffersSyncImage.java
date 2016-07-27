package com.mrefive.freebay.OwnOffersDB;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;

import com.mrefive.freebay.MainActivity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Random;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by mrefive on 6/2/16.
 */
public class OwnOffersSyncImage extends AsyncTask<String, Void, String>{

    private String serverimagename;
    @Override
    protected String doInBackground(String... params) {
        String post_url= "https://sommer.kdt-hosting.ch/freebay/dbsyncclient.php";

        String UUID = params[0];
        String imagename = params[1];
        serverimagename = imagename;

        try {
            URL url = new URL(post_url);
            HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url.openConnection();
            httpsURLConnection.setRequestMethod("POST");
            httpsURLConnection.setDoOutput(true);
            httpsURLConnection.setDoInput(true);
            OutputStream OS = httpsURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));
            String data =
                    URLEncoder.encode("UUID", "UTF-8") +"="+ URLEncoder.encode(UUID, "UTF-8")+
                    URLEncoder.encode("imagename", "UTF-8") +"="+ URLEncoder.encode(imagename, "UTF-8");
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
        return "error uploading jsonsyncstring - no script response";
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if(!s.equals("Error finding image")) {
            byte[] decodedString = Base64.decode(s, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

            try {
                FileOutputStream out = new FileOutputStream(getOutputMediaFile(serverimagename));
                decodedByte.compress(Bitmap.CompressFormat.JPEG, 100, out);
                out.flush();
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static File getOutputMediaFile(String filename){
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),"FreeBay");
        if (!mediaStorageDir.exists()) {
            if(!mediaStorageDir.mkdirs()) {
                Log.d("OwnOffersSyncImage", "Failed to create dir...");
                return null;
            }
        }
        File mediaFile;
        mediaFile= new File(mediaStorageDir.getPath() + File.separator + filename);

        return mediaFile;
    }
}
