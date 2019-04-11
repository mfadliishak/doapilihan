package com.mfadli.doapilihan.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.mfadli.doapilihan.DoaPilihanApp;
import com.mfadli.doapilihan.R;
import com.mfadli.doapilihan.model.BGPattern;
import com.mfadli.doapilihan.model.DoaDetail;
import com.mfadli.utils.BitmapCacher;
import com.mfadli.utils.Common;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by mfad on 22/03/2016.
 * <p>
 * Animate filter is from https://github.com/Wrdlbrnft/Searchable-RecyclerView-Demo
 */
public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {
    private static final String LOG_TAG = MainAdapter.class.getSimpleName();

    private final LayoutInflater mInflater;
    private final List<DoaDetail> mList;
    private BGPattern mBGPattern;
    private Context mContext;

    public MainAdapter(Context context, List<DoaDetail> doaDetailList, BGPattern bgPattern) {
        mList = new ArrayList<>(doaDetailList);
        mInflater = LayoutInflater.from(context);
        mBGPattern = bgPattern;
        mContext = context;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_main_view, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final DoaDetail doaDetail = mList.get(position);
        holder.bind(doaDetail);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public DoaDetail getItem(int position) {
        return mList.get(position);
    }

    private void applyAndAnimateRemovals(List<DoaDetail> newList) {
        for (int i = mList.size() - 1; i >= 0; i--) {
            final DoaDetail doaDetail = mList.get(i);
            if (!newList.contains(doaDetail)) {
                removeItem(i);
            }
        }
    }

    private void applyAndAnimateAdditions(List<DoaDetail> newList) {
        for (int i = 0, count = newList.size(); i < count; i++) {
            final DoaDetail doaDetail = newList.get(i);
            if (!mList.contains(doaDetail)) {
                addItem(i, doaDetail);
            }
        }
    }

    private void applyAndAnimateMovedItems(List<DoaDetail> newList) {
        for (int toPosition = newList.size() - 1; toPosition >= 0; toPosition--) {
            final DoaDetail doaDetail = newList.get(toPosition);
            final int fromPosition = mList.indexOf(doaDetail);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }

    public DoaDetail removeItem(int position) {
        final DoaDetail model = mList.remove(position);
        notifyItemRemoved(position);
        return model;
    }

    public void addItem(int position, DoaDetail doaDetail) {
        mList.add(position, doaDetail);
        notifyItemInserted(position);
    }

    public void moveItem(int fromPosition, int toPosition) {
        final DoaDetail doaDetail = mList.remove(fromPosition);
        mList.add(toPosition, doaDetail);
        notifyItemMoved(fromPosition, toPosition);
    }

    public void animateTo(List<DoaDetail> list) {
        applyAndAnimateRemovals(list);
        applyAndAnimateAdditions(list);
        applyAndAnimateMovedItems(list);
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

    /**
     * View Holder Class
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        @Nullable
        @BindView(R.id.main_title)
        TextView mTvTitle;
        @BindView(R.id.detail_title_frame)
        FrameLayout mFrameLayout;
        @BindView(R.id.detail_pattern)
        ImageView mImageView;
        @BindView(R.id.main_type)
        TextView mTvType;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(DoaDetail doaDetail) {
            final String title = doaDetail.getId() + ". " + Common.trimBreakLine(doaDetail.getTitle());

            mTvTitle.setText(title);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mFrameLayout.setTransitionName(title);
            }
            mFrameLayout.setTag(title);

            // Change Pattern background
            if (((DoaPilihanApp) DoaPilihanApp.getContext()).isBgPatternNormal(mBGPattern)) {
                mImageView.setImageResource(android.R.color.transparent);
            } else {
                Bitmap bitmap = BitmapCacher.getCacheBitmap(mBGPattern.getName());

                BitmapDrawable bitmapDrawable = new BitmapDrawable(mContext.getResources(), bitmap);
                bitmapDrawable.setTileModeXY(Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);

                int color = Common.getColorFromThemeAttribute(mContext, R.attr.themedPatternColorStyle);

                mImageView.setImageDrawable(bitmapDrawable);
                mImageView.setColorFilter(color);
            }

            // Change the Source Type Tag color.
            if (doaDetail.getType() == DoaDetail.SOURCE_TYPE_HADITH) {
                mTvType.setBackground(mContext.getResources().getDrawable(R.drawable.rounded_corner_h));
                mTvType.setText("H");
            } else if (doaDetail.getType() == DoaDetail.SOURCE_TYPE_QURAN) {
                mTvType.setBackground(mContext.getResources().getDrawable(R.drawable.rounded_corner_q));
                mTvType.setText("Q");
            }
        }
    }


}
