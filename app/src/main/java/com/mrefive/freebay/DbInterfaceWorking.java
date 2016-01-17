package com.mrefive.freebay;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

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
 * Created by mrefive on 11/17/15.
 */


//not needed anymore!!!!!!!!!!

public class DbInterfaceWorking extends AsyncTask<String, Void, String> {

    Context ctx;
    DbInterfaceWorking(Context ctx) {
        this.ctx = ctx;

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }


    @Override
    protected String doInBackground(String... params) {

        String post_url= "http://mrefive.bplaced.net/freeBay/register.php";
        String retrieve_url= "http://mrefive.bplaced.net/freeBay/retrieve.php";
        String method = params[0];
        if(method.equals("post")) {
            String name = params[1];
            String descriptions = params[2];

            try {
                //post info
                URL url = new URL(post_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream OS = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));
                String data =
                        URLEncoder.encode("title", "UTF-8") +"="+ URLEncoder.encode(name, "UTF-8")+"&"+
                        URLEncoder.encode("descr", "UTF-8") +"="+URLEncoder.encode(descriptions, "UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                OS.close();

                //------------------input of info ?
                InputStream IS = httpURLConnection.getInputStream();
                IS.close();

                return  "Post Success...";
                // the return goes to onPostExecute-----------------------------

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (method.equals("retrieve")){
            try {
                URL url = new URL(retrieve_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                String response = "";
                String line = "";
                while ((line = bufferedReader.readLine())!=null) {
                    response+= line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();

                return response;


            }catch (MalformedURLException e) {
                e.printStackTrace(); 
            } catch (IOException e) {
                e.printStackTrace();
            }
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
            Toast.makeText(ctx,result,Toast.LENGTH_LONG).show();

        } else {

        }
    }

    public void submitOfferToDatabase() {
        System.out.println("DbInterface: submit data to database ------------------------------------");


    }



    /*
    public void getDataFromDB() {
        String result = "";
        InputStream isr = null;
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://10.0.0.61/classic/getAllLectures.php");
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            isr = entity.getContent();
        } catch (Exception e) {
            Log.e("log_tag", "Error in http connection" + e.toString());
        }
        try{
            BufferedReader reader = new BufferedReader(new InputStreamReader(isr,"iso-8859-1"),8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line=reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            isr.close();
            result=sb.toString();
        }
        catch(Exception e){
            Log.e("log_tag","Error converting result"+e.toString());
        }

        try {
            String s = "";
            JSONArray jArray = new JSONArray(result);

            for(int i=0; i<jArray.length(); i++){
                JSONObject json = jArray.getJSONObject(i);
                s = s+
                        "idLecturer : "+json.getInt("idLecturer")+"\n"+
                        "username : "+json.getString("username")+"\n"+
                        "pass : "+json.getString("pass")+"\n"+
                        "fullName :"+json.getString("fullName")+"\n\n";
            }

            //resultView.setText(s);

        }

        catch (Exception e){
//to do handle exception
            Log.e("Log_tag","Error Parsing Data"+e.toString());
        }
    }
    */

}
