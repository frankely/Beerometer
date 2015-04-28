package me.frankelydiaz.beerometer.adapter;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.Arrays;

import me.frankelydiaz.beerometer.R;
import me.frankelydiaz.beerometer.data.BeerContract;

/**
 * Created by frankelydiaz on 3/18/15.
 */
public class BeerAdapter extends CursorAdapter {


    public static final String[] BEER_COLUMNS = {
            BeerContract.BeerEntry._ID,
            BeerContract.BeerEntry.COLUMN_NAME,
            BeerContract.BeerEntry.COLUMN_ABV,
            BeerContract.BeerEntry.COLUMN_BREWER,
            BeerContract.BeerEntry.COLUMN_CATEGORY,
            BeerContract.BeerEntry.COLUMN_COUNTRY,
            BeerContract.BeerEntry.COLUMN_IMAGE_URL
    };

    public BeerAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    public static int getBeerColumnIndex(String column) {
        return Arrays.asList(BEER_COLUMNS).indexOf(column);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        final View view = LayoutInflater.from(context).inflate(R.layout.list_item_beer, parent, false);
        final ViewHolder viewHolder = new ViewHolder(view);

        view.setTag(viewHolder);
        return view;
    }

    /*
        This is where we fill-in the views with the contents of the cursor.
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        final ViewHolder viewHolder = (ViewHolder) view.getTag();

        final String imageUrl = cursor.getString(getBeerColumnIndex(BeerContract.BeerEntry.COLUMN_IMAGE_URL));

        if (imageUrl != null) {
            final Uri uri = Uri.parse(imageUrl);
            viewHolder.draweeView.setImageURI(uri);
        }



        viewHolder.nameView.setText(cursor.getString(getBeerColumnIndex(BeerContract.BeerEntry.COLUMN_NAME)));
    }

    public static class ViewHolder {
        public final TextView nameView;
        public final SimpleDraweeView draweeView;

        public ViewHolder(View view) {
            this.nameView = (TextView) view.findViewById(R.id.list_item_beer_textview);
           this.draweeView = (SimpleDraweeView) view.findViewById(R.id.list_item_beer_imageview);
        }
    }
}
