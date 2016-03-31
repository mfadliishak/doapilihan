package com.mfadli.doapilihan.adapter;

import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mfadli.doapilihan.R;
import com.mfadli.doapilihan.model.Font;
import com.mfadli.utils.Common;

import java.util.List;

/**
 * Created by mfad on 31/03/2016.
 */
public class FontListArrayAdapter extends ArrayAdapter<Font> {
    private final List<Font> mFontList;
    private Context mContext;

    static class ViewHolder {
        protected TextView mTextView;
    }

    public FontListArrayAdapter(Context context, List<Font> fontList) {
        super(context, R.layout.list_font_type_view, fontList);
        mContext = context;
        mFontList = fontList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;

        if (convertView == null) {
            LayoutInflater inflator = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflator.inflate(R.layout.list_font_type_view, null);
            final ViewHolder viewHolder = new ViewHolder();
            viewHolder.mTextView = (TextView) view.findViewById(R.id.detail_font_type);
            view.setTag(viewHolder);
        } else {
            view = convertView;
        }

        ViewHolder holder = (ViewHolder) view.getTag();
        holder.mTextView.setText(Common.convertStringToFont(
                holder.mTextView.getText().toString(), mFontList.get(position).getPath()));

        // Change size because Thabit font is too big and long
        if (mFontList.get(position).getName() == "Thabit") {
            holder.mTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        } else {
            holder.mTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
        }
        return view;
    }
}
