package com.mrefive.freebay.OwnOffersDB;

import android.app.ActionBar;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by mrefive on 1/8/16.
 */
public class OwnOffersDatabase extends SQLiteOpenHelper {

    public static final int database_version = 1;


    private String CREATE_QUERY_OLD = "CREATE TABLE "+ OwnOffersTableInfo.TableInfo.TABLE_NAME+ "("+ OwnOffersTableInfo.TableInfo.TITLE+" TEXT,"+ OwnOffersTableInfo.TableInfo.DESCR+" TEXT);";
    private String CREATE_QUERY = "CREATE TABLE "+ OwnOffersTableInfo.TableInfo.TABLE_NAME+ "("+ OwnOffersTableInfo.TableInfo.UUID+" TEXT,"+ OwnOffersTableInfo.TableInfo.UOID+" TEXT,"+ OwnOffersTableInfo.TableInfo.CATEGORY+" TEXT,"+ OwnOffersTableInfo.TableInfo.TITLE+" TEXT,"+ OwnOffersTableInfo.TableInfo.DESCR+" TEXT,"+ OwnOffersTableInfo.TableInfo.TIMEPUT+" TEXT,"+ OwnOffersTableInfo.TableInfo.DATEPUT+" TEXT,"+ OwnOffersTableInfo.TableInfo.TIMEDUE+" TEXT,"+ OwnOffersTableInfo.TableInfo.DATEDUE+" TEXT,"+ OwnOffersTableInfo.TableInfo.LAT+" TEXT,"+ OwnOffersTableInfo.TableInfo.LNG+" TEXT);";


    public OwnOffersDatabase(Context context) {
        super(context, OwnOffersTableInfo.TableInfo.DB_NAME, null, database_version);
        Log.d("Database operations", "Database own_offers_local_db created");
        Log.d("OwnOffersDatabase", "Table create Query: " + CREATE_QUERY);
        Log.d("OwnOffersDatabase", "Table create Query: " + CREATE_QUERY_OLD);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_QUERY);
        Log.d("Database operations", "Table own_offers_table created");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void putInformation(OwnOffersDatabase ownOffersDatabase, String UUID, String UOID, String category,String title,String descr,String timeput,String dateput,String timedue,String datedue,String lat,String lng) {

        SQLiteDatabase sqLiteDatabase = ownOffersDatabase.getWritableDatabase();

        //content for columns to put into db
        ContentValues contentValues = new ContentValues();

        //choose column and define value to add there
        contentValues.put(OwnOffersTableInfo.TableInfo.UUID, UUID);
        contentValues.put(OwnOffersTableInfo.TableInfo.UOID, UOID);
        contentValues.put(OwnOffersTableInfo.TableInfo.CATEGORY, category);
        contentValues.put(OwnOffersTableInfo.TableInfo.TITLE, title);
        contentValues.put(OwnOffersTableInfo.TableInfo.DESCR, descr);
        contentValues.put(OwnOffersTableInfo.TableInfo.TIMEPUT, timeput);
        contentValues.put(OwnOffersTableInfo.TableInfo.DATEPUT, dateput);
        contentValues.put(OwnOffersTableInfo.TableInfo.TIMEDUE, timedue);
        contentValues.put(OwnOffersTableInfo.TableInfo.DATEDUE, datedue);
        contentValues.put(OwnOffersTableInfo.TableInfo.LAT, lat);
        contentValues.put(OwnOffersTableInfo.TableInfo.LNG, lng);

        //insert contentValue with sqlite method
        sqLiteDatabase.insert(OwnOffersTableInfo.TableInfo.TABLE_NAME, null, contentValues);
        //Log.d("Database operations", "One row inserted into own_offers_table");
    }

    //delete an entry
    public void deleteEntry(OwnOffersDatabase ownOffersDatabase, String UOID) {

        SQLiteDatabase sqLiteDatabase = ownOffersDatabase.getWritableDatabase();

        /*
        String selection = OwnOffersTableInfo.TableInfo.UUID+" LIKE ? AND "+ OwnOffersTableInfo.TableInfo.UOID+" LIKE ? AND "+ OwnOffersTableInfo.TableInfo.CATEGORY+" LIKE ? AND "+ OwnOffersTableInfo.TableInfo.TITLE+" LIKE ? AND "+ OwnOffersTableInfo.TableInfo.DESCR+" LIKE ? AND "+ OwnOffersTableInfo.TableInfo.TIMEPUT+" LIKE ? AND "+ OwnOffersTableInfo.TableInfo.DATEPUT+" LIKE ? AND "+ OwnOffersTableInfo.TableInfo.TIMEDUE+" LIKE ? AND "+ OwnOffersTableInfo.TableInfo.DATEDUE+" LIKE ? AND "+ OwnOffersTableInfo.TableInfo.LAT+" LIKE ? AND "+ OwnOffersTableInfo.TableInfo.LNG+" LIKE ?";
        String args[] = {UOID};
        //sqLiteDatabase.delete(OwnOffersTableInfo.TableInfo.TABLE_NAME, selection, args);
        sqLiteDatabase.delete(OwnOffersTableInfo.TableInfo.TABLE_NAME, "WHERE UOID = " +UOID, ar)
        */

        String sql_query = "DELETE FROM " + OwnOffersTableInfo.TableInfo.TABLE_NAME + " WHERE UOID = '" +UOID +"'";
        sqLiteDatabase.execSQL(sql_query);

    }


    public void deleteTable(OwnOffersDatabase ownOffersDatabase, String tablename) {
        SQLiteDatabase sqLiteDatabase = ownOffersDatabase.getWritableDatabase();
        sqLiteDatabase.delete(tablename, null, null);
    }

    //retrieving data back form database
    public Cursor getInformation(OwnOffersDatabase ownOffersDatabase) {
        SQLiteDatabase sqLiteDatabase = ownOffersDatabase.getReadableDatabase();
        //String[] columns = {OwnOffersTableInfo.TableInfo.TITLE, OwnOffersTableInfo.TableInfo.DESCR};
        String[] columns = {OwnOffersTableInfo.TableInfo.UUID, OwnOffersTableInfo.TableInfo.UOID, OwnOffersTableInfo.TableInfo.CATEGORY, OwnOffersTableInfo.TableInfo.TITLE, OwnOffersTableInfo.TableInfo.DESCR, OwnOffersTableInfo.TableInfo.TIMEPUT, OwnOffersTableInfo.TableInfo.DATEPUT, OwnOffersTableInfo.TableInfo.TIMEDUE, OwnOffersTableInfo.TableInfo.DATEDUE, OwnOffersTableInfo.TableInfo.LAT, OwnOffersTableInfo.TableInfo.LNG};
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


}
