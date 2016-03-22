package com.mfadli.doapilihan;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mfadli.utils.Common;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import doa.mfadli.com.doapilihan.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private List<String> mList = new ArrayList<>();
    private MainAdapter mMainAdapter;

    @Bind(R.id.recycler_main_view)
    RecyclerView mRecyclerView;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, view);

        mMainAdapter = new MainAdapter(mList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        mRecyclerView.setAdapter(mMainAdapter);

        loadTitles();

        return view;
    }

    /**
     * Read from xml for the Doa titles and update the recycler view adapter.
     */
    private void loadTitles() {
        ArrayList<String> list = Common.getArrayListFromResource(getContext(), R.array.titles);
        for (String l : list) {
            mList.add(l);
        }
        mMainAdapter.notifyDataSetChanged();
    }
}
