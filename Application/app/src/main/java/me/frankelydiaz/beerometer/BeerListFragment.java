package me.frankelydiaz.beerometer;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import me.frankelydiaz.beerometer.adapter.BeerAdapter;
import me.frankelydiaz.beerometer.data.BeerContract;
import me.frankelydiaz.beerometer.sync.BeerometerSyncAdapter;

/**
 * Created by frankelydiaz on 3/18/15.
 */
public class BeerListFragment extends Fragment implements android.support.v4.app.LoaderManager.LoaderCallbacks<Cursor>, SharedPreferences.OnSharedPreferenceChangeListener {
    private static final int BEERS_LOADER = 0;
    private static final String TAG = BeerListFragment.class.getCanonicalName();
    private BeerAdapter mBeerAdapter = new BeerAdapter(getActivity(), null);
    private RecyclerView.LayoutManager mLayoutManager;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());


        prefs.registerOnSharedPreferenceChangeListener(this);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // The CursorAdapter will take data from our cursor and populate the ListView.
        mBeerAdapter = new BeerAdapter(getActivity(), null);

        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        // Get a reference to the ListView, and attach this adapter to it.
        RecyclerView mRecyclerView = (RecyclerView) rootView.findViewById(R.id.beer_list_listview);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new GridLayoutManager(getActivity(),getColumnsSize());
        mRecyclerView.setLayoutManager(mLayoutManager);


        mRecyclerView.setAdapter(mBeerAdapter);


        return rootView;
    }

    private int getColumnsSize() {
        return getResources().getInteger(R.integer.grid_columns);
    }

    @Override
    public void onStart() {
        super.onStart();


    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(BEERS_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

        Uri beerUri = BeerContract.BeerEntry.buildBeerUri();

        return new CursorLoader(getActivity(),
                beerUri,
                BeerAdapter.BEER_COLUMNS,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<Cursor> loader, Cursor data) {
        mBeerAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<Cursor> loader) {
        mBeerAdapter.swapCursor(null);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        if (!isAdded())
            return;

        if (key.equals(getString(R.string.pref_alcohol_abv_key))) {
            BeerometerSyncAdapter.syncImmediately(getActivity());
            getLoaderManager().restartLoader(BEERS_LOADER, null, this);
        }
    }
}
