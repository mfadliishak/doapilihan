package com.mfadli.doapilihan.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mfadli.doapilihan.DoaPilihanApp;
import com.mfadli.doapilihan.R;
import com.mfadli.doapilihan.event.GeneralEvent;
import com.mfadli.doapilihan.event.RxBus;
import com.mfadli.doapilihan.model.BGPattern;
import com.mfadli.utils.Analytic;
import com.mfadli.utils.BitmapCacher;
import com.mfadli.utils.Common;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by mfad on 10/04/2016.
 */
public class AdsSettingFragment extends Fragment {
    private static final String LOG_TAG = AdsSettingFragment.class.getSimpleName();
    private static final String ARG_PRICE = "Price";
    private OnAdsSettingFragmentListener mListener;
    private RxBus mRxBus;
    private CompositeSubscription mSubscription;

    @Bind(R.id.fragment_ads_setting)
    RelativeLayout mLayout;
    @Bind(R.id.button_upgrade)
    Button mBtnUpgrade;
    @Bind(R.id.cardview_ads_upgrade)
    CardView mCardUpgrade;
    @Bind(R.id.cardview_ads_premium)
    CardView mCardPremium;
    @Bind(R.id.ads_background)
    ImageView mBackground;

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
        mRxBus = ((DoaPilihanApp) DoaPilihanApp.getContext()).getRxBusSingleton();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ads_setting, container, false);
        ButterKnife.bind(this, view);

        DoaPilihanApp app = (DoaPilihanApp) DoaPilihanApp.getContext();

        premiumUser(app.isPremium());

        configureBackground(app.getBgPattern());

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        mSubscription = new CompositeSubscription();

        // subscribe to SuccessSaveFontSize
        mSubscription
                .add(mRxBus.toObserverable()
                        .subscribe(event -> {
                            if (event instanceof GeneralEvent.SuccessPurchased) {
                                GeneralEvent.SuccessPurchased ev = (GeneralEvent.SuccessPurchased) event;
                                if (ev.isSuccess())
                                    premiumUser(ev.isPremium());
                            } else if (event instanceof GeneralEvent.SuccessSaveBGPattern) {
                                GeneralEvent.SuccessSaveBGPattern ev = (GeneralEvent.SuccessSaveBGPattern) event;
                                configureBackground(ev.getBgPattern());
                            }
                        }));
    }

    @Override
    public void onStop() {
        super.onStop();
        mSubscription.unsubscribe();
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
     * Change Background to selected pattern.
     *
     * @param bgPattern {@link BGPattern}
     */
    private void configureBackground(BGPattern bgPattern) {
        Bitmap bitmap = BitmapCacher.getCacheBitmap(bgPattern.getName());
        BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(), bitmap);
        bitmapDrawable.setTileModeY(Shader.TileMode.REPEAT);

        mBackground.setBackground(bitmapDrawable);
    }

    /**
     * If premium user, remove ads and show thanks card view.
     *
     * @param isPremium boolean
     */
    private void premiumUser(boolean isPremium) {

        if (isPremium) {
            DoaPilihanApp app = (DoaPilihanApp) DoaPilihanApp.getContext();

            mCardUpgrade.setVisibility(View.GONE);
            mCardPremium.setVisibility(View.VISIBLE);

            TextView tvArabic = (TextView) mCardPremium.findViewById(R.id.text_ads_upgraded_arabic_1);
            tvArabic.setText(Common.convertStringToFont(tvArabic.getText().toString(), app.getPremiumThanksFont().getPath()));

            TextView tvArabic2 = (TextView) mCardPremium.findViewById(R.id.text_ads_upgraded_arabic_2);
            tvArabic2.setText(Common.convertStringToFont(tvArabic2.getText().toString(), app.getPremiumThanksFont().getPath()));

        } else {
            mCardPremium.setVisibility(View.GONE);
            mCardUpgrade.setVisibility(View.VISIBLE);

            mBtnUpgrade.getBackground().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.MULTIPLY);
        }
    }

    /**
     * Click Callback when pressing Upgrade Button
     *
     * @see android.view.View.OnClickListener
     */
    @OnClick(R.id.button_upgrade)
    void onClickUpgrade(View view) {
        mListener.onClickUpgrade();
    }

    /**
     * Callback interface to communicate with Main Activity
     */
    public interface OnAdsSettingFragmentListener {
        void onClickUpgrade();
    }
}
