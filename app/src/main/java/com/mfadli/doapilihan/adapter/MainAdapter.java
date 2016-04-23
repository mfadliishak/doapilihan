package com.mfadli.doapilihan.adapter;

import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.mfadli.doapilihan.R;
import com.mfadli.doapilihan.model.BGPattern;
import com.mfadli.doapilihan.model.DoaDetail;
import com.mfadli.utils.Common;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by mfad on 22/03/2016.
 */
public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {

    private static final String LOG_TAG = MainAdapter.class.getSimpleName();
    private List<DoaDetail> mList;
    private BGPattern mBGPattern;

    public MainAdapter(List<DoaDetail> doaDetailList, BGPattern bgPattern) {
        mList = doaDetailList;
        mBGPattern = bgPattern;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_main_view, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        DoaDetail doaDetail = mList.get(position);
        final String title = Common.trimBreakLine(doaDetail.getTitle());
        holder.mTvTitle.setText(title);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            holder.mFrameLayout.setTransitionName(title);
        }
        holder.mFrameLayout.setTag(title);
        holder.mImageView.setImageResource(mBGPattern.getDrawable());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    /**
     * Set BGPattern and refresh the view.
     *
     * @param bgPattern {@link BGPattern}
     */
    public void setBgPattern(BGPattern bgPattern) {
        if (mBGPattern != bgPattern) {
            mBGPattern = bgPattern;
            notifyDataSetChanged();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @Nullable
        @Bind(R.id.main_title)
        TextView mTvTitle;
        @Bind(R.id.detail_title_frame)
        FrameLayout mFrameLayout;
        @Bind(R.id.detail_pattern)
        ImageView mImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    /**
     * Direct access to DoaDetail by giving position/index value.
     *
     * @param position int Index/Position
     * @return {@link DoaDetail}
     */
    public DoaDetail getItem(int position) {
        return mList.get(position);
    }


}
