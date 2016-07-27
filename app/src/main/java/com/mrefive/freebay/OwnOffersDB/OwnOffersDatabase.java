package com.mrefive.freebay.OwnOffersDB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;

/**
 * Created by mrefive on 1/8/16.
 */
public class OwnOffersDatabase extends SQLiteOpenHelper {

    public static final int database_version = 1;
    private Context context;


    private String CREATE_QUERY = "CREATE TABLE "+ OwnOffersTableInfo.TableInfo.TABLE_NAME+ "("+ OwnOffersTableInfo.TableInfo.UOID+" TEXT,"+ OwnOffersTableInfo.TableInfo.UUID+" TEXT,"+ OwnOffersTableInfo.TableInfo.CATEGORY+" TEXT,"+ OwnOffersTableInfo.TableInfo.TITLE+" TEXT,"+ OwnOffersTableInfo.TableInfo.DESCRIPTION+" TEXT,"+ OwnOffersTableInfo.TableInfo.DATEPUT+" TEXT,"+ OwnOffersTableInfo.TableInfo.DATEEND+" TEXT,"+ OwnOffersTableInfo.TableInfo.LAT+" TEXT,"+ OwnOffersTableInfo.TableInfo.LNG+ " TEXT,"+ OwnOffersTableInfo.TableInfo.IMGNAME+ " TEXT);";


    public OwnOffersDatabase(Context context) {
        super(context, OwnOffersTableInfo.TableInfo.DB_NAME, null, database_version);
        this.context=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_QUERY);
        Log.d("Database operations", "Table own_offers_table created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void putInformation(OwnOffersDatabase ownOffersDatabase, String UOID, String UUID, String category,String title,String description,String dateput,String dateend,String lat,String lng, String imgname) {

        SQLiteDatabase sqLiteDatabase = ownOffersDatabase.getWritableDatabase();

        //content for columns to put into db
        ContentValues contentValues = new ContentValues();

        //choose column and define value to add there
        contentValues.put(OwnOffersTableInfo.TableInfo.UOID, UOID);
        contentValues.put(OwnOffersTableInfo.TableInfo.UUID, UUID);
        contentValues.put(OwnOffersTableInfo.TableInfo.CATEGORY, category);
        contentValues.put(OwnOffersTableInfo.TableInfo.TITLE, title);
        contentValues.put(OwnOffersTableInfo.TableInfo.DESCRIPTION, description);
        contentValues.put(OwnOffersTableInfo.TableInfo.DATEPUT, dateput);
        contentValues.put(OwnOffersTableInfo.TableInfo.DATEEND, dateend);
        contentValues.put(OwnOffersTableInfo.TableInfo.LAT, lat);
        contentValues.put(OwnOffersTableInfo.TableInfo.LNG, lng);
        contentValues.put(OwnOffersTableInfo.TableInfo.IMGNAME, imgname);

        //insert contentValue with sqlite method
        sqLiteDatabase.insert(OwnOffersTableInfo.TableInfo.TABLE_NAME, null, contentValues);
        //Log.d("Database operations", "One row inserted into own_offers_table");
    }

    public void addEntry(OwnOffersDatabase ownOffersDatabase, String data[]){

        SQLiteDatabase sqLiteDatabase = ownOffersDatabase.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(OwnOffersTableInfo.TableInfo.UOID, data[0]);
        contentValues.put(OwnOffersTableInfo.TableInfo.UUID, data[1]);
        contentValues.put(OwnOffersTableInfo.TableInfo.CATEGORY, data[2]);
        contentValues.put(OwnOffersTableInfo.TableInfo.TITLE, data[3]);
        contentValues.put(OwnOffersTableInfo.TableInfo.DESCRIPTION, data[4]);
        contentValues.put(OwnOffersTableInfo.TableInfo.DATEPUT, data[5]);
        contentValues.put(OwnOffersTableInfo.TableInfo.DATEEND, data[6]);
        contentValues.put(OwnOffersTableInfo.TableInfo.LAT, data[7]);
        contentValues.put(OwnOffersTableInfo.TableInfo.LNG, data[8]);
        contentValues.put(OwnOffersTableInfo.TableInfo.IMGNAME, data[9]);

        sqLiteDatabase.insert(OwnOffersTableInfo.TableInfo.TABLE_NAME, null, contentValues);
        Log.d("OwnOffersDatabase", "New Entry in LocalDB created");
    }


    //delete an entry
    public void deleteEntry(OwnOffersDatabase ownOffersDatabase, String UOID) {

        SQLiteDatabase sqLiteDatabase = ownOffersDatabase.getWritableDatabase();

        String whereclause = "UOID =?";
        String[] whereargs = new String[] {UOID};
        sqLiteDatabase.delete(OwnOffersTableInfo.TableInfo.TABLE_NAME, whereclause, whereargs);
    }


    public void deleteTable(OwnOffersDatabase ownOffersDatabase, String tablename) {
        SQLiteDatabase sqLiteDatabase = ownOffersDatabase.getWritableDatabase();
        sqLiteDatabase.delete(tablename, null, null);
    }

    //retrieving data back form database
    public Cursor getInformation(OwnOffersDatabase ownOffersDatabase) {
        SQLiteDatabase sqLiteDatabase = ownOffersDatabase.getReadableDatabase();
        //String[] columns = {OwnOffersTableInfo.TableInfo.TITLE, OwnOffersTableInfo.TableInfo.DESCR};
        String[] columns = {OwnOffersTableInfo.TableInfo.UOID, OwnOffersTableInfo.TableInfo.UUID, OwnOffersTableInfo.TableInfo.CATEGORY, OwnOffersTableInfo.TableInfo.TITLE, OwnOffersTableInfo.TableInfo.DESCRIPTION, OwnOffersTableInfo.TableInfo.DATEPUT, OwnOffersTableInfo.TableInfo.DATEEND, OwnOffersTableInfo.TableInfo.LAT, OwnOffersTableInfo.TableInfo.LNG, OwnOffersTableInfo.TableInfo.IMGNAME};
        Cursor cursor = sqLiteDatabase.query(OwnOffersTableInfo.TableInfo.TABLE_NAME, columns, null, null, null, null, null);

        return cursor;
    }

    //get specific entry
    public Cursor getTitle(OwnOffersDatabase ownOffersDatabase, String user) {
        SQLiteDatabase sqLiteDatabase = ownOffersDatabase.getReadableDatabase();
        String selection = OwnOffersTableInfo.TableInfo.TITLE+" LIKE ?";
        String columns[] = {OwnOffersTableInfo.TableInfo.TITLE};
        String args[] = {user};
        Cursor cursor = sqLiteDatabase.query(OwnOffersTableInfo.TableInfo.TABLE_NAME, columns, selection, args, null, null, null);

        return cursor;

    }

    public boolean doesDatabaseExist(Context context) {
        File dbFile = context.getDatabasePath(OwnOffersTableInfo.TableInfo.DB_NAME);
        Log.d("OwnOffersDatabaseW", "path to db is: " + OwnOffersTableInfo.TableInfo.DB_NAME);
        return dbFile.exists();
    }

    public boolean checklocaldbforentries(OwnOffersDatabase ownOffersDatabase) {
        SQLiteDatabase sqLiteDatabase = ownOffersDatabase.getWritableDatabase();
        String[] columns = {"UOID"};
        Cursor cursor=sqLiteDatabase.query(OwnOffersTableInfo.TableInfo.TABLE_NAME, columns, null,null,null,null,null);
        cursor.moveToFirst();
        if(cursor.getCount() <= 0){
            cursor.close();
            return false;
        }
        cursor.close();
        return true;

    }

}
