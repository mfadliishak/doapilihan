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
 * A placeholder fragment containing a simple view.
 */
public class DetailDoaFragment extends Fragment {
    private static final String LOG_TAG = DetailDoaFragment.class.getSimpleName();
    private static final String ARG_DOA = "Doa";
    private String mDoa;

    @Bind(R.id.detail_doa)
    TextView mTvDoa;

    public DetailDoaFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param doa Parameter 1.
     * @return A new instance of fragment DetailDoaFragment.
     */
    public static DetailDoaFragment newInstance(String doa) {
        DetailDoaFragment fragment = new DetailDoaFragment();
        Bundle args = new Bundle();
        args.putString(ARG_DOA, doa);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mDoa = getArguments().getString(ARG_DOA);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_doa, container, false);
        ButterKnife.bind(this, view);

        reloadScreen();

        return view;
    }

    /**
     * Handy function to set back Doa TextView
     */
    private void reloadScreen() {
        mTvDoa.setText(mDoa);
    }

}
