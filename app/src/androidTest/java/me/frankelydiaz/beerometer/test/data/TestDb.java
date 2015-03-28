package me.frankelydiaz.beerometer.test.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import java.util.HashSet;

import me.frankelydiaz.beerometer.data.BeerContract;
import me.frankelydiaz.beerometer.data.BeerDbHelper;
import me.frankelydiaz.beerometer.test.TestUtilities;

/**
 * Created by frankelydiaz on 3/28/15.
 */
public class TestDb extends AndroidTestCase {

    public static final String LOG_TAG = TestDb.class.getSimpleName();

    // Since we want each test to start with a clean slate
    void deleteTheDatabase() {
        mContext.deleteDatabase(BeerDbHelper.DATABASE_NAME);
    }

    /*
        This function gets called before each test is executed to delete the database.  This makes
        sure that we always have a clean test.
     */
    public void setUp() {
        deleteTheDatabase();
    }

    /*
        Students: Uncomment this test once you've written the code to create the Location
        table.  Note that you will have to have chosen the same column names that I did in
        my solution for this test to compile, so if you haven't yet done that, this is
        a good time to change your column names to match mine.

        Note that this only tests that the Location table has the correct columns, since we
        give you the code for the weather table.  This test does not look at the
     */
    public void testCreateDb() throws Throwable {
        // build a HashSet of all of the table names we wish to look for
        // Note that there will be another table in the DB that stores the
        // Android metadata (db version information)
        final HashSet<String> tableNameHashSet = new HashSet<String>();
        tableNameHashSet.add(BeerContract.BeerEntry.TABLE_NAME);


        mContext.deleteDatabase(BeerDbHelper.DATABASE_NAME);
        SQLiteDatabase db = new BeerDbHelper(
                this.mContext).getWritableDatabase();
        assertEquals(true, db.isOpen());

        // have we created the tables we want?
        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);

        assertTrue("Error: This means that the database has not been created correctly",
                c.moveToFirst());

        // verify that the tables have been created
        do {
            tableNameHashSet.remove(c.getString(0));
        } while( c.moveToNext() );

        // if this fails, it means that your database doesn't contain both the location entry
        // and weather entry tables
        assertTrue("Error: Your database was created without both the location entry and weather entry tables",
                tableNameHashSet.isEmpty());

        // now, do our tables contain the correct columns?
        c = db.rawQuery("PRAGMA table_info(" + BeerContract.BeerEntry.TABLE_NAME + ")",
                null);

        assertTrue("Error: This means that we were unable to query the database for table information.",
                c.moveToFirst());

        // Build a HashSet of all of the column names we want to look for
        final HashSet<String> locationColumnHashSet = new HashSet<String>();
        locationColumnHashSet.add(BeerContract.BeerEntry._ID);
        locationColumnHashSet.add(BeerContract.BeerEntry.COLUMN_ABV);
        locationColumnHashSet.add(BeerContract.BeerEntry.COLUMN_BREWER);
        locationColumnHashSet.add(BeerContract.BeerEntry.COLUMN_CATEGORY);
        locationColumnHashSet.add(BeerContract.BeerEntry.COLUMN_COUNTRY);
        locationColumnHashSet.add(BeerContract.BeerEntry.COLUMN_IMAGE_URL);
        locationColumnHashSet.add(BeerContract.BeerEntry.COLUMN_NAME);
        locationColumnHashSet.add(BeerContract.BeerEntry.COLUMN_ON_SALE);
        locationColumnHashSet.add(BeerContract.BeerEntry.COLUMN_TYPE);

        int columnNameIndex = c.getColumnIndex("name");
        do {
            String columnName = c.getString(columnNameIndex);
            locationColumnHashSet.remove(columnName);
        } while(c.moveToNext());

        // if this fails, it means that your database doesn't contain all of the required location
        // entry columns
        assertTrue("Error: The database doesn't contain all of the required location entry columns",
                locationColumnHashSet.isEmpty());
        db.close();
    }



    public void testBeerTable() {


        BeerDbHelper dbHelper = new BeerDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues beerValues = new ContentValues();
        beerValues.put(BeerContract.BeerEntry.COLUMN_NAME,"Keystone Ice");
        beerValues.put(BeerContract.BeerEntry.COLUMN_IMAGE_URL,"http://www.thebeerstore.ca/sites/default/files/styles/brand_hero/public/brand/hero/Key_ICE_355mL.jpg?itok=UDkvF6Rl");
        beerValues.put(BeerContract.BeerEntry.COLUMN_ABV,5.5);
        beerValues.put(BeerContract.BeerEntry.COLUMN_CATEGORY,"Discount");
        beerValues.put(BeerContract.BeerEntry.COLUMN_TYPE,"Discount");
        beerValues.put(BeerContract.BeerEntry.COLUMN_BREWER,"Molson");
        beerValues.put(BeerContract.BeerEntry.COLUMN_COUNTRY,"Canada");
        beerValues.put(BeerContract.BeerEntry.COLUMN_ON_SALE,"true");


        long beerRowId = db.insert(BeerContract.BeerEntry.TABLE_NAME, null, beerValues);
        assertTrue(beerRowId != -1);

        Cursor beerCursor = db.query(
                BeerContract.BeerEntry.TABLE_NAME,  // Table to Query
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null, // columns to group by
                null, // columns to filter by row groups
                null  // sort order
        );

        // Move the cursor to the first valid database row and check to see if we have any rows
        assertTrue( "Error: No Records returned from location query", beerCursor.moveToFirst() );

        // Fifth Step: Validate the location Query
        TestUtilities.validateCurrentRecord("testInsertReadDb weatherEntry failed to validate",
                beerCursor, beerValues);

        // Move the cursor to demonstrate that there is only one record in the database
        assertFalse( "Error: More than one record returned from weather query",
                beerCursor.moveToNext() );

        // Sixth Step: Close cursor and database
        beerCursor.close();
        dbHelper.close();
    }

}