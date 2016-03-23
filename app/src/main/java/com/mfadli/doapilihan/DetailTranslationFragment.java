package com.mfadli.doapilihan;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
    private String mTranslation;

    @Bind(R.id.detail_translation)
    TextView mTvTranslation;

    public DetailTranslationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param translation Parameter 1.
     * @return A new instance of fragment DetailTranslationFragment.
     */
    public static DetailTranslationFragment newInstance(String translation) {
        DetailTranslationFragment fragment = new DetailTranslationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TRANSLATION, translation);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTranslation = getArguments().getString(ARG_TRANSLATION);
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
    }

}
