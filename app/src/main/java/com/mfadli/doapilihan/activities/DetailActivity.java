package com.mfadli.doapilihan.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.mfadli.doapilihan.DoaPilihanApp;
import com.mfadli.doapilihan.R;
import com.mfadli.doapilihan.adapter.DetailPagerAdapter;
import com.mfadli.doapilihan.data.repo.DoaDataRepo;
import com.mfadli.doapilihan.fragments.DetailFragment;
import com.mfadli.doapilihan.model.DoaDetail;
import com.mfadli.utils.Analytic;
import com.mfadli.utils.Common;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class DetailActivity extends BaseActivity implements DetailFragment.OnDetailFragmentListener {
    private static final String LOG_TAG = DetailActivity.class.getSimpleName();
    public static final String EXTRA_DOA = "Doa";
    public static final String EXTRA_ORIGINAL_POSITION = "ArgOriginalPosition";
    public static final String EXTRA_CURRENT_POSITION = "ArgCurrentPosition";

    private int mOriginalPosition;
    private int mCurrentPosition;
    private DetailPagerAdapter mPagerAdapter;

    @BindView(R.id.detail_view_pager)
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(((DoaPilihanApp) DoaPilihanApp.getContext()).isDarkTheme() ? R.style.AppTheme_NoActionBar : R.style.AppTheme_NoActionBar_Light);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        TextView tvTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        tvTitle.setText(R.string.app_name);

        DoaDataRepo doaDataRepo = new DoaDataRepo();
        List<DoaDetail> doaList = doaDataRepo.getAllDoa();

        mOriginalPosition = getIntent().getExtras().getInt(EXTRA_DOA);

        if (savedInstanceState != null) {
            mCurrentPosition = savedInstanceState.getInt(EXTRA_CURRENT_POSITION);
        } else {
            mCurrentPosition = mOriginalPosition;
            Analytic.sendScreen("Doa Selection~" + doaList.get(mCurrentPosition).getTitle());
        }

        mPagerAdapter = new DetailPagerAdapter(getSupportFragmentManager(), doaList);
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setCurrentItem(mCurrentPosition);
        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {

            boolean callHappened;

            @Override
            public void onPageSelected(int position) {
                mCurrentPosition = position;
                callHappened = false;
                Analytic.sendScreen("Detail View Pager~" + doaList.get(mCurrentPosition).getTitle());
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // If First Page and Last page, callback to disable left/right icon
                if ((position == 0 || position == mPagerAdapter.getCount() - 1) && !callHappened) {
                    callHappened = true;
                    triggerLastAndFirstPage();
                }
            }
        });

        shouldShowAds(((DoaPilihanApp) DoaPilihanApp.getContext()).shouldShowAds());
    }

    /**
     * Use this factory method to create an intent of the activity with
     * the provided parameters and start the activity.
     *
     * @param position   Parameter 1.
     * @param titleFrame Parameter 2.
     */
    public static void start(Context context, int position, FrameLayout titleFrame) {
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra(DetailActivity.EXTRA_DOA, position);
        ((MainActivity) context).startActivityForResult(intent, 1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finishWithResult();
                onBackPressed();
                return true;
            case R.id.action_change_mode:
                changeMode();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(EXTRA_CURRENT_POSITION, mCurrentPosition);
    }

    @Override
    public void onBackPressed() {
        finishWithResult();
        super.onBackPressed();
    }

    @Override
    public void onDetailFragmentLeftClick() {
        mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1);
    }

    @Override
    public void onDetailFragmentRightClick() {
        mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
    }

    /**
     * Change Theme from Menu pop up.
     * Restart MainActivity manually with flags and close this activity
     */
    private void changeMode() {
        DoaPilihanApp app = (DoaPilihanApp) DoaPilihanApp.getContext();
        boolean isDarkTheme = app.isDarkTheme();
        app.setIsDarkTheme(!isDarkTheme);

        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    /**
     * Configure intent to send back Original Position and current position
     * of View Pager to MainActivity so that can scroll RecyclerView
     * position accordingly.
     */
    private void finishWithResult() {
        Intent data = new Intent();
        data.putExtra(EXTRA_ORIGINAL_POSITION, mOriginalPosition);
        data.putExtra(EXTRA_CURRENT_POSITION, mCurrentPosition);
        setResult(RESULT_OK, data);
    }

    /**
     * To add or remove the actual ads view container and disable/enable the ads.
     *
     * @param display boolean True to display ads.
     */
    private void shouldShowAds(boolean display) {
        Log.d(LOG_TAG, "shouldShowAds: " + display);

        if (display) {
//            AdRequest adRequest = new AdRequest.Builder()
//                    .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
//                    .addTestDevice("ABC1234567890ABC1234567890")
//                    .build();

            RelativeLayout adsContainer = (RelativeLayout) findViewById(R.id.detail_layout);

            // Remove existing, to manually create a new one
            if (this.findViewById(R.id.adView) != null) {
                AdView adMobAds = (AdView) this.findViewById(R.id.adView);
                adsContainer.removeView(adMobAds);
            }

            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            lp.addRule(RelativeLayout.CENTER_HORIZONTAL);

            AdView mAdView = new AdView(this);
            mAdView.setId(R.id.adView);
            mAdView.setAdSize(AdSize.BANNER);
            mAdView.setAdUnitId(getString(R.string.banner_ad_unit_id));
            mAdView.setLayoutParams(lp);

            adsContainer.addView(mAdView);

            AdRequest adRequest = new AdRequest
                    .Builder()
                    .addTestDevice("AABA3F9975AC4BBE5AB069468B4BFD7D")
                    .build();
            mAdView.loadAd(adRequest);

            mViewPager.setPadding(0, 0, 0, Common.dpToPixel(50));

        } else {
            RelativeLayout adsContainer = (RelativeLayout) findViewById(R.id.detail_layout);

            AdView adMobAds = (AdView) this.findViewById(R.id.adView);
            adsContainer.removeView(adMobAds);

            mViewPager.setPadding(0, 0, 0, 0);
        }

    }

    /**
     * Get the current fragment in ViewPager and disable left/right navigation Icon
     * if the current page is first or last page.
     */
    private void triggerLastAndFirstPage() {
        DetailFragment fragment = getFragmentByPosition(mCurrentPosition);

        if (fragment != null) {
            if (mCurrentPosition == 0) {
                fragment.disableLeftIcon(true);
            } else if (mCurrentPosition == mPagerAdapter.getCount() - 1) {
                fragment.disableRightIcon(true);
            }

        }
    }

    /**
     * Get Fragment inside ViewPager by page index.
     *
     * @param pos int position/index of ViewPager
     * @return DetailFragment
     */
    private DetailFragment getFragmentByPosition(int pos) {
        String tag = "android:switcher:" + mViewPager.getId() + ":" + pos;
        return (DetailFragment) getSupportFragmentManager().findFragmentByTag(tag);
    }
}
