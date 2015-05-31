package me.frankelydiaz.beerometer.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by frankelydiaz on 3/18/15.
 */
public class BeerDbHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 5;

    public static final String DATABASE_NAME = "beerometer.db";

    public BeerDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // Create a table to hold locations.  A location consists of the string supplied in the
        // location setting, the city name, and the latitude and longitude
        final String SQL_CREATE_BEER_TABLE = "CREATE TABLE " + BeerContract.BeerEntry.TABLE_NAME + " (" +
                BeerContract.BeerEntry._ID + " INTEGER," +
                BeerContract.BeerEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                BeerContract.BeerEntry.COLUMN_IMAGE_URL + " TEXT NULL, " +
                BeerContract.BeerEntry.COLUMN_CATEGORY + " TEXT NOT NULL, " +
                BeerContract.BeerEntry.COLUMN_ABV + " REAL NOT NULL ," +
                BeerContract.BeerEntry.COLUMN_TYPE + " TEXT NOT NULL , " +
                BeerContract.BeerEntry.COLUMN_BREWER + " TEXT NOT NULL, " +
                BeerContract.BeerEntry.COLUMN_COUNTRY + " TEXT NOT NULL, " +
                BeerContract.BeerEntry.COLUMN_ON_SALE + " TEXT NOT NULL " +
                " )";

        sqLiteDatabase.execSQL(SQL_CREATE_BEER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        // Note that this only fires if you change the version number for your database.
        // It does NOT depend on the version number for your application.
        // If you want to update the schema without wiping data, commenting out the next 2 lines
        // should be your top priority before modifying this method.
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + BeerContract.BeerEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
