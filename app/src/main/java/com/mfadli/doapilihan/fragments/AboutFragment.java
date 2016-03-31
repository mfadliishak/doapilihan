package com.mfadli.doapilihan.fragments;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mfadli.doapilihan.BuildConfig;
import com.mfadli.doapilihan.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AboutFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AboutFragment extends Fragment {
    private static final String LOG_TAG = AboutFragment.class.getSimpleName();

    @Bind(R.id.about_version)
    TextView mVersion;

    public AboutFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment.
     *
     * @return A new instance of fragment AboutFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AboutFragment newInstance() {
        AboutFragment fragment = new AboutFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about, container, false);
        ButterKnife.bind(this, view);

        mVersion.setText("Version " + BuildConfig.VERSION_NAME);

        return view;
    }

    /**
     * Open URL in a view using intent
     *
     * @param url String
     */
    private void openUrl(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }

    /**
     * Email support callback
     *
     * @see View.OnClickListener
     */
    @OnClick(R.id.email_support)
    void onClickEmailSupport(View view) {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + "fadliishak@gmail.com"));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.email_helpdesk));
        startActivity(Intent.createChooser(emailIntent, getString(R.string.email_intent_title)));
    }

    /**
     * Open URL for library 1
     *
     * @see View.OnClickListener
     */
    @OnClick(R.id.library_1)
    void onClickLibrary1(View view) {
        openUrl(getString(R.string.library_url_1));
    }

    @OnClick(R.id.library_2)
    void onClickLibrary2(View view) {
        openUrl(getString(R.string.library_url_2));
    }

    @OnClick(R.id.library_3)
    void onClickLibrary3(View view) {
        openUrl(getString(R.string.library_url_3));
    }

    @OnClick(R.id.reference_1)
    void onClickReference1(View view) {
        openUrl(getString(R.string.reference_url_1));
    }

    @OnClick(R.id.reference_2)
    void onClickReference2(View view) {
        openUrl(getString(R.string.reference_url_2));
    }

    @OnClick(R.id.reference_3)
    void onClickReference3(View view) {
        openUrl(getString(R.string.reference_url_3));
    }

    @OnClick(R.id.reference_4)
    void onClickReference4(View view) {
        openUrl(getString(R.string.reference_url_4));
    }

    @OnClick(R.id.reference_5)
    void onClickReference5(View view) {
        openUrl(getString(R.string.reference_url_5));
    }

    @OnClick(R.id.reference_6)
    void onClickReference6(View view) {
        openUrl(getString(R.string.reference_url_6));
    }

}
