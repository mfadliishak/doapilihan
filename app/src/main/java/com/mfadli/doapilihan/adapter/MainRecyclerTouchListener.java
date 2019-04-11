package com.mfadli.doapilihan.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by mfad on 25/04/2016.
 */
public class MainRecyclerTouchListener implements RecyclerView.OnItemTouchListener {
    private GestureDetector mGestureDetector;
    private MainRecyclerClickListener mRecyclerClickListener;

    public MainRecyclerTouchListener(Context context, RecyclerView recyclerView, MainRecyclerClickListener recyclerClickListener) {
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
