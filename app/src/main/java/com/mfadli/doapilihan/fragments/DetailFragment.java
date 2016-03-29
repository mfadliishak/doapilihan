package com.mfadli.doapilihan.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.mfadli.doapilihan.DoaPilihanApplication;
import com.mfadli.doapilihan.R;
import com.mfadli.doapilihan.model.DoaDetail;
import com.mfadli.utils.Common;
import com.mikepenz.iconics.view.IconicsImageView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by mfad on 29/03/2016.
 */
public class DetailFragment extends Fragment {

    private static final String ARG_DOA_DETAIL = "DoaDetail";
    private static final String LOG_TAG = DetailFragment.class.getSimpleName();
    private DoaDetail mDoaDetail;
    private OnDetailFragmentListener mListener;

    @Bind(R.id.detail_title)
    TextView mTitle;
    @Bind(R.id.detail_title_frame)
    FrameLayout mFrameLayout;
    @Bind(R.id.img_translate_icon)
    IconicsImageView mImgTranslate;
    @Bind(R.id.detail_left_icon)
    com.mikepenz.iconics.view.IconicsImageView mLeftIcon;
    @Bind(R.id.detail_right_icon)
    com.mikepenz.iconics.view.IconicsImageView mRightIcon;

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
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_detail, null);
        ButterKnife.bind(this, view);

        if (savedInstanceState != null) {
            mDoaDetail = savedInstanceState.getParcelable(ARG_DOA_DETAIL);
        } else {
            DetailDoaFragment doaFragment = DetailDoaFragment.newInstance(mDoaDetail.getDoa());
            DetailTranslationFragment translationFragment = DetailTranslationFragment.newInstance(mDoaDetail.getTranslation(), mDoaDetail.getTranslationEn());
            DetailReferenceFragment referenceFragment = DetailReferenceFragment.newInstance(mDoaDetail.getReference(), mDoaDetail.getUrl());

            getChildFragmentManager().beginTransaction()
                    .add(R.id.fragment_detail_doa, doaFragment)
                    .add(R.id.fragment_detail_translation, translationFragment)
                    .add(R.id.fragment_detail_reference, referenceFragment)
                    .commit();
        }

        mTitle.setText(Common.trimBreakLine(mDoaDetail.getTitle()));

        // Color the translation icon accordingly
        DoaPilihanApplication app = (DoaPilihanApplication) DoaPilihanApplication.getContext();
        resetTranslateIcon(app.isEnglishTranslation());

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

    /**
     * Reset Translate Icon to appropriate color.
     *
     * @param isEnglish boolean
     */
    private void resetTranslateIcon(boolean isEnglish) {
        if (isEnglish) {
            mImgTranslate.setColor(getResources().getColor(R.color.md_grey_600));
        } else {
            mImgTranslate.setColor(getResources().getColor(R.color.md_grey_400));
        }
    }

    /**
     * Click Font Size Icon Listener.
     * Trigger {@link DetailDoaFragment#onClickFontSize()}
     *
     * @param view
     */
    @OnClick(R.id.img_font_size)
    void onClickFontSize(View view) {
        DetailDoaFragment fragment = (DetailDoaFragment) getChildFragmentManager()
                .findFragmentById(R.id.fragment_detail_doa);

        fragment.onClickFontSize();
    }

    /**
     * Click Translate Icon Listener.
     * Trigger {@link DetailDoaFragment#onClickFontSize()}
     *
     * @param view
     */
    @OnClick(R.id.img_translate)
    void onClickTranslate(View view) {
        DoaPilihanApplication app = (DoaPilihanApplication) DoaPilihanApplication.getContext();
        boolean isEnglish = !app.isEnglishTranslation();
        app.saveEnglishTranslation(isEnglish);

        DetailTranslationFragment fragment = (DetailTranslationFragment) getChildFragmentManager()
                .findFragmentById(R.id.fragment_detail_translation);

        fragment.onClickTranslate(isEnglish);

        resetTranslateIcon(isEnglish);
    }

    /**
     * Click Listener when Left Navigation Icon is clicked
     *
     * @see android.view.View.OnClickListener
     */
    @OnClick(R.id.detail_left_icon)
    void onClickLeftIcon(View view) {
        mListener.onDetailFragmentLeftClick();
    }

    /**
     * Click Listener when Right Navigation Icon is clicked
     *
     * @see android.view.View.OnClickListener
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