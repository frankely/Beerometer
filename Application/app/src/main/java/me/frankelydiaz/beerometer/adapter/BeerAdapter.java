package me.frankelydiaz.beerometer.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.Arrays;

import me.frankelydiaz.beerometer.DetailActivity;
import me.frankelydiaz.beerometer.R;
import me.frankelydiaz.beerometer.data.BeerContract;

/**
 * Created by frankelydiaz on 3/18/15.
 */
public class BeerAdapter extends CursorRecyclerViewAdapter<BeerAdapter.ViewHolder> {


    public static final String[] BEER_COLUMNS = {
            BeerContract.BeerEntry._ID,
            BeerContract.BeerEntry.COLUMN_NAME,
            BeerContract.BeerEntry.COLUMN_ABV,
            BeerContract.BeerEntry.COLUMN_BREWER,
            BeerContract.BeerEntry.COLUMN_CATEGORY,
            BeerContract.BeerEntry.COLUMN_COUNTRY,
            BeerContract.BeerEntry.COLUMN_IMAGE_URL,
            BeerContract.BeerEntry.COLUMN_TYPE
    };

    public BeerAdapter(Context context, Cursor c) {
        super(context, c);
    }

    public static int getBeerColumnIndex(String column) {
        return Arrays.asList(BEER_COLUMNS).indexOf(column);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_beer, parent, false);
        final ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;


    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Cursor cursor) {

        final String imageUrl = cursor.getString(getBeerColumnIndex(BeerContract.BeerEntry.COLUMN_IMAGE_URL));

        if (imageUrl != null) {
            final Uri uri = Uri.parse(imageUrl);
            viewHolder.draweeView.setImageURI(uri);
        }

        viewHolder.nameView.setText(cursor.getString(getBeerColumnIndex(BeerContract.BeerEntry.COLUMN_NAME)));
        viewHolder.typeView.setText(cursor.getString(getBeerColumnIndex(BeerContract.BeerEntry.COLUMN_TYPE)));
        viewHolder.setBeerId(cursor.getInt(getBeerColumnIndex(BeerContract.BeerEntry._ID)));
        viewHolder.abvView.setText(cursor.getString(getBeerColumnIndex(BeerContract.BeerEntry.COLUMN_ABV)) + "%");
    }


    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView nameView;
        public final TextView typeView;
        public final TextView abvView;
        public final SimpleDraweeView draweeView;

        private int beerId;

        public ViewHolder(View view) {
            super(view);

            this.nameView = (TextView) view.findViewById(R.id.list_item_beer_textview);
            this.draweeView = (SimpleDraweeView) view.findViewById(R.id.list_item_beer_imageview);
            this.typeView = (TextView)view.findViewById(R.id.list_item_beer_type_textview);
            this.abvView = (TextView) view.findViewById(R.id.detail_abv);


            view.setOnClickListener(this);
        }

        public int getBeerId() {
            return beerId;
        }

        public void setBeerId(int beerId) {
            this.beerId = beerId;
        }

        @Override
        public void onClick(View v) {


            Intent intent = new Intent(super.itemView.getContext(), DetailActivity.class)
                    .setData(BeerContract.BeerEntry.buildBeerUri(getBeerId()));
            super.itemView.getContext().startActivity(intent);
        }
    }
}
