package com.mfadli.doapilihan.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.mfadli.doapilihan.R;
import com.mfadli.doapilihan.model.DoaDetail;
import com.mfadli.utils.Analytic;

import java.text.Bidi;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by mfad on 25/03/2016.
 */
public class DetailReferenceFragment extends Fragment {
    private static final String LOG_TAG = DetailReferenceFragment.class.getSimpleName();
    private static final String ARG_REFERENCE = "Reference";
    private static final String ARG_URL = "Url";
    private static final String ARG_TYPE = "Type";
    private String mReference;
    private String mUrl;
    private int mType;

    @Bind(R.id.detail_reference)
    TextView mTvReference;
    @Bind(R.id.detail_type)
    FrameLayout mLayoutType;

    public DetailReferenceFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param reference Parameter 1.
     * @param url       Parameter 2.
     * @param type      Parameter 3.
     * @return A new instance of fragment DetailTranslationFragment.
     */
    public static DetailReferenceFragment newInstance(String reference, String url, int type) {
        DetailReferenceFragment fragment = new DetailReferenceFragment();
        Bundle args = new Bundle();
        args.putString(ARG_REFERENCE, reference);
        args.putString(ARG_URL, url);
        args.putInt(ARG_TYPE, type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mReference = getArguments().getString(ARG_REFERENCE);
            mUrl = getArguments().getString(ARG_URL);
            mType = getArguments().getInt(ARG_TYPE);
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
     * Handy function to set back Translation TextView.
     * Change the Type color of the reference.
     * Check the reference if LTR, remove its gravity.
     */
    private void reloadScreen() {
        mTvReference.setText(mReference);

        if (mType == DoaDetail.SOURCE_TYPE_HADITH) {
            mLayoutType.setBackground(getContext().getResources().getDrawable(R.drawable.rounded_corner_h));
        } else if (mType == DoaDetail.SOURCE_TYPE_QURAN) {
            mLayoutType.setBackground(getContext().getResources().getDrawable(R.drawable.rounded_corner_q));
        }

        Bidi bidi = new Bidi(mReference, Bidi.DIRECTION_DEFAULT_LEFT_TO_RIGHT);

        if (bidi.getBaseLevel() == 0) {
            mTvReference.setGravity(Gravity.NO_GRAVITY);
        } else {
            mTvReference.setGravity(Gravity.END);
        }
    }

    /**
     * Open URL when reference fram TextView/ImageView is clicked.
     *
     * @see android.view.View.OnClickListener
     */
    @OnClick(R.id.detail_reference_frame)
    void onClickReference(View view) {
        Analytic.sendEvent(Analytic.EVENT_BUTTON, "Reference", mReference);

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(mUrl));
        startActivity(intent);
    }

}