package com.mrefive.freebay.OwnOffersDB;

import android.provider.BaseColumns;

/**
 * Created by mrefive on 1/12/16.
 */
public class OwnOffersTableInfo {

    public OwnOffersTableInfo() {
    }

    public static abstract class TableInfo implements BaseColumns{

        public static String UOID = "UUID";
        public static String UUID = "UOID";
        public static String CATEGORY = "category";
        public static String TITLE = "title";
        public static String DESCRIPTION = "description";
        public static String DATEPUT = "dateput";
        public static String DATEEND = "dateend";
        public static String LAT = "lat";
        public static String LNG = "lng";
        public static String IMGNAME = "imgname";
        public static String  DB_NAME = "freebay_own_offers_local_db";
        public static String  TABLE_NAME = "own_offers_table";


    }
}
