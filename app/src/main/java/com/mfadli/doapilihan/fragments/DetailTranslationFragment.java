package com.mfadli.doapilihan.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mfadli.doapilihan.DoaPilihanApp;
import com.mfadli.doapilihan.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Fragment to show the translation part.
 * Use the {@link DetailTranslationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailTranslationFragment extends Fragment {
    private static final String LOG_TAG = DetailTranslationFragment.class.getSimpleName();
    private static final String ARG_TRANSLATION = "Translation";
    private static final String ARG_TRANSLATION_EN = "TranslationEn";
    private String mTranslation;
    private String mTranslationEn;

    @Bind(R.id.detail_translation)
    TextView mTvTranslation;
    @Bind(R.id.detail_translation_en)
    TextView mTvTranslationEn;

    public DetailTranslationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param translation   Parameter 1.
     * @param translationEn Parameter 2.
     * @return A new instance of fragment DetailTranslationFragment.
     */
    public static DetailTranslationFragment newInstance(String translation, String translationEn) {
        DetailTranslationFragment fragment = new DetailTranslationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TRANSLATION, translation);
        args.putString(ARG_TRANSLATION_EN, translationEn);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTranslation = getArguments().getString(ARG_TRANSLATION);
            mTranslationEn = getArguments().getString(ARG_TRANSLATION_EN);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_translation, container, false);
        ButterKnife.bind(this, view);

        reloadScreen();

        return view;
    }

    /**
     * Handy function to set back Translation TextView
     */
    private void reloadScreen() {
        mTvTranslation.setText(mTranslation);
        mTvTranslationEn.setText(mTranslationEn);

        changeTranslationVisibility(((DoaPilihanApp) getActivity().getApplication()).isEnglishTranslation());
    }

    /**
     * Change Translation malay <-> english
     */
    public void changeTranslationVisibility(boolean isEnglish) {
        if (isEnglish) {
            mTvTranslation.setVisibility(View.INVISIBLE);
            mTvTranslationEn.setVisibility(View.VISIBLE);
        } else {
            mTvTranslation.setVisibility(View.VISIBLE);
            mTvTranslationEn.setVisibility(View.INVISIBLE);
        }
    }

}
