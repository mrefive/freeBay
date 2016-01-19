package com.mrefive.freebay;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.CharacterPickerDialog;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.mrefive.freebay.OwnOffersDB.DeleteOwnOfferFromServer;
import com.mrefive.freebay.OwnOffersDB.OwnOffersDatabase;

public class OfferMenuActivity extends Activity {

    private Context context;

    private Bundle bundle;
    private int listViewPosition;

    private String titleString, descrString, UOIDString, UUIDString;

    private TextView title, descr;
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
        deleteentry = (Button) findViewById(R.id.offermenubuttondeleteentry);

        //retrieve info from db
        Cursor cursor = ownOffersDatabase.getInformation(ownOffersDatabase);
        cursor.moveToPosition(listViewPosition);

        UUIDString = cursor.getString(0);
        UOIDString = cursor.getString(1);
        titleString = cursor.getString(3);
        descrString = cursor.getString(4);



        title.setText(titleString);
        descr.setText(descrString);


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
                                //delte from local DB
                                ownOffersDatabase.deleteEntry(ownOffersDatabase, UOIDString);

                                //delete from server

                                DeleteOwnOfferFromServer deleteOwnOfferFromServer = new DeleteOwnOfferFromServer(context);
                                deleteOwnOfferFromServer.execute(UUIDString, UOIDString);


                                //Toast.makeText(context,"Entry removed", Toast.LENGTH_LONG).show();
                                //end menuactivity
                                finish();
                                //break;

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
