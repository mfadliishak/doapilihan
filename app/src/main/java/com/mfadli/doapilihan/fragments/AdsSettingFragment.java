package com.mfadli.doapilihan.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Switch;

import com.mfadli.doapilihan.DoaPilihanApp;
import com.mfadli.doapilihan.R;
import com.mfadli.utils.Analytic;
import com.mfadli.utils.Common;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;

/**
 * Created by mfad on 10/04/2016.
 */
public class AdsSettingFragment extends Fragment {
    private static final String LOG_TAG = AdsSettingFragment.class.getSimpleName();
    private OnAdsSettingFragmentListener mListener;

    @Bind(R.id.fragment_ads_setting)
    RelativeLayout mLayout;
    @Bind(R.id.ads_switch)
    Switch mSwitch;

    public AdsSettingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment.
     *
     * @return A new instance of fragment AdsSettingFragment.
     */
    public static AdsSettingFragment newInstance() {
        AdsSettingFragment fragment = new AdsSettingFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            Analytic.sendScreen("AdsSetting");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ads_setting, container, false);
        ButterKnife.bind(this, view);

        shouldShowAds(((DoaPilihanApp) DoaPilihanApp.getContext()).shouldShowAds());

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnAdsSettingFragmentListener) {
            mListener = (OnAdsSettingFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + "must implement OnAdsSettingFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * To add or remove bottom padding for the ads banner.
     *
     * @param display boolean True to display ads.
     */
    private void shouldShowAds(boolean display) {
        mSwitch.setChecked(display);
        if (display) {
            mLayout.setPadding(0, 0, 0, Common.dpToPixel(50));
        } else {
            mLayout.setPadding(0, 0, 0, 0);
        }
    }

    /**
     * Callback for switch button
     *
     * @see android.widget.CompoundButton.OnCheckedChangeListener
     */
    @OnCheckedChanged(R.id.ads_switch)
    void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        ((DoaPilihanApp) DoaPilihanApp.getContext()).saveShouldShowAds(isChecked);
        shouldShowAds(isChecked);
        if (isChecked) {
            mListener.onClickShowAds();
        } else {
            mListener.onClickHideAds();
        }
    }

    /**
     * Callback interface to communicate with Main Activity
     */
    public interface OnAdsSettingFragmentListener {
        void onClickShowAds();

        void onClickHideAds();
    }
}
