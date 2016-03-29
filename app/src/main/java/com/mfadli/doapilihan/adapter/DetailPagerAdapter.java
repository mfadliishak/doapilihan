package com.mfadli.doapilihan.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.mfadli.doapilihan.fragments.DetailFragment;
import com.mfadli.doapilihan.model.DoaDetail;

import java.util.List;

/**
 * Created by mfad on 29/03/2016.
 */
public class DetailPagerAdapter extends FragmentPagerAdapter {
    private static final String LOG_TAG = DetailPagerAdapter.class.getSimpleName();
    private List<DoaDetail> mDoaDetails;
    private DetailFragment mCurrentFragment;

    public DetailPagerAdapter(FragmentManager fm, List<DoaDetail> doaDetails) {
        super(fm);
        mDoaDetails = doaDetails;
    }

    @Override
    public int getCount() {
        return mDoaDetails.size();
    }

    @Override
    public Fragment getItem(int position) {
        return DetailFragment.newInstance(mDoaDetails.get(position));
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        if (getCurrentFragment() != object) {
            mCurrentFragment = (DetailFragment) object;
        }
        super.setPrimaryItem(container, position, object);
    }

    public DetailFragment getCurrentFragment() {
        return mCurrentFragment;
    }
}
