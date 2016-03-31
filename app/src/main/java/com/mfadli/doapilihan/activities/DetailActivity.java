package com.mfadli.doapilihan.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.mfadli.doapilihan.R;
import com.mfadli.doapilihan.adapter.DetailPagerAdapter;
import com.mfadli.doapilihan.data.repo.DoaDataRepo;
import com.mfadli.doapilihan.fragments.DetailFragment;
import com.mfadli.doapilihan.model.DoaDetail;

import java.util.List;

import butterknife.Bind;
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

    @Bind(R.id.detail_view_pager)
    ViewPager mViewPager;

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

        DoaDataRepo doaDataRepo = new DoaDataRepo();
        List<DoaDetail> doaList = doaDataRepo.getAllDoa();

        mOriginalPosition = getIntent().getExtras().getInt(EXTRA_DOA);

        if (savedInstanceState != null) {
            mCurrentPosition = savedInstanceState.getInt(EXTRA_CURRENT_POSITION);
        } else {
            mCurrentPosition = mOriginalPosition;
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
