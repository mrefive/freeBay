package com.mrefive.freebay.OwnOffersDB;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import com.mrefive.freebay.MainActivity;

import java.io.File;

/**
 * Created by mrefive on 5/22/16.
 */
public class DeleteOfferImg {

    private Context context;

    String filename;
    String fileName2;

    public DeleteOfferImg(Context context) {
        this.context = context;
    }

    public void deleteFile(String UOID) {

        OwnOffersDatabase ownOffersDatabase = new OwnOffersDatabase(context);
        SQLiteDatabase sqLiteDatabase = ownOffersDatabase.getWritableDatabase();

        Log.d("DeleteOfferImg", "UOID = " +UOID);

        String[] columns = {"imgname"};
        String where = OwnOffersTableInfo.TableInfo.UOID + " =?";
        String[] whereargs = {UOID};
        Cursor cursor = sqLiteDatabase.query(OwnOffersTableInfo.TableInfo.TABLE_NAME, columns, where, whereargs, null, null, null );

        if(cursor != null && cursor.moveToFirst()){
            //num = cursor.getString(cursor.getColumnIndex("ContactNumber"));
            fileName2 = cursor.getString(cursor.getColumnIndex("imgname"));
            cursor.close();
        }
        Log.d("DeleteOfferImg", "fileName2 = " +fileName2);

        File folder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "FreeBay");
        File[] listoffiles = folder.listFiles();

        for(int i =0; i<listoffiles.length;i++) {
            filename = listoffiles[i].getName();
            if(filename.endsWith(fileName2+".jpg")){
                Log.d("DeleteOfferImg","Filefound: "+filename);
                File image = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "FreeBay" + File.separator + filename);
                image.delete();
                return;
            }
        }
        ownOffersDatabase.close();
    }
}
