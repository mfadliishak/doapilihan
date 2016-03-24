package com.mfadli.doapilihan.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.mfadli.doapilihan.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailDoaFragment extends Fragment {
    private static final String LOG_TAG = DetailDoaFragment.class.getSimpleName();
    private static final String ARG_DOA = "Doa";
    private String mDoa;
    private int mSelectedSize = 0;
    private float mOriginalSize = 0.0f;
    private int mCurrentProgress = 0;

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

    @OnClick(R.id.img_font_size)
    void onClickFontSize(View view) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = LayoutInflater.from(getContext());
        final View dialogView = inflater.inflate(R.layout.dialog_doa_font, null);
        dialogBuilder.setView(dialogView);

        final SeekBar seekBar = (SeekBar) dialogView.findViewById(R.id.dialog_seekbar_doa_font);
        final TextView tvFont = (TextView) dialogView.findViewById(R.id.dialog_font_text);

        mOriginalSize = mTvDoa.getTextSize() / getResources().getDisplayMetrics().scaledDensity;
        tvFont.setText("" + (int) mOriginalSize);
        seekBar.setProgress(mSelectedSize);

        mCurrentProgress = mSelectedSize;

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mCurrentProgress = progress;

                tvFont.setText("" + (int) (mOriginalSize + (mCurrentProgress - mSelectedSize)));

                mTvDoa.setTextSize(TypedValue.COMPLEX_UNIT_SP, mOriginalSize + (mCurrentProgress - mSelectedSize));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        dialogBuilder.setPositiveButton("Confirm", null);
        dialogBuilder.setNegativeButton("Cancel", null);
        AlertDialog dialog = dialogBuilder.create();
        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            mSelectedSize = mCurrentProgress;
            dialog.dismiss();
        });

        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(v -> {
            mTvDoa.setTextSize(TypedValue.COMPLEX_UNIT_SP, mOriginalSize);
            dialog.dismiss();
        });

        dialog.setOnCancelListener(dialog1 -> {
            mTvDoa.setTextSize(TypedValue.COMPLEX_UNIT_SP, mOriginalSize);
        });
    }
}
