package me.frankelydiaz.beerometer;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import me.frankelydiaz.beerometer.adapter.BeerAdapter;
import me.frankelydiaz.beerometer.data.BeerContract;
import me.frankelydiaz.beerometer.task.FetchBeerTask;

/**
 * Created by frankelydiaz on 3/18/15.
 */
public class BeerListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private BeerAdapter mBeerAdapter = new BeerAdapter(getActivity(), null, 0);

    private static final String[] BEER_COLUMNS = {
            BeerContract.BeerEntry._ID,
            BeerContract.BeerEntry.COLUMN_NAME
    };

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // The CursorAdapter will take data from our cursor and populate the ListView.
        mBeerAdapter = new BeerAdapter(getActivity(), null, 0);

        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        // Get a reference to the ListView, and attach this adapter to it.
        ListView listView = (ListView) rootView.findViewById(R.id.beer_list_view);
        listView.setAdapter(mBeerAdapter);


        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        updateBeers();
    }

    private void updateBeers() {
        FetchBeerTask beerTask = new FetchBeerTask(getActivity());
        beerTask.execute();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

        Uri beerUri = BeerContract.BeerEntry.buildBeerUri();

        return new CursorLoader(getActivity(),
                beerUri,
                BEER_COLUMNS,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        mBeerAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        mBeerAdapter.swapCursor(null);
    }
}
