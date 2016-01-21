package com.mrefive.freebay.OwnOffersDB;

import android.provider.BaseColumns;

/**
 * Created by mrefive on 1/12/16.
 */
public class OwnOffersTableInfo {

    public OwnOffersTableInfo() {
    }

    public static abstract class TableInfo implements BaseColumns{

        public static String UUID = "UUID";
        public static String UOID = "UOID";
        public static String CATEGORY = "category";
        public static String TITLE = "title";
        public static String DESCR = "descr";
        public static String TIMEPUT = "timeput";
        public static String DATEPUT = "timedue";
        public static String DATEDUE = "datedue";
        public static String LAT = "lat";
        public static String LNG = "lng";
        public static String  DB_NAME = "own_offers_local_db";
        public static String  TABLE_NAME = "own_offers_table";


    }
}
