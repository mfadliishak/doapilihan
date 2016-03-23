package com.mfadli.doapilihan;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.FrameLayout;

public class MainActivity extends AppCompatActivity implements MainActivityFragment.OnMainFragmentItemClickListener {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    @Override
    public void onMainFragmentItemClick(String title, String doa, String translation, FrameLayout titleFrame) {
        DetailActivity.start(this, title, doa, translation, titleFrame);
    }
}
