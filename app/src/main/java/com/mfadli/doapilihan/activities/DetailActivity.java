package com.mfadli.doapilihan.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.mfadli.doapilihan.R;
import com.mfadli.doapilihan.fragments.DetailDoaFragment;
import com.mfadli.doapilihan.fragments.DetailReferenceFragment;
import com.mfadli.doapilihan.fragments.DetailTranslationFragment;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DetailActivity extends BaseActivity {
    private static final String LOG_TAG = DetailActivity.class.getSimpleName();
    public static final String EXTRA_TITLE = "Title";
    public static final String EXTRA_DOA = "Doa";
    public static final String EXTRA_TRANSLATION = "Translation";
    public static final String EXTRA_REFERENCE = "Reference";
    public static final String EXTRA_URL = "Url";

    @Bind(R.id.detail_title)
    TextView mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        TextView tvTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        tvTitle.setText(R.string.app_name);

        if (savedInstanceState == null) {

            // Get the extras
            String title = getIntent().getExtras().getString(EXTRA_TITLE);
            String doa = getIntent().getExtras().getString(EXTRA_DOA);
            String translation = getIntent().getExtras().getString(EXTRA_TRANSLATION);
            String reference = getIntent().getExtras().getString(EXTRA_REFERENCE);
            String url = getIntent().getExtras().getString(EXTRA_URL);

            // Show the title
            mTitle.setText(title);

            // Create instances of doa and translation fragments
            DetailDoaFragment doaFragment = DetailDoaFragment.newInstance(doa);
            DetailTranslationFragment translationFragment = DetailTranslationFragment.newInstance(translation);
            DetailReferenceFragment referenceFragment = DetailReferenceFragment.newInstance(reference, url);

            // Show the fragments
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_detail_doa, doaFragment)
                    .add(R.id.fragment_detail_translation, translationFragment)
                    .add(R.id.fragment_detail_reference, referenceFragment)
                    .commit();
        } else {
            mTitle.setText(savedInstanceState.getString(EXTRA_TITLE));
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(EXTRA_TITLE, mTitle.getText().toString());
    }

    /**
     * Use this factory method to create an intent of the activity with
     * the provided parameters and start the activity.
     *
     * @param title       Parameter 1.
     * @param doa         Parameter 2.
     * @param translation Parameter 3.
     * @param reference   Parameter 4.
     * @param url         Parameter 5.
     * @param titleFrame  Parameter 6.
     */
    public static void start(Context context,
                             String title, String doa, String translation, String reference,
                             String url, FrameLayout titleFrame) {
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra(DetailActivity.EXTRA_TITLE, title);
        intent.putExtra(DetailActivity.EXTRA_DOA, doa);
        intent.putExtra(DetailActivity.EXTRA_TRANSLATION, translation);
        intent.putExtra(DetailActivity.EXTRA_REFERENCE, reference);
        intent.putExtra(DetailActivity.EXTRA_URL, url);

        // Shared Element Transition only for Lollipop and above.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptionsCompat options = ActivityOptionsCompat
                    .makeSceneTransitionAnimation(
                            (Activity) context, titleFrame, context.getString(R.string.title_frame_transition));
            context.startActivity(intent, options.toBundle());
        } else {
            context.startActivity(intent);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Click Font Size Icon Listener.
     * Trigger {@link DetailDoaFragment#onClickFontSize()}
     *
     * @param view
     */
    @OnClick(R.id.img_font_size)
    void onClickFontSize(View view) {
        DetailDoaFragment doaFragment = (DetailDoaFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_detail_doa);

        doaFragment.onClickFontSize();
    }
}
