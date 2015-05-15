package me.frankelydiaz.beerometer;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import me.frankelydiaz.beerometer.adapter.BeerAdapter;
import me.frankelydiaz.beerometer.data.BeerContract;


/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final String DETAIL_URI = "URI";
    private static final int DETAIL_LOADER = 0;
    private Uri mUri;

    public static DetailActivityFragment newInstance(Bundle bundle){
        DetailActivityFragment fragment = new DetailActivityFragment();
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);

        Bundle arguments = getArguments();
        if (arguments != null) {
            mUri = arguments.getParcelable(DETAIL_URI);

        }

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(DETAIL_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        if (null != mUri) {
            // Now create and return a CursorLoader that will take care of
            // creating a Cursor for the data being displayed.
            return new CursorLoader(
                    getActivity(),
                    mUri,
                    BeerAdapter.BEER_COLUMNS,
                    null,
                    null,
                    null
            );
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data != null && data.moveToFirst()) {
            SimpleDraweeView imageView = (SimpleDraweeView) getView().findViewById(R.id.detail_imageview);
            TextView titleText = (TextView) getView().findViewById(R.id.detail_title_textview);
            TextView subtitleText = (TextView) getView().findViewById(R.id.detail_subtitle_textview);
            TextView abvText = (TextView) getView().findViewById(R.id.detail_abv);

            final String imageUrl = data.getString(BeerAdapter.getBeerColumnIndex(BeerContract.BeerEntry.COLUMN_IMAGE_URL));

            if (imageUrl != null) {
                final Uri uri = Uri.parse(imageUrl);
                imageView.setImageURI(uri);
            }

            titleText.setText(data.getString(BeerAdapter.getBeerColumnIndex(BeerContract.BeerEntry.COLUMN_NAME)));
            subtitleText.setText(data.getString(BeerAdapter.getBeerColumnIndex(BeerContract.BeerEntry.COLUMN_TYPE)));
            abvText.setText(data.getString(BeerAdapter.getBeerColumnIndex(BeerContract.BeerEntry.COLUMN_ABV)) + "%");


        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
