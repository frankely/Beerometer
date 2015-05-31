package me.frankelydiaz.beerometer.data;

import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

/**
 * Created by frankelydiaz on 3/18/15.
 */
public class BeerProvider extends ContentProvider {

    // The URI Matcher used by this content provider.
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private BeerDbHelper mOpenHelper;

    public static final int BEER = 100;
    public static final int BEER_DETAIL = 200;


    private static final SQLiteQueryBuilder sBeersOnSaleQueryBuilder;

    static {
        sBeersOnSaleQueryBuilder = new SQLiteQueryBuilder();
        sBeersOnSaleQueryBuilder.setTables(BeerContract.BeerEntry.TABLE_NAME);
    }

    private static final String sBeerOnSaleSelection =
            BeerContract.BeerEntry.TABLE_NAME +
                    "." + BeerContract.BeerEntry.COLUMN_ON_SALE + " = ? ";

    private static final String sBeerIdSelection =
            BeerContract.BeerEntry.TABLE_NAME +
                    "." + BeerContract.BeerEntry._ID + " = ? ";


    private Cursor getBeers(String[] projection, String sortOrder) {


        return sBeersOnSaleQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                sBeerOnSaleSelection,
                new String[]{"true"},
                null,
                null,
                sortOrder
        );
    }

    private Cursor getBeer(Uri uri, String[] projection, String sortOrder) {


        String id = BeerContract.BeerEntry.getBeerIdFromUri(uri);

        return sBeersOnSaleQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                sBeerIdSelection, new String[]{ id },

                null,
                null,
                sortOrder
        );
    }


    public static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = BeerContract.CONTENT_AUTHORITY;


        matcher.addURI(authority, BeerContract.PATH_BEER, BEER);
        matcher.addURI(authority, BeerContract.PATH_BEER + "/*", BEER_DETAIL);

        return matcher;
    }


    @Override
    public boolean onCreate() {
        mOpenHelper = new BeerDbHelper(getContext());
        return true;
    }

    /*
        Students: Here's where you'll code the getType function that uses the UriMatcher.  You can
        test this by uncommenting testGetType in TestProvider.

     */
    @Override
    public String getType(Uri uri) {


        final int match = sUriMatcher.match(uri);

        switch (match) {

            case BEER:
                return BeerContract.BeerEntry.CONTENT_TYPE;

            case BEER_DETAIL:
                return BeerContract.BeerEntry.CONTENT_ITEM_TYPE;


            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {

        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {

            case BEER: {
                retCursor = getBeers(projection, sortOrder);
                break;
            }
            case BEER_DETAIL: {
                retCursor = getBeer(uri, projection, sortOrder);
                break;
            }


            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    /*
        Student: Add the ability to insert Locations to the implementation of this function.
     */
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case BEER: {
                normalizeDate(values);
                long _id = db.insertWithOnConflict(BeerContract.BeerEntry.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
                if (_id > 0)
                    returnUri = BeerContract.BeerEntry.buildBeerUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;
        // this makes delete all rows return the number of rows deleted
        if (null == selection) selection = "1";
        switch (match) {
            case BEER:
                rowsDeleted = db.delete(
                        BeerContract.BeerEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        // Because a null deletes all rows
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    private void normalizeDate(ContentValues values) {

    }

    @Override
    public int update(
            Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;

        switch (match) {
            case BEER:
                normalizeDate(values);
                rowsUpdated = db.update(BeerContract.BeerEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case BEER:
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        normalizeDate(value);
                        long _id = db.insertWithOnConflict(BeerContract.BeerEntry.TABLE_NAME, null, value, SQLiteDatabase.CONFLICT_REPLACE);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            default:
                return super.bulkInsert(uri, values);
        }
    }

    @Override
    @TargetApi(11)
    public void shutdown() {
        mOpenHelper.close();
        super.shutdown();
    }
}