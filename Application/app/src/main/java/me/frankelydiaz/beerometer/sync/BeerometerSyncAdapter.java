package me.frankelydiaz.beerometer.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SyncRequest;
import android.content.SyncResult;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import me.frankelydiaz.beerometer.R;
import me.frankelydiaz.beerometer.data.BeerContract;
import me.frankelydiaz.beerometer.model.Beer;
import me.frankelydiaz.beerometer.webservice.BeerWebService;

/**
 * Created by frankelydiaz on 4/27/15.
 */
public class BeerometerSyncAdapter extends AbstractThreadedSyncAdapter {
    public final String LOG_TAG = BeerometerSyncAdapter.class.getSimpleName();
    // Interval at which to sync with the weather, in seconds.
    // 60 seconds (1 minute) * 180 = 3 hours
    public static final int SYNC_INTERVAL = 60 * 180;
    public static final int SYNC_FLEXTIME = SYNC_INTERVAL / 3;


    public BeerometerSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        Log.d(LOG_TAG, "Starting sync");


        if (hasInternetConnection()) {
            final String defaultMinimunAbv = this.getContext().getString(R.string.pref_alcohol_abv_default);
            final String minimunAbv = PreferenceManager.getDefaultSharedPreferences(this.getContext()).getString(this.getContext().getString(R.string.pref_alcohol_abv_key), defaultMinimunAbv);

            List<Beer> beers = BeerWebService.getBeers(Integer.parseInt(minimunAbv));

            if (needUpdate(beers)) {
                deleteAllBears();
                storeData(beers);
            }
        }
    }

    private boolean hasInternetConnection() {
        ConnectivityManager cm =
                (ConnectivityManager) this.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        return isConnected;
    }


    private boolean needUpdate(final List<Beer> beers) {
        Cursor allBeers = getContext().getContentResolver().query(BeerContract.BeerEntry.CONTENT_URI,
                null,
                null,
                null,
                null);

        return allBeers.getCount() != beers.size();

    }

    private void deleteAllBears() {
        getContext().getContentResolver().delete(
                BeerContract.BeerEntry.CONTENT_URI,
                null,
                null
        );
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
            beerValues.put(BeerContract.BeerEntry.COLUMN_IMAGE_URL, beer.imageUrl);
            beerRows.add(beerValues);
        }

        ContentValues[] beerArray = new ContentValues[beerRows.size()];

        beerRows.toArray(beerArray);

        int inserted = getContext().getContentResolver().bulkInsert(BeerContract.BeerEntry.CONTENT_URI, beerArray);

        Log.d(LOG_TAG, "inserted: " + inserted);

    }


    /**
     * Helper method to schedule the sync adapter periodic execution
     */
    public static void configurePeriodicSync(Context context, int syncInterval, int flexTime) {
        Account account = getSyncAccount(context);
        String authority = context.getString(R.string.content_authority);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // we can enable inexact timers in our periodic sync
            SyncRequest request = new SyncRequest.Builder().
                    syncPeriodic(syncInterval, flexTime).
                    setSyncAdapter(account, authority).
                    setExtras(new Bundle()).build();
            ContentResolver.requestSync(request);
        } else {
            ContentResolver.addPeriodicSync(account,
                    authority, new Bundle(), syncInterval);
        }
    }

    /**
     * Helper method to have the sync adapter sync immediately
     *
     * @param context The context used to access the account service
     */
    public static void syncImmediately(Context context) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        ContentResolver.requestSync(getSyncAccount(context),
                context.getString(R.string.content_authority), bundle);
    }

    /**
     * Helper method to get the fake account to be used with SyncAdapter, or make a new one
     * if the fake account doesn't exist yet.  If we make a new account, we call the
     * onAccountCreated method so we can initialize things.
     *
     * @param context The context used to access the account service
     * @return a fake account.
     */
    public static Account getSyncAccount(Context context) {
        // Get an instance of the Android account manager
        AccountManager accountManager =
                (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);

        // Create the account type and default account
        Account newAccount = new Account(
                context.getString(R.string.app_name), context.getString(R.string.sync_account_type));

        // If the password doesn't exist, the account doesn't exist
        if (null == accountManager.getPassword(newAccount)) {

        /*
         * Add the account and account type, no password or user data
         * If successful, return the Account object, otherwise report an error.
         */
            if (!accountManager.addAccountExplicitly(newAccount, "", null)) {
                return null;
            }
            /*
             * If you don't set android:syncable="true" in
             * in your <provider> element in the manifest,
             * then call ContentResolver.setIsSyncable(account, AUTHORITY, 1)
             * here.
             */

            onAccountCreated(newAccount, context);
        }
        return newAccount;
    }

    private static void onAccountCreated(Account newAccount, Context context) {
        /*
         * Since we've created an account
         */
        BeerometerSyncAdapter.configurePeriodicSync(context, SYNC_INTERVAL, SYNC_FLEXTIME);

        /*
         * Without calling setSyncAutomatically, our periodic sync will not be enabled.
         */
        ContentResolver.setSyncAutomatically(newAccount, context.getString(R.string.content_authority), true);

        /*
         * Finally, let's do a sync to get things started
         */
        syncImmediately(context);
    }

    public static void initializeSyncAdapter(Context context) {
        getSyncAccount(context);
    }
}
