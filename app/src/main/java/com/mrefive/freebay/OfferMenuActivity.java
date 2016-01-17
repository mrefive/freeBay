package com.mrefive.freebay;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.CharacterPickerDialog;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.mrefive.freebay.OwnOffersDB.OwnOffersDatabase;

public class OfferMenuActivity extends Activity {

    private Context context;

    private Bundle bundle;
    private int listViewPosition;

    private String titleString, descrString, UOIDString;

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

        UOIDString = cursor.getString(1);
        titleString = cursor.getString(3);
        descrString = cursor.getString(4);



        title.setText(titleString);
        descr.setText(descrString);


        deleteentry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ownOffersDatabase.deleteEntry(ownOffersDatabase, UOIDString);

                Toast.makeText(context,"Entry removed", Toast.LENGTH_LONG);
                //end menuactivity
                finish();

            }
        });


    }
}
