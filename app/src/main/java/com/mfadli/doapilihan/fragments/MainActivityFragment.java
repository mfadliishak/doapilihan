package com.mfadli.doapilihan.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.mfadli.doapilihan.R;
import com.mfadli.doapilihan.activities.MainActivity;
import com.mfadli.doapilihan.adapter.MainAdapter;
import com.mfadli.doapilihan.model.DoaDetail;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private MainAdapter mMainAdapter;
    private OnMainFragmentItemClickListener mItemClickListener;

    @Bind(R.id.recycler_main_view)
    RecyclerView mRecyclerView;

    public MainActivityFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment.
     *
     * @return A new instance of fragment MainActivityFragment.
     */
    public static MainActivityFragment newInstance() {
        MainActivityFragment fragment = new MainActivityFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, view);

        mMainAdapter = new MainAdapter();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
//        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        mRecyclerView.setAdapter(mMainAdapter);
        mRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), mRecyclerView, new RecyclerClickListener() {
            @Override
            public void onClick(View view, int position) {
                DoaDetail doaDetail = mMainAdapter.getItem(position);
                FrameLayout titleFrame = (FrameLayout) view.findViewById(R.id.list_title_frame);

                if (mItemClickListener != null) {
                    mItemClickListener.onMainFragmentItemClick(doaDetail, titleFrame);
                }

            }

            @Override
            public void onLongClick(View view, int position) {
                Toast.makeText(getContext(), "long click", Toast.LENGTH_SHORT).show();
            }
        }));

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnMainFragmentItemClickListener) {
            mItemClickListener = (OnMainFragmentItemClickListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + "must implement OnMainFragmentItemClickListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mItemClickListener = null;
    }

    /**
     * RecyclerClickListener Interface
     */
    public interface RecyclerClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    /**
     * Recycler OnItemTouchListener class
     */
    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {
        private GestureDetector mGestureDetector;
        private RecyclerClickListener mRecyclerClickListener;

        public RecyclerTouchListener(Context context, RecyclerView recyclerView, RecyclerClickListener recyclerClickListener) {
            mRecyclerClickListener = recyclerClickListener;
            mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && mRecyclerClickListener != null) {
                        mRecyclerClickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && mRecyclerClickListener != null && mGestureDetector.onTouchEvent(e)) {
                mRecyclerClickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }

    /**
     * Interface to communicate with MainActivity
     */
    public interface OnMainFragmentItemClickListener {
        void onMainFragmentItemClick(DoaDetail doaDetail, FrameLayout titleFrame);
    }
}
