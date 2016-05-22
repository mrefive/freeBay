package com.mrefive.freebay;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by mrefive on 2/11/16.
 */
public class ImageToServer extends AsyncTask<String, Void, String> {

    Context ctx;
    ImageToServer(Context ctx) {
        this.ctx = ctx;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }


    @Override
    protected String doInBackground(String... imagedata) {

        //String post_url= "test.kdt-hosting.ch/free/index.php";
        String post_url= "https://sommer.kdt-hosting.ch/freebay/imageupload.php";

        String imagesString = imagedata[0];
        String imageName = imagedata[1];

        try {
            Log.d("ImageToServer", "uploading image to server...");

            //post info
            URL url = new URL(post_url);
            HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url.openConnection();

            httpsURLConnection.setConnectTimeout(20000);
            httpsURLConnection.setReadTimeout(20000);
            httpsURLConnection.setRequestMethod("POST");
            httpsURLConnection.setDoOutput(true);
            httpsURLConnection.setDoInput(true);

            //httpURLConnection.setDoInput(true);
            OutputStream OS = httpsURLConnection.getOutputStream();

            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));
            String data =
                    URLEncoder.encode("base64", "UTF-8") +"="+ URLEncoder.encode(imagesString, "UTF-8")+"&"+
                            URLEncoder.encode("ImageName", "UTF-8") +"="+ URLEncoder.encode(imageName, "UTF-8");

            //Log.d("ImageToServer", "Submited data in UTF-8 = " + data);

            bufferedWriter.write(data);
            bufferedWriter.flush();
            bufferedWriter.close();
            OS.close();

            InputStream inputStream = httpsURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder = new StringBuilder();


            //stringbuilder saves all information from server

            String getScriptResponse = "";

            //request data from db into string
            while ((getScriptResponse = bufferedReader.readLine()) != null) {
                stringBuilder.append(getScriptResponse + "\n");
            }

            //close http connection
            bufferedReader.close();
            inputStream.close();


            httpsURLConnection.disconnect();

            getScriptResponse = stringBuilder.toString().trim();


            Log.d("ImageToServer", "Server script echo = " + getScriptResponse);

            return  "Post Success...";

        } catch (MalformedURLException e) {
            Log.d("ImageToServer","MalformedURLException");
            e.printStackTrace();
        } catch (IOException e) {
            Log.d("ImageToServer","IOException");
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String result) {
        if(result.equals("Post Success...")) {
            Toast.makeText(ctx, ctx.getResources().getString(R.string.ResponseToast), Toast.LENGTH_LONG).show();
        } else {
        }
    }

}