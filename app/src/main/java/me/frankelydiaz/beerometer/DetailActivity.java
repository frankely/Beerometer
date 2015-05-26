package me.frankelydiaz.beerometer;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;


public class DetailActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_detail);

        setTitle(R.string.title_activity_detail);

        if (savedInstanceState == null) {

            Bundle arguments = new Bundle();
            arguments.putParcelable(DetailActivityFragment.DETAIL_URI, getIntent().getData());

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container_detail, DetailActivityFragment.newInstance(arguments))
                    .commit();
        }
    }
}
