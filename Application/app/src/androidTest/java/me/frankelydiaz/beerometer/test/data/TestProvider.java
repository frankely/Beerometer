package me.frankelydiaz.beerometer.test.data;

import android.content.ComponentName;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.net.Uri;
import android.test.AndroidTestCase;

import me.frankelydiaz.beerometer.data.BeerContract;
import me.frankelydiaz.beerometer.data.BeerProvider;
import me.frankelydiaz.beerometer.test.TestUtilities;

/**
 * Created by frankelydiaz on 3/28/15.
 */
public class TestProvider extends AndroidTestCase {

    public static final String LOG_TAG = TestProvider.class.getSimpleName();

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        deleteAllRecords();
    }

    public void deleteAllRecordsFromProvider() {
        mContext.getContentResolver().delete(
                BeerContract.BeerEntry.CONTENT_URI,
                null,
                null
        );
        mContext.getContentResolver().delete(
                BeerContract.BeerEntry.CONTENT_URI,
                null,
                null
        );

        Cursor cursor = mContext.getContentResolver().query(
                BeerContract.BeerEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        assertEquals("Error: Records not deleted from Beer table during delete", 0, cursor.getCount());
        cursor.close();
    }

    public void deleteAllRecords() {
        deleteAllRecordsFromProvider();
    }


    public void testProviderRegistry() {
        PackageManager pm = mContext.getPackageManager();

        // We define the component name based on the package name from the context and the
        // BeerProvider class.
        ComponentName componentName = new ComponentName(mContext.getPackageName(),
                BeerProvider.class.getName());
        try {
            // Fetch the provider info using the component name from the PackageManager
            // This throws an exception if the provider isn't registered.
            ProviderInfo providerInfo = pm.getProviderInfo(componentName, 0);

            // Make sure that the registered authority matches the authority from the Contract.
            assertEquals("Error: BeerProvider registered with authority: " + providerInfo.authority +
                            " instead of authority: " + BeerContract.CONTENT_AUTHORITY,
                    providerInfo.authority, BeerContract.CONTENT_AUTHORITY);
        } catch (PackageManager.NameNotFoundException e) {
            // I guess the provider isn't registered correctly.
            assertTrue("Error: BeerProvider not registered at " + mContext.getPackageName(),
                    false);
        }
    }


    public void testGetType() {

        String type = mContext.getContentResolver().getType(BeerContract.BeerEntry.CONTENT_URI);

        assertEquals("Error: the BeerEntry CONTENT_URI should return BeerEntry.CONTENT_TYPE",
                BeerContract.BeerEntry.CONTENT_TYPE, type);

        final long beerId = 1;

        type = mContext.getContentResolver().getType(
                BeerContract.BeerEntry.buildBeerUri(beerId));

        assertEquals("Error: the BeerEntry CONTENT_URI by id should return BeerEntry.CONTENT_ITEM_TYPE",
                BeerContract.BeerEntry.CONTENT_ITEM_TYPE, type);

    }


    public void testInsertReadProvider() {
        ContentValues testValues = TestUtilities.getBeerValues();


        TestUtilities.TestContentObserver tco = TestUtilities.TestContentObserver.getTestContentObserver();

        mContext.getContentResolver().registerContentObserver(BeerContract.BeerEntry.CONTENT_URI, true, tco);
        Uri locationUri = mContext.getContentResolver().insert(BeerContract.BeerEntry.CONTENT_URI, testValues);


        tco.waitForNotificationOrFail();
        mContext.getContentResolver().unregisterContentObserver(tco);

        long beerRowId = ContentUris.parseId(locationUri);

        assertTrue(beerRowId != -1);


        Cursor cursor = mContext.getContentResolver().query(
                BeerContract.BeerEntry.CONTENT_URI,
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null  // sort order
        );

        TestUtilities.validateCursor("testInsertReadProvider. Error validating LocationEntry.",
                cursor, testValues);


    }


    public void testDeleteRecords() {
        testInsertReadProvider();

        // Register a content observer for our location delete.
        TestUtilities.TestContentObserver locationObserver = TestUtilities.TestContentObserver.getTestContentObserver();
        mContext.getContentResolver().registerContentObserver(BeerContract.BeerEntry.CONTENT_URI, true, locationObserver);

        // Register a content observer for our Beer delete.
        TestUtilities.TestContentObserver BeerObserver = TestUtilities.TestContentObserver.getTestContentObserver();
        mContext.getContentResolver().registerContentObserver(BeerContract.BeerEntry.CONTENT_URI, true, BeerObserver);

        deleteAllRecordsFromProvider();

        // Students: If either of these fail, you most-likely are not calling the
        // getContext().getContentResolver().notifyChange(uri, null); in the ContentProvider
        // delete.  (only if the insertReadProvider is succeeding)
        locationObserver.waitForNotificationOrFail();
        BeerObserver.waitForNotificationOrFail();

        mContext.getContentResolver().unregisterContentObserver(locationObserver);
        mContext.getContentResolver().unregisterContentObserver(BeerObserver);
    }


    static private final int BULK_INSERT_RECORDS_TO_INSERT = 10;
    static ContentValues[] createBulkInsertBeerValues() {

        ContentValues[] returnContentValues = new ContentValues[BULK_INSERT_RECORDS_TO_INSERT];

        for ( int i = 0; i < BULK_INSERT_RECORDS_TO_INSERT; i++) {
            ContentValues beerValues = new ContentValues();
            beerValues.put(BeerContract.BeerEntry.COLUMN_NAME,"Keystone Ice" + i);
            beerValues.put(BeerContract.BeerEntry.COLUMN_IMAGE_URL,"http://www.thebeerstore.ca/sites/default/files/styles/brand_hero/public/brand/hero/Key_ICE_355mL.jpg?itok=UDkvF6Rl");
            beerValues.put(BeerContract.BeerEntry.COLUMN_ABV,5.5);
            beerValues.put(BeerContract.BeerEntry.COLUMN_CATEGORY,"Discount");
            beerValues.put(BeerContract.BeerEntry.COLUMN_TYPE,"Discount");
            beerValues.put(BeerContract.BeerEntry.COLUMN_BREWER,"Molson");
            beerValues.put(BeerContract.BeerEntry.COLUMN_COUNTRY,"Canada");
            beerValues.put(BeerContract.BeerEntry.COLUMN_ON_SALE,"true");
            returnContentValues[i] = beerValues;
        }
        return returnContentValues;
    }

    // Student: Uncomment this test after you have completed writing the BulkInsert functionality
    // in your provider.  Note that this test will work with the built-in (default) provider
    // implementation, which just inserts records one-at-a-time, so really do implement the
    // BulkInsert ContentProvider function.
    public void testBulkInsert() {

        // Now we can bulkInsert some Beer.  In fact, we only implement BulkInsert for Beer
        // entries.  With ContentProviders, you really only have to implement the features you
        // use, after all.
        ContentValues[] bulkInsertContentValues = createBulkInsertBeerValues();

        // Register a content observer for our bulk insert.
        TestUtilities.TestContentObserver BeerObserver = TestUtilities.TestContentObserver.getTestContentObserver();
        mContext.getContentResolver().registerContentObserver(BeerContract.BeerEntry.CONTENT_URI, true, BeerObserver);

        int insertCount = mContext.getContentResolver().bulkInsert(BeerContract.BeerEntry.CONTENT_URI, bulkInsertContentValues);

        // Students:  If this fails, it means that you most-likely are not calling the
        // getContext().getContentResolver().notifyChange(uri, null); in your BulkInsert
        // ContentProvider method.
        BeerObserver.waitForNotificationOrFail();
        mContext.getContentResolver().unregisterContentObserver(BeerObserver);

        assertEquals(BULK_INSERT_RECORDS_TO_INSERT,insertCount);

        // A cursor is your primary interface to the query results.
        Cursor cursor = mContext.getContentResolver().query(
                BeerContract.BeerEntry.CONTENT_URI,
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                BeerContract.BeerEntry.COLUMN_BREWER + " ASC"
        );

        // we should have as many records in the database as we've inserted
        assertEquals(BULK_INSERT_RECORDS_TO_INSERT,cursor.getCount());

        // and let's make sure they match the ones we created
        cursor.moveToFirst();
        for ( int i = 0; i < BULK_INSERT_RECORDS_TO_INSERT; i++, cursor.moveToNext() ) {
            TestUtilities.validateCurrentRecord("testBulkInsert.  Error validating BeerEntry " + i,
                    cursor, bulkInsertContentValues[i]);
        }
        cursor.close();
    }
}