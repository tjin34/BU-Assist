package net.bucssa.buassist.Ui.Fragments.Explore.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.bucssa.buassist.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by KimuraShin on 17/8/17.
 */

public class ExploreAdapter extends RecyclerView.Adapter<ExploreAdapter.ViewHolder> {

    private Context mContext;
    private List<String> mDatas = new ArrayList<>();

    public ExploreAdapter(Context context, List<String> datas) {
        this.mContext = context;
        this.mDatas = datas;
    }


    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder viewHolder = new ViewHolder(LayoutInflater.from(mContext).
                inflate(R.layout.item_explore, parent, false));
        return viewHolder;
    }

    public List<String> getmDatas() {
        return this.mDatas;
    }

    public void clearData() {
        this.mDatas.clear();
        notifyDataSetChanged();
    }

    public void addItems(List<String> datas, int position) {
        for (int i = 0; i < datas.size(); i++) {
            addItem(datas.get(i), position);
            position+=1;
        }
    }

    //添加一个item
    public void addItem(String data, int position) {
        mDatas.add(position, data);
        notifyItemInserted(position);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }



    public class ViewHolder extends RecyclerView.ViewHolder{
        public ViewHolder(View view) {
            super(view);
        }
    }
}
