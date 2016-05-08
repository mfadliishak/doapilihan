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
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
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
import butterknife.OnCheckedChanged;
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
    @Bind(R.id.ads_switch)
    Switch mSwitch;
    @Bind(R.id.button_upgrade)
    Button mBtnUpgrade;
    @Bind(R.id.cardview_ads_manual)
    CardView mCardManual;
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
        if (((DoaPilihanApp) DoaPilihanApp.getContext()).isBgPatternNormal(bgPattern)) {
            mBackground.setImageResource(android.R.color.transparent);
        } else {
            Bitmap bitmap = BitmapCacher.getCacheBitmap(bgPattern.getName());

            BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(), bitmap);
            bitmapDrawable.setTileModeXY(Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);

            int color = Common.getColorFromThemeAttribute(getContext(), R.attr.themedPatternColorStyle);

            mBackground.setImageDrawable(bitmapDrawable);
            mBackground.setColorFilter(color);
        }
    }

    /**
     * If premium user, remove ads and show thanks card view.
     *
     * @param isPremium boolean
     */
    private void premiumUser(boolean isPremium) {

        if (isPremium) {
            DoaPilihanApp app = (DoaPilihanApp) DoaPilihanApp.getContext();

            mCardManual.setVisibility(View.GONE);
            mCardUpgrade.setVisibility(View.GONE);
            mCardPremium.setVisibility(View.VISIBLE);

            TextView tvArabic = (TextView) mCardPremium.findViewById(R.id.text_ads_upgraded_arabic_1);
            tvArabic.setText(Common.convertStringToFont(tvArabic.getText().toString(), app.getPremiumThanksFont().getPath()));

            TextView tvArabic2 = (TextView) mCardPremium.findViewById(R.id.text_ads_upgraded_arabic_2);
            tvArabic2.setText(Common.convertStringToFont(tvArabic2.getText().toString(), app.getPremiumThanksFont().getPath()));

        } else {
            mCardPremium.setVisibility(View.GONE);
            mCardManual.setVisibility(View.VISIBLE);
            mCardUpgrade.setVisibility(View.VISIBLE);

            mBtnUpgrade.getBackground().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.MULTIPLY);
        }

        shouldShowAds(((DoaPilihanApp) DoaPilihanApp.getContext()).shouldShowAds());
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
            Analytic.sendEvent(Analytic.EVENT_BUTTON, "ShowAds", "Show");
        } else {
            mListener.onClickHideAds();
            Analytic.sendEvent(Analytic.EVENT_BUTTON, "ShowAds", "Hide");
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
        void onClickShowAds();

        void onClickHideAds();

        void onClickUpgrade();

    }
}
