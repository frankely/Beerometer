package me.frankelydiaz.beerometer.task;

import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import me.frankelydiaz.beerometer.data.BeerContract;
import me.frankelydiaz.beerometer.model.Beer;
import me.frankelydiaz.beerometer.webservice.BeerWebService;

/**
 * Created by frankelydiaz on 3/18/15.
 */
public class FetchBeerTask extends AsyncTask<String, Void, Void> {

    private final String LOG_TAG = FetchBeerTask.class.getSimpleName();

    private final Context mContext;

    public FetchBeerTask(Context context) {
        mContext = context;
    }


    private void storeData(List<Beer> beers) {

        if (beers.isEmpty())
            return;

        List<ContentValues> beerRows = new ArrayList<>();

        for (Beer beer : beers) {
            ContentValues beerValues = new ContentValues();
            beerValues.put(BeerContract.BeerEntry._ID, beer.beerId);
            beerValues.put(BeerContract.BeerEntry.COLUMN_ABV, beer.abv);
            beerValues.put(BeerContract.BeerEntry.COLUMN_BREWER, beer.brewer);
            beerValues.put(BeerContract.BeerEntry.COLUMN_ON_SALE, beer.onSale);
            beerValues.put(BeerContract.BeerEntry.COLUMN_CATEGORY, beer.category);
            beerValues.put(BeerContract.BeerEntry.COLUMN_COUNTRY, beer.country);
            beerValues.put(BeerContract.BeerEntry.COLUMN_CATEGORY, beer.category);
            beerValues.put(BeerContract.BeerEntry.COLUMN_NAME, beer.name);
            beerValues.put(BeerContract.BeerEntry.COLUMN_TYPE, beer.type);

            beerRows.add(beerValues);
        }

        ContentValues[] beerArray = new ContentValues[beerRows.size()];

        beerRows.toArray(beerArray);

        int inserted = mContext.getContentResolver().bulkInsert(BeerContract.BeerEntry.CONTENT_URI, beerArray);

        Log.d(LOG_TAG, "inserted: " + inserted);

    }

    @Override
    protected Void doInBackground(String... params) {

        List<Beer> beers = BeerWebService.getBeers();

        storeData(beers);

        return null;
    }


}