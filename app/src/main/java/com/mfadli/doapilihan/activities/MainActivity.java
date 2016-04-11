package com.mfadli.doapilihan.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.mfadli.doapilihan.BuildConfig;
import com.mfadli.doapilihan.DoaPilihanApp;
import com.mfadli.doapilihan.R;
import com.mfadli.doapilihan.fragments.AboutFragment;
import com.mfadli.doapilihan.fragments.AdsSettingFragment;
import com.mfadli.doapilihan.fragments.MainActivityFragment;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements MainActivityFragment.OnMainFragmentItemClickListener,
        AdsSettingFragment.OnAdsSettingFragmentListener {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final String STATE_SELECTED_DRAWER = "state_selected_drawer";
    private int mCurrentSelectedPosition = 0;
    private Bundle mReenterState;

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @Bind(R.id.main_nav_view)
    NavigationView mNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        setUpNavDrawer();

        if (savedInstanceState != null) {
            mCurrentSelectedPosition = savedInstanceState.getInt(STATE_SELECTED_DRAWER, 0);
            Menu menu = mNavigationView.getMenu();
            menu.getItem(mCurrentSelectedPosition).setChecked(true);
            TextView tvTitle = (TextView) mToolbar.findViewById(R.id.toolbar_title);
            tvTitle.setText(menu.getItem(mCurrentSelectedPosition).getTitle());
        } else {

            MainActivityFragment fragment = MainActivityFragment.newInstance();

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.main_content, fragment)
                    .commit();

            TextView tvTitle = (TextView) mToolbar.findViewById(R.id.toolbar_title);
            tvTitle.setText(R.string.drawer_title_home);
        }

        mNavigationView.setNavigationItemSelectedListener(menuItem -> {
            processSelectedItem(menuItem);
            return true;
        });

        shouldShowAds(((DoaPilihanApp) DoaPilihanApp.getContext()).shouldShowAds());
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_SELECTED_DRAWER, mCurrentSelectedPosition);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        mReenterState = new Bundle(data.getExtras());
        int originalPosition = mReenterState.getInt(DetailActivity.EXTRA_ORIGINAL_POSITION);
        int currentPosition = mReenterState.getInt(DetailActivity.EXTRA_CURRENT_POSITION);

        if (originalPosition != currentPosition) {
            ((MainActivityFragment) getSupportFragmentManager().findFragmentById(R.id.main_content))
                    .scrollToPosition(currentPosition);
        }
    }

    @Override
    public void onMainFragmentItemClick(int position, FrameLayout titleFrame) {
        DetailActivity.start(this, position, titleFrame);
    }

    @Override
    public void onClickShowAds() {
        shouldShowAds(true);
    }

    @Override
    public void onClickHideAds() {
        shouldShowAds(false);
    }

    /**
     * To add or remove the actual ads view container and disable/enable the ads.
     *
     * @param display boolean True to display ads.
     */
    private void shouldShowAds(boolean display) {

        if (display) {
//            AdRequest adRequest = new AdRequest.Builder()
//                    .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
//                    .addTestDevice("ABC1234567890ABC1234567890")
//                    .build();

            RelativeLayout adsContainer = (RelativeLayout) findViewById(R.id.main_layout);

            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            lp.addRule(RelativeLayout.CENTER_HORIZONTAL);

            com.google.android.gms.ads.AdView mAdView = new com.google.android.gms.ads.AdView(this);
            mAdView.setId(R.id.adView);
            mAdView.setAdSize(AdSize.BANNER);
            mAdView.setAdUnitId(getString(R.string.banner_ad_unit_id));
            mAdView.setLayoutParams(lp);

            adsContainer.addView(mAdView);

            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);

        } else {
            RelativeLayout adsContainer = (RelativeLayout) findViewById(R.id.main_layout);

            AdView adMobAds = (AdView) this.findViewById(R.id.adView);
            adsContainer.removeView(adMobAds);

        }

    }

    /**
     * Configure Toolbar and Drawer Layout.
     * Add burger icon and click listener to open the drawer.
     * Add app version to header.
     */
    private void setUpNavDrawer() {
        if (mToolbar != null) {
            IconicsDrawable icon = new IconicsDrawable(this)
                    .icon(GoogleMaterial.Icon.gmd_menu)
                    .color(getResources().getColor(R.color.md_white_1000))
                    .sizeDp(24);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            mToolbar.setNavigationIcon(icon);
            mToolbar.setNavigationOnClickListener(v -> mDrawerLayout.openDrawer(GravityCompat.START));

            View headerLayout = mNavigationView.getHeaderView(0);
            TextView tvVersion = (TextView) headerLayout.findViewById(R.id.drawer_header_version);
            tvVersion.setText(BuildConfig.VERSION_NAME);
        }
    }

    /**
     * From menu item provided, will check for which item is being click,
     * ignore item which is already selected.
     * Replace fragment according to selected item and close the drawer.
     *
     * @param menuItem {@link MenuItem}
     */
    private void processSelectedItem(MenuItem menuItem) {
        Fragment fragment = null;
        Class fragmentClass;
        int position = 0;

        switch (menuItem.getItemId()) {
            case R.id.navigation_item_home:
                position = 0;
                fragmentClass = MainActivityFragment.class;
                break;
            case R.id.navigation_item_ads_setting:
                position = 1;
                fragmentClass = AdsSettingFragment.class;
                break;
            case R.id.navigation_item_about:
                position = 2;
                fragmentClass = AboutFragment.class;
                break;
            default:
                fragmentClass = MainActivityFragment.class;
        }

        if (mCurrentSelectedPosition == position) {
            return;
        } else {
            mCurrentSelectedPosition = position;
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_content, fragment)
                .commit();

        menuItem.setChecked(true);
        mDrawerLayout.closeDrawer(GravityCompat.START);

        TextView tvTitle = (TextView) mToolbar.findViewById(R.id.toolbar_title);
        tvTitle.setText(menuItem.getTitle());
    }

}
