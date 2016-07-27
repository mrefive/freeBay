package com.mrefive.freebay;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by mrefive on 1/7/16.
 */
public class OwnOffers {

    private String name, descr, dateend, filename;
    private Bitmap imagesmall;

    public OwnOffers(String name, String descr, String dateend, String filename) {
        this.setName(name);
        this.setDescr(descr);
        this.setDateend(dateend);
        this.setFilename(filename);

        File image = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "FreeBay" + File.separator + filename);
        Log.d("OwnOffers",image.getAbsolutePath());
        imagesmall = BitmapFactory.decodeFile(image.getAbsolutePath());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    public String getDateend() {
        return dateend;
    }

    public void setDateend(String dateend) {
        this.dateend = dateend;
    }

    public Bitmap getImagesmall() {
        return imagesmall;
    }

    public void setImagesmall(Bitmap imagesmall) {
        this.imagesmall = imagesmall;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
}
