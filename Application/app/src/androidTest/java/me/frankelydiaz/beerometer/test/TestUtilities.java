package me.frankelydiaz.beerometer.test;

/**
 * Created by frankelydiaz on 3/28/15.
 */

import android.content.ContentValues;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.test.AndroidTestCase;

import java.util.Map;
import java.util.Set;

import me.frankelydiaz.beerometer.data.BeerContract;

/*
    Students: These are functions and some test data to make it easier to test your database and
    Content Provider.  Note that you'll want your WeatherContract class to exactly match the one
    in our solution to use these as-given.
 */
public class TestUtilities extends AndroidTestCase {

    public static void validateCursor(String error, Cursor valueCursor, ContentValues expectedValues) {
        assertTrue("Empty cursor returned. " + error, valueCursor.moveToFirst());
        validateCurrentRecord(error, valueCursor, expectedValues);
        valueCursor.close();
    }

    public static void validateCurrentRecord(String error, Cursor valueCursor, ContentValues expectedValues) {
        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();
        for (Map.Entry<String, Object> entry : valueSet) {
            String columnName = entry.getKey();
            int idx = valueCursor.getColumnIndex(columnName);
            assertFalse("Column '" + columnName + "' not found. " + error, idx == -1);
            String expectedValue = entry.getValue().toString();
            assertEquals("Value '" + entry.getValue().toString() +
                    "' did not match the expected value '" +
                    expectedValue + "'. " + error, expectedValue, valueCursor.getString(idx));
        }
    }
    public static ContentValues getBeerValues() {
        ContentValues beerValues = new ContentValues();
        beerValues.put(BeerContract.BeerEntry.COLUMN_NAME,"Keystone Ice");
        beerValues.put(BeerContract.BeerEntry.COLUMN_IMAGE_URL,"http://www.thebeerstore.ca/sites/default/files/styles/brand_hero/public/brand/hero/Key_ICE_355mL.jpg?itok=UDkvF6Rl");
        beerValues.put(BeerContract.BeerEntry.COLUMN_ABV,5.5);
        beerValues.put(BeerContract.BeerEntry.COLUMN_CATEGORY,"Discount");
        beerValues.put(BeerContract.BeerEntry.COLUMN_TYPE,"Discount");
        beerValues.put(BeerContract.BeerEntry.COLUMN_BREWER,"Molson");
        beerValues.put(BeerContract.BeerEntry.COLUMN_COUNTRY,"Canada");
        beerValues.put(BeerContract.BeerEntry.COLUMN_ON_SALE,"true");
        return beerValues;
    }

    public static class TestContentObserver extends ContentObserver {
        final HandlerThread mHT;
        boolean mContentChanged;

        public static TestContentObserver getTestContentObserver() {
            HandlerThread ht = new HandlerThread("ContentObserverThread");
            ht.start();
            return new TestContentObserver(ht);
        }

        private TestContentObserver(HandlerThread ht) {
            super(new Handler(ht.getLooper()));
            mHT = ht;
        }

        public void waitForNotificationOrFail() {
            // Note: The PollingCheck class is taken from the Android CTS (Compatibility Test Suite).
            // It's useful to look at the Android CTS source for ideas on how to test your Android
            // applications.  The reason that PollingCheck works is that, by default, the JUnit
            // testing framework is not running on the main Android application thread.
            new PollingCheck(5000) {
                @Override
                protected boolean check() {
                    return mContentChanged;
                }
            }.run();
            mHT.quit();
        }

        // On earlier versions of Android, this onChange method is called
        @Override
        public void onChange(boolean selfChange) {
            onChange(selfChange, null);
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            mContentChanged = true;
        }

    }

}