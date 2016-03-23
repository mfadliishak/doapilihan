package com.mfadli.doapilihan;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements MainActivityFragment.OnMainFragmentItemClickListener {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final String STATE_SELECTED_DRAWER = "state_selected_drawer";
    private int mCurrentSelectedPosition = 0;

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @Bind(R.id.main_nav_view)
    NavigationView mNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        getWindow().requestFeature(android.view.Window.FEATURE_CONTENT_TRANSITIONS);
//        getWindow().requestFeature(android.view.Window.FEATURE_ACTIVITY_TRANSITIONS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
        }

        setUpNavDrawer();

        if (savedInstanceState != null) {
            mCurrentSelectedPosition = savedInstanceState.getInt(STATE_SELECTED_DRAWER);
        } else {
            MainActivityFragment fragment = MainActivityFragment.newInstance();

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.main_content, fragment)
                    .commit();

            setTitle(R.string.drawer_title_home);
        }

        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                processSelectedItem(menuItem);
                return true;
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_SELECTED_DRAWER, mCurrentSelectedPosition);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mCurrentSelectedPosition = savedInstanceState.getInt(STATE_SELECTED_DRAWER, 0);
        Menu menu = mNavigationView.getMenu();
        menu.getItem(mCurrentSelectedPosition).setChecked(true);
    }

    @Override
    public void onMainFragmentItemClick(String title, String doa, String translation, FrameLayout titleFrame) {
        DetailActivity.start(this, title, doa, translation, titleFrame);
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
            case R.id.navigation_item_about:
                position = 1;
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
        mToolbar.setTitle(menuItem.getTitle());
        mDrawerLayout.closeDrawer(GravityCompat.START);
    }

}
