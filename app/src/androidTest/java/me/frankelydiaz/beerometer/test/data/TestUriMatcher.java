package me.frankelydiaz.beerometer.test.data;

import android.content.UriMatcher;
import android.net.Uri;
import android.test.AndroidTestCase;

import me.frankelydiaz.beerometer.data.BeerContract;
import me.frankelydiaz.beerometer.data.BeerProvider;

/**
 * Created by frankelydiaz on 3/28/15.
 */
public class TestUriMatcher extends AndroidTestCase {

    private static final long BEER_ID = 1;
    private static final Uri TEST_BEER_DIR = BeerContract.BeerEntry.CONTENT_URI;
    private static final Uri TEST_BEER_WITH_LOCATION_DIR = BeerContract.BeerEntry.buildBeerUri(BEER_ID);


    public void testUriMatcher() {
        UriMatcher testMatcher = BeerProvider.buildUriMatcher();

        assertEquals("Error: The BEER URI was matched incorrectly.",
                testMatcher.match(TEST_BEER_DIR), BeerProvider.BEER);

        assertEquals("Error: The BEER ON SALE URI was matched incorrectly.",
                testMatcher.match(TEST_BEER_WITH_LOCATION_DIR), BeerProvider.BEERS_ON_SALE);

    }

}
