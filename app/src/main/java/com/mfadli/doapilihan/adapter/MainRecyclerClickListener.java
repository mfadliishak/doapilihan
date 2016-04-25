package com.mfadli.doapilihan.adapter;

import android.view.View;

/**
 * Created by mfad on 25/04/2016.
 * <p>
 * Interface Callback for MainRecyclerTouchListener tap or long click.
 */
public interface MainRecyclerClickListener {
    void onClick(View view, int position);

    void onLongClick(View view, int position);
}
