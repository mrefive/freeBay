package com.mrefive.freebay.OwnOffersDB;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

import com.mrefive.freebay.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by mrefive on 5/24/16.
 */
public class OwnOffersSync extends AsyncTask<JSONObject, Void, String> {

    private Context context;
    public ArrayList<String> imagenames = new ArrayList<>();

    public OwnOffersSync(Context context) {
        this.context = context;
    }

    @Override
    protected String doInBackground(JSONObject... params) {

        String post_url= "https://sommer.kdt-hosting.ch/freebay/dbsyncclient.php";

        String jsondata = params[0].toString();

        try {
            URL url = new URL(post_url);
            HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url.openConnection();
            httpsURLConnection.setRequestMethod("POST");
            httpsURLConnection.setDoOutput(true);
            httpsURLConnection.setDoInput(true);
            OutputStream OS = httpsURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));
            String data =
                    URLEncoder.encode("DATA", "UTF-8") +"="+ URLEncoder.encode(jsondata, "UTF-8");
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
        JSONObject jsonObject =null;
        String data[] = new String[10];
        try {
            jsonObject = new JSONObject(s);

            JSONArray jsonArray = jsonObject.getJSONArray("server_response");

            for(int i=0; i<jsonArray.length();i++) {
                JSONObject jsontmp = jsonArray.getJSONObject(i);
                data[0] = jsontmp.getString("UOID");
                data[1] = MainActivity.ANDROID_ID;
                data[2] = jsontmp.getString("category");
                data[3] = jsontmp.getString("title");
                data[4] = jsontmp.getString("description");
                data[5] = jsontmp.getString("dateput");
                data[6] = jsontmp.getString("dateend");
                data[7] = jsontmp.getString("lat");
                data[8] = jsontmp.getString("lng");
                data[9] = jsontmp.getString("imagename");
                imagenames.add(jsontmp.getString("imagename"));
                OwnOffersDatabase ownOffersDatabase = new OwnOffersDatabase(context);
                ownOffersDatabase.addEntry(ownOffersDatabase, data);
            }
        } catch (JSONException e) {
            //e.printStackTrace();
            Log.d("OwnOffersSync","No new Entries to localdb");
        }
    }

    public JSONObject createUOIDJSON() throws JSONException {

        JSONObject jsonString = new JSONObject();
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("UUID", MainActivity.ANDROID_ID);

        //get UOIDs
        OwnOffersDatabase ownOffersDatabase = new OwnOffersDatabase(context);
        SQLiteDatabase sqLiteDatabase = ownOffersDatabase.getWritableDatabase();

        String[] columns = {"UOID"};
        Cursor cursor=sqLiteDatabase.query(OwnOffersTableInfo.TableInfo.TABLE_NAME, columns, null,null,null,null,null);
        cursor.moveToFirst();

        ArrayList<String> UOIDs = new ArrayList<>();

        while(!cursor.isAfterLast()) {
            UOIDs.add(cursor.getString(0));
            cursor.moveToNext();
        }

        for(int i=0; i<UOIDs.size();i++) {
            String helper = UOIDs.get(i);
            jsonObject.put(Integer.toString(i),helper);
        }

        jsonString.put("UOID",jsonObject);
        System.out.println("OwnOffersSync: JSON STRING "+jsonString);

        return jsonString;
    }
}
