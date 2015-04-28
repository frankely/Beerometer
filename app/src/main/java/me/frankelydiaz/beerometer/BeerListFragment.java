package me.frankelydiaz.beerometer;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import me.frankelydiaz.beerometer.adapter.BeerAdapter;
import me.frankelydiaz.beerometer.data.BeerContract;

/**
 * Created by frankelydiaz on 3/18/15.
 */
public class BeerListFragment extends Fragment implements android.support.v4.app.LoaderManager.LoaderCallbacks<Cursor> {
    private static final int BEERS_LOADER = 0;
    private BeerAdapter mBeerAdapter = new BeerAdapter(getActivity(), null, 0);


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // The CursorAdapter will take data from our cursor and populate the ListView.
        mBeerAdapter = new BeerAdapter(getActivity(), null, 0);

        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        // Get a reference to the ListView, and attach this adapter to it.
        ListView listView = (ListView) rootView.findViewById(R.id.beer_list_listview);
        listView.setAdapter(mBeerAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Cursor cursor = (Cursor) adapterView.getItemAtPosition(position);
                if (cursor != null) {
                    Intent intent = new Intent(getActivity(), DetailActivity.class)
                            .setData(BeerContract.BeerEntry.buildBeerUri(cursor.getLong(BeerAdapter.getBeerColumnIndex(BeerContract.BeerEntry._ID))));
                    startActivity(intent);
                }
            }
        });


        return rootView;
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

}
