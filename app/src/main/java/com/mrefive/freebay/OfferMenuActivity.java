package com.mrefive.freebay;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.CharacterPickerDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mrefive.freebay.OwnOffersDB.DeleteOfferImg;
import com.mrefive.freebay.OwnOffersDB.DeleteOwnOfferFromServer;
import com.mrefive.freebay.OwnOffersDB.OwnOffersDatabase;

import java.io.File;

public class OfferMenuActivity extends Activity {

    private Context context;

    private Bundle bundle;
    private int listViewPosition;

    private String titleString, descrString, UOIDString, UUIDString, dateendstring, imagename;
    private Bitmap offerimage;

    private TextView title, descr, dateend;
    private ImageView imagecontainer;
    private Button deleteentry;

    private OwnOffersDatabase ownOffersDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_menu);

        context=this;

        bundle = getIntent().getExtras();
        listViewPosition = bundle.getInt("listViewPosition");

        ownOffersDatabase = new OwnOffersDatabase(context);

        title = (TextView) findViewById(R.id.offermenutitle);
        descr = (TextView) findViewById(R.id.offermenudescr);
        dateend = (TextView) findViewById(R.id.offermenudateend);
        imagecontainer = (ImageView) findViewById(R.id.offerimagecontainer);

        deleteentry = (Button) findViewById(R.id.offermenubuttondeleteentry);


        //retrieve info from db
        Cursor cursor = ownOffersDatabase.getInformation(ownOffersDatabase);
        cursor.moveToPosition(listViewPosition);

        UOIDString = cursor.getString(0);
        UUIDString = cursor.getString(1);
        titleString = cursor.getString(3);
        descrString = cursor.getString(4);
        dateendstring = cursor.getString(6);
        imagename = cursor.getString(9);

        //fill in the content
        title.setText(titleString);
        descr.setText(descrString);
        dateend.setText(dateendstring);

        File image = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "FreeBay" + File.separator + imagename);
        offerimage = BitmapFactory.decodeFile(image.getAbsolutePath());
        imagecontainer.setImageBitmap(offerimage);



        deleteentry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //define dialog
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                //Yes button clicked

                                //delete from server
                                DeleteOwnOfferFromServer deleteOwnOfferFromServer = new DeleteOwnOfferFromServer(context){

                                    @Override
                                    public void onPostExecute(String result) {
                                        Log.d("DeleteOwnOfferServer", "Script response: " + result);

                                        if(result.equals("Offer deleted")){
                                            Log.d("DeleteOwnOfferServer", "if clause executed");
                                            //delte from local DB
                                            ownOffersDatabase.deleteEntry(ownOffersDatabase, UOIDString);
                                            DeleteOfferImg deleteOfferImg = new DeleteOfferImg(getApplicationContext());
                                            deleteOfferImg.deleteFile(UOIDString);

                                            //decrement countofposts
                                            SharedPreferences preferences;
                                            preferences = getApplicationContext().getSharedPreferences("com.mrefive.freebay", Context.MODE_PRIVATE);
                                            Integer countOfPosts= preferences.getInt("countOfPosts", 0);
                                            countOfPosts--;
                                            preferences.edit().putInt("countOfPosts",countOfPosts).apply();
                                            Log.d("OfferMenuActivity", "countofposts= "+countOfPosts);
                                        }
                                    }
                                };
                                deleteOwnOfferFromServer.execute(UOIDString);

                                finish();

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Delete "+titleString+"?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();



            }
        });


    }
}
