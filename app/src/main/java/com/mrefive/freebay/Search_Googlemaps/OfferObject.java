package com.mrefive.freebay.Search_Googlemaps;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import java.io.File;

/**
 * Created by mrefive on 6/14/16.
 */
public class OfferObject {

    private String UOID, title, descr, category, lat, lng, dateend, imagename;
    private Bitmap offerimage;
    private Bitmap offerthumbnail;

    public OfferObject() {
    }

    public String getUOID() {
        return UOID;
    }

    public void setUOID(String UUID) {
        this.UOID = UOID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getDateend() {
        return dateend;
    }

    public void setDateend(String dateend) {
        this.dateend = dateend;
    }

    public String getImagename() {
        return imagename;
    }


    public Bitmap getOfferthumbnail() {
        return offerthumbnail;
    }

    public void setOfferthumbnail(Bitmap offerthumbnail) {
        this.offerthumbnail = offerthumbnail;
    }

    public void setImagename(String imagename) {
        this.imagename = imagename;

        File image = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "FreeBay" + File.separator + imagename);
        offerimage = BitmapFactory.decodeFile(image.getAbsolutePath());
    }

    public Bitmap getOfferimage() {
        return offerimage;
    }

    public void setOfferimage(Bitmap offerimage) {
        this.offerimage = offerimage;
    }
}
