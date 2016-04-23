package com.mfadli.doapilihan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mfadli.doapilihan.R;
import com.mfadli.doapilihan.model.BGPattern;

import java.util.List;

/**
 * Created by mfad on 24/04/2016.
 */
public class BGPatternListArrayAdapter extends ArrayAdapter<BGPattern> {
private final List<BGPattern> mBgPatternList;
private Context mContext;

static class ViewHolder {
    protected ImageView mImageView;
    protected TextView mTextView;
}

    public BGPatternListArrayAdapter(Context context, List<BGPattern> bgPatternList) {
        super(context, R.layout.list_font_type_view, bgPatternList);
        mContext = context;
        mBgPatternList = bgPatternList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;

        if (convertView == null) {
            LayoutInflater inflator = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflator.inflate(R.layout.list_pattern_view, null);
            final ViewHolder viewHolder = new ViewHolder();
            viewHolder.mImageView = (ImageView) view.findViewById(R.id.pattern_image);
            viewHolder.mTextView = (TextView) view.findViewById(R.id.pattern_title);
            view.setTag(viewHolder);
        } else {
            view = convertView;
        }

        if (mBgPatternList.get(position).getDrawable() > 0) {
            ViewHolder holder = (ViewHolder) view.getTag();
            holder.mImageView.setImageResource(mBgPatternList.get(position).getDrawableDialog());
            holder.mTextView.setText(mBgPatternList.get(position).getName());
        }

        return view;
    }
}
