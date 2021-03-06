package com.mfadli.doapilihan.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.mfadli.doapilihan.DoaPilihanApp;
import com.mfadli.doapilihan.R;
import com.mfadli.doapilihan.adapter.FontListArrayAdapter;
import com.mfadli.doapilihan.event.GeneralEvent;
import com.mfadli.doapilihan.event.RxBus;
import com.mfadli.doapilihan.model.BGPattern;
import com.mfadli.doapilihan.model.DoaDetail;
import com.mfadli.doapilihan.model.Font;
import com.mfadli.utils.Analytic;
import com.mfadli.utils.BitmapCacher;
import com.mfadli.utils.Common;
import com.mikepenz.iconics.view.IconicsImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by mfad on 29/03/2016.
 */
public class DetailFragment extends Fragment {

    private static final String ARG_DOA_DETAIL = "DoaDetail";
    private static final String LOG_TAG = DetailFragment.class.getSimpleName();
    private DoaDetail mDoaDetail;
    private OnDetailFragmentListener mListener;
    private RxBus mRxBus;
    private CompositeSubscription mSubscription;

    @BindView(R.id.detail_title)
    TextView mTitle;
    @BindView(R.id.detail_title_frame)
    FrameLayout mFrameLayout;
    @BindView(R.id.img_translate_icon)
    IconicsImageView mImgTranslate;
    @BindView(R.id.detail_left_icon)
    com.mikepenz.iconics.view.IconicsImageView mLeftIcon;
    @BindView(R.id.detail_right_icon)
    com.mikepenz.iconics.view.IconicsImageView mRightIcon;
    @BindView(R.id.detail_background)
    ImageView mBackground;

    public DetailFragment() {
    }

    public static DetailFragment newInstance(DoaDetail doaDetail) {
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_DOA_DETAIL, doaDetail);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mDoaDetail = getArguments().getParcelable(ARG_DOA_DETAIL);
        }
        mRxBus = ((DoaPilihanApp) DoaPilihanApp.getContext()).getRxBusSingleton();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_detail, null);
        ButterKnife.bind(this, view);

        if (getChildFragmentManager().findFragmentById(R.id.fragment_detail_doa) == null) {
            DetailDoaFragment doaFragment = DetailDoaFragment.newInstance(mDoaDetail.getDoa());
            DetailTranslationFragment translationFragment = DetailTranslationFragment.newInstance(mDoaDetail.getTranslation(), mDoaDetail.getTranslationEn());
            DetailReferenceFragment referenceFragment = DetailReferenceFragment.newInstance(mDoaDetail.getReference(), mDoaDetail.getUrl(), mDoaDetail.getType());

            getChildFragmentManager().beginTransaction()
                    .add(R.id.fragment_detail_doa, doaFragment)
                    .add(R.id.fragment_detail_translation, translationFragment)
                    .add(R.id.fragment_detail_reference, referenceFragment)
                    .commit();
        }

        mTitle.setText(mDoaDetail.getId() + ". " + Common.trimBreakLine(mDoaDetail.getTitle()));

        // Color the translation icon accordingly
        DoaPilihanApp app = (DoaPilihanApp) DoaPilihanApp.getContext();
        resetTranslateIcon(app.isEnglishTranslation());

        configureBackground(app.getBgPattern());

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(ARG_DOA_DETAIL, mDoaDetail);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnDetailFragmentListener) {
            mListener = (OnDetailFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + "must implement OnDetailFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onStart() {
        super.onStart();
        mSubscription = new CompositeSubscription();

        // subscribe to SuccessSaveFontSize
        mSubscription
                .add(mRxBus.toObserverable()
                        .subscribe(event -> {
                            if (event instanceof GeneralEvent.SuccessSaveTranslation) {
                                GeneralEvent.SuccessSaveTranslation ev = (GeneralEvent.SuccessSaveTranslation) event;
                                onSuccessSaveTranslation(ev.isEnglish());
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
     * Callback for {@link GeneralEvent.SuccessSaveTranslation}.
     * Change Translation Icon and visibility.
     *
     * @param isEnglish boolean
     */
    private void onSuccessSaveTranslation(boolean isEnglish) {
        DetailTranslationFragment fragment = (DetailTranslationFragment) getChildFragmentManager()
                .findFragmentById(R.id.fragment_detail_translation);

        fragment.changeTranslationVisibility(isEnglish);

        resetTranslateIcon(isEnglish);
    }

    /**
     * Reset Translate Icon to appropriate color.
     *
     * @param isEnglish boolean
     */
    private void resetTranslateIcon(boolean isEnglish) {
        if (isEnglish) {
            mImgTranslate.setColor(getResources().getColor(R.color.colorTranslationEnabled));
        } else {
            mImgTranslate.setColor(getResources().getColor(R.color.colorTranslationDisabled));
        }
    }

    /**
     * Disable Left Navigation Icon and make it invisible if true is given.
     * Used when first page off ViewPager is loaded.
     *
     * @param disable boolean
     */
    public void disableLeftIcon(boolean disable) {
        mLeftIcon.setEnabled(!disable);

        if (disable) {
            mLeftIcon.setVisibility(View.INVISIBLE);
        } else {
            mLeftIcon.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Disable Right Navigation Icon and make it invisible if true is given.
     * Used when last page of ViewPager is loaded.
     *
     * @param disable boolean
     */
    public void disableRightIcon(boolean disable) {
        mRightIcon.setEnabled(!disable);

        if (disable) {
            mRightIcon.setVisibility(View.INVISIBLE);
        } else {
            mRightIcon.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Click Font Size Icon Listener.
     * Trigger {@link DetailDoaFragment#onClickFontSize()}
     *
     * @see View.OnClickListener
     */
    @OnClick(R.id.img_font_size)
    void onClickFontSize(View view) {
        DetailDoaFragment fragment = (DetailDoaFragment) getChildFragmentManager()
                .findFragmentById(R.id.fragment_detail_doa);

        fragment.onClickFontSize();
    }

    /**
     * Click Line Spacing Icon Listener.
     * Trigger {@link DetailDoaFragment#onClickLineSpacing()}
     *
     * @see View.OnClickListener
     */
    @OnClick(R.id.img_line_spacing)
    void onClickLineSpacing(View view) {
        DetailDoaFragment fragment = (DetailDoaFragment) getChildFragmentManager()
                .findFragmentById(R.id.fragment_detail_doa);

        fragment.onClickLineSpacing();
    }

    /**
     * Click Translate Icon Listener.
     * Trigger {@link DetailDoaFragment#onClickFontSize()}
     *
     * @see View.OnClickListener
     */
    @OnClick(R.id.img_translate)
    void onClickTranslate(View view) {
        DoaPilihanApp app = (DoaPilihanApp) DoaPilihanApp.getContext();
        boolean isEnglish = !app.isEnglishTranslation();
        app.saveEnglishTranslation(isEnglish);

        if (mRxBus.hasObservers()) {
            mRxBus.send(new GeneralEvent.SuccessSaveTranslation(isEnglish));
        }
        Analytic.sendEvent(Analytic.EVENT_BUTTON, "Translate", isEnglish ? "English" : "Bahasa");

    }

    /**
     * Click Font Type Icon Listener.
     * Show list of available fonts.
     *
     * @see View.OnClickListener
     */
    @OnClick(R.id.img_font_type)
    void onClickFontType(View view) {

        final List<Font> fontList = ((DoaPilihanApp) DoaPilihanApp.getContext()).getFonts();
        final int fontType = ((DoaPilihanApp) DoaPilihanApp.getContext()).getDoaFontType();
        final ArrayAdapter<Font> adapter = new FontListArrayAdapter(getContext(), fontList);


        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        dialogBuilder.setTitle(R.string.dialog_doa_font_type_label);
        dialogBuilder.setNegativeButton(R.string.action_cancel, null);
        dialogBuilder.setSingleChoiceItems(adapter, fontType, (dialog, which) -> {

            ((DoaPilihanApp) DoaPilihanApp.getContext()).saveDoaFontType(which);
            Analytic.sendEvent(Analytic.EVENT_BUTTON, getString(R.string.dialog_doa_font_type_label), fontList.get(which).getName());

            if (mRxBus.hasObservers()) {
                mRxBus.send(new GeneralEvent.SuccessSaveFontType(fontList.get(which)));
            }

            dialog.dismiss();
        });
        dialogBuilder.create().show();
    }

    /**
     * Click Listener when Left Navigation Icon is clicked
     *
     * @see View.OnClickListener
     */
    @OnClick(R.id.detail_left_icon)
    void onClickLeftIcon(View view) {
        mListener.onDetailFragmentLeftClick();
    }

    /**
     * Click Listener when Right Navigation Icon is clicked
     *
     * @see View.OnClickListener
     */
    @OnClick(R.id.detail_right_icon)
    void onClickRightIcon(View view) {
        mListener.onDetailFragmentRightClick();
    }

    /**
     * Interface to communicate with DetailActivity
     */
    public interface OnDetailFragmentListener {
        /**
         * Callback when Left navigation Icon is clicked
         */
        void onDetailFragmentLeftClick();

        /**
         * Callback when Right navigation Icon is clicked
         */
        void onDetailFragmentRightClick();
    }
}
