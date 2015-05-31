package me.frankelydiaz.beerometer.test.data;

import android.net.Uri;
import android.test.AndroidTestCase;

import me.frankelydiaz.beerometer.data.BeerContract;

/**
 * Created by frankelydiaz on 3/28/15.
 */
public class TestBeerContract extends AndroidTestCase {

    private static final long TEST_BEER_ID = 1;


    public void testBuildBeerUri() {
        final Uri locationUri = BeerContract.BeerEntry.buildBeerUri(TEST_BEER_ID);

        assertNotNull("Error: Null Uri returned.  You must fill-in buildBeerUri in " +
                        "BeerContract.",
                locationUri);

        assertEquals("Error: BeerId not properly appended to the end of the Uri",
                TEST_BEER_ID,  Long.parseLong(locationUri.getLastPathSegment()));

        assertEquals("Error: Beer  Id Uri doesn't match our expected result",
                locationUri.toString(),
                "content://me.frankelydiaz.beerometer/beer/1");
    }
}
