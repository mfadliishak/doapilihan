package com.mfadli.doapilihan.adapter;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mfadli.doapilihan.R;
import com.mfadli.doapilihan.data.repo.DoaDataRepo;
import com.mfadli.doapilihan.model.DoaDetail;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by mfad on 22/03/2016.
 */
public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {

    private List<DoaDetail> mList;

    public MainAdapter() {
        DoaDataRepo doaDataRepo = new DoaDataRepo();
        mList = doaDataRepo.getAllDoa();
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
        holder.tvTitle.setText(doaDetail.getTitle().replace("\\n", "\n"));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @Nullable
        @Bind(R.id.main_title)
        TextView tvTitle;

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
