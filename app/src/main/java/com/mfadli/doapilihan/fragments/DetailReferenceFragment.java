package com.mfadli.doapilihan.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mfadli.doapilihan.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by mfad on 25/03/2016.
 */
public class DetailReferenceFragment extends Fragment {
    private static final String LOG_TAG = DetailReferenceFragment.class.getSimpleName();
    private static final String ARG_REFERENCE = "Reference";
    private String mReference;

    @Bind(R.id.detail_reference)
    TextView mTvReference;

    public DetailReferenceFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param reference Parameter 1.
     * @return A new instance of fragment DetailTranslationFragment.
     */
    public static DetailReferenceFragment newInstance(String reference) {
        DetailReferenceFragment fragment = new DetailReferenceFragment();
        Bundle args = new Bundle();
        args.putString(ARG_REFERENCE, reference);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mReference = getArguments().getString(ARG_REFERENCE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_reference, container, false);
        ButterKnife.bind(this, view);

        reloadScreen();

        return view;
    }

    /**
     * Handy function to set back Translation TextView
     */
    private void reloadScreen() {
        mTvReference.setText(mReference);
    }

}