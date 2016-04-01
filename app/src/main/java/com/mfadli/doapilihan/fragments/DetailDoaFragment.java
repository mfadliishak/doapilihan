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

import com.mfadli.doapilihan.DoaPilihanApp;
import com.mfadli.doapilihan.R;
import com.mfadli.doapilihan.event.GeneralEvent;
import com.mfadli.doapilihan.event.RxBus;
import com.mfadli.doapilihan.model.Font;
import com.mfadli.utils.Common;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.subscriptions.CompositeSubscription;

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
    private int mSelectedLineSpacingSize = 0;
    private float mOriginalLineSpacingSize = 0;
    private RxBus mRxBus;
    private CompositeSubscription mSubscription;

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
        mRxBus = ((DoaPilihanApp) DoaPilihanApp.getContext()).getRxBusSingleton();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_doa, container, false);
        ButterKnife.bind(this, view);

        getFontSize();
        getLineSpacingSize();

        reloadScreen();

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
                            if (event instanceof GeneralEvent.SuccessSaveFontSize) {
                                GeneralEvent.SuccessSaveFontSize ev = (GeneralEvent.SuccessSaveFontSize) event;
                                resetFontSize(ev.getOriginalSize());
                            } else if (event instanceof GeneralEvent.SuccessSaveLineSpacingSize) {
                                GeneralEvent.SuccessSaveLineSpacingSize ev = (GeneralEvent.SuccessSaveLineSpacingSize) event;
                                resetLineSpacingSize(ev.getOriginalSize());
                            } else if (event instanceof GeneralEvent.SuccessSaveFontType) {
                                GeneralEvent.SuccessSaveFontType ev = (GeneralEvent.SuccessSaveFontType) event;
                                resetFontType(ev.getFont());
                            }
                        }));
    }

    @Override
    public void onStop() {
        super.onStop();
        mSubscription.unsubscribe();
    }

    /**
     * Callback for {@link GeneralEvent.SuccessSaveFontSize}
     *
     * @param originalSize float
     */
    private void resetFontSize(float originalSize) {
        getFontSize();
        mTvDoa.setTextSize(TypedValue.COMPLEX_UNIT_SP, originalSize);
    }

    /**
     * Callback for {@link GeneralEvent.SuccessSaveLineSpacingSize}
     *
     * @param originalSize float
     */
    private void resetLineSpacingSize(float originalSize) {
        getLineSpacingSize();
        mTvDoa.setLineSpacing(originalSize, 1.0f);
    }

    /**
     * Callback for {@link GeneralEvent.SuccessSaveFontType}
     *
     * @param font {@link Font}
     */
    private void resetFontType(Font font) {
        mTvDoa.setText(Common.convertStringToFont(mDoa, font.getPath()));
    }

    /**
     * Handy function to set back Doa TextView
     */
    private void reloadScreen() {
        DoaPilihanApp app = (DoaPilihanApp) DoaPilihanApp.getContext();
        int fontType = app.getDoaFontType();
        Font font = app.getFonts().get(fontType);

        resetFontType(font);

        mTvDoa.setTextSize(TypedValue.COMPLEX_UNIT_SP, mOriginalSize + mSelectedSize);
        mTvDoa.setLineSpacing(mOriginalLineSpacingSize + mSelectedLineSpacingSize, 1.0f);
    }

    /**
     * Get Selection value (Seekbar) from local save and Get original font size of Doa TextView
     */
    private void getFontSize() {
        mSelectedSize = ((DoaPilihanApp) getActivity().getApplication()).getDoaFontSize();
        mOriginalSize = mTvDoa.getTextSize() / getResources().getDisplayMetrics().scaledDensity;
    }

    /**
     * Get Selection value (Seekbar) from local save and Get original line spacing of Doa TextView
     */
    private void getLineSpacingSize() {
        mSelectedLineSpacingSize = ((DoaPilihanApp) getActivity().getApplication()).getDoaLineSpacingSize();
        mOriginalLineSpacingSize = mTvDoa.getLineSpacingExtra();
    }

    /**
     * Show Dialog to change font size of the doa.
     */
    public void onClickFontSize() {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = LayoutInflater.from(getContext());
        final View dialogView = inflater.inflate(R.layout.dialog_doa_font, null);
        dialogBuilder.setTitle(R.string.dialog_doa_font_label);
        dialogBuilder.setView(dialogView);

        final SeekBar seekBar = (SeekBar) dialogView.findViewById(R.id.dialog_seekbar_doa_font);
        final TextView tvFont = (TextView) dialogView.findViewById(R.id.dialog_font_text);

        getFontSize();

        tvFont.setText("" + (int) mOriginalSize);
        seekBar.setProgress(mSelectedSize);

        mCurrentProgress = mSelectedSize;

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            float calculatedSize = 0.0f;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mCurrentProgress = progress;
                calculatedSize = mOriginalSize + (mCurrentProgress - mSelectedSize);

                tvFont.setText("" + (int) calculatedSize);

                mTvDoa.setTextSize(TypedValue.COMPLEX_UNIT_SP, calculatedSize);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        dialogBuilder.setPositiveButton(getResources().getString(R.string.action_confirm), null);
        dialogBuilder.setNegativeButton(getResources().getString(R.string.action_cancel), null);
        AlertDialog dialog = dialogBuilder.create();
        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            ((DoaPilihanApp) getActivity().getApplication()).saveDoaFontSize(mCurrentProgress);

            // Broadcast SuccessSaveFontSize
            if (mRxBus.hasObservers()) {
                getFontSize();
                mRxBus.send(new GeneralEvent.SuccessSaveFontSize(mOriginalSize));
            }

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

    /**
     * Show Dialog to change line spacing of the doa.
     */
    public void onClickLineSpacing() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = LayoutInflater.from(getContext());
        final View dialogView = inflater.inflate(R.layout.dialog_doa_line_spacing, null);
        dialogBuilder.setTitle(R.string.dialog_doa_line_spacing_label);
        dialogBuilder.setView(dialogView);

        final SeekBar seekBar = (SeekBar) dialogView.findViewById(R.id.dialog_seekbar_doa_line_spacing);
        final TextView tvFont = (TextView) dialogView.findViewById(R.id.dialog_line_spacing_text);

        getLineSpacingSize();

        tvFont.setText("" + (int) mOriginalLineSpacingSize);
        seekBar.setProgress(mSelectedLineSpacingSize);

        mCurrentProgress = mSelectedLineSpacingSize;

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            float calculatedSize = 0.0f;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mCurrentProgress = progress;
                calculatedSize = mOriginalLineSpacingSize + (mCurrentProgress - mSelectedLineSpacingSize);

                tvFont.setText("" + (int) calculatedSize);
                mTvDoa.setLineSpacing(calculatedSize, 1.0f);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        dialogBuilder.setPositiveButton(getResources().getString(R.string.action_confirm), null);
        dialogBuilder.setNegativeButton(getResources().getString(R.string.action_cancel), null);
        AlertDialog dialog = dialogBuilder.create();
        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            ((DoaPilihanApp) getActivity().getApplication()).saveDoaLineSpacingSize(mCurrentProgress);

            // Broadcast SuccessSaveFontSize
            if (mRxBus.hasObservers()) {
                getLineSpacingSize();
                mRxBus.send(new GeneralEvent.SuccessSaveLineSpacingSize(mOriginalLineSpacingSize));
            }

            dialog.dismiss();
        });

        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(v -> {
            mTvDoa.setLineSpacing(mOriginalLineSpacingSize, 1.0f);
            dialog.dismiss();
        });

        dialog.setOnCancelListener(dialog1 -> {
            mTvDoa.setLineSpacing(mOriginalLineSpacingSize, 1.0f);
        });
    }

}
