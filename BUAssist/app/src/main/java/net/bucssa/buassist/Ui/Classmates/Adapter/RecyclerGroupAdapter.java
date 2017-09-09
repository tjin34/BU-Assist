package net.bucssa.buassist.Ui.Classmates.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.bucssa.buassist.R;
import net.bucssa.buassist.Ui.Classmates.GroupDetailActivity;

import java.util.List;

/**
 * Created by KimuraShin on 17/7/24.
 */

public  class RecyclerGroupAdapter extends RecyclerView.Adapter<RecyclerGroupAdapter.ViewHolder>{

    private Context mContext;
    private List<String> datas;

    public static interface OnRecyclerViewItemClickListener {
        void onItemClick(View view);
    }
    private OnRecyclerViewItemClickListener mOnItemClickListener = null;
    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        mOnItemClickListener = listener;
    }
    public RecyclerGroupAdapter(Context context, List<String> datas) {
        this.mContext = context;
        this.datas=datas;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_group_list,parent,false);
//        v.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//                mOnItemClickListener.onItemClick(v);
//            }
//        });
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holderView, int position) {
        final String data = datas.get(position);
        holderView.name.setText(data);
        holderView.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, GroupDetailActivity.class);
                intent.putExtra("groupName", data);
                ((Activity) mContext).startActivity(intent);
            }
        });
    }

    class ViewHolder extends RecyclerView.ViewHolder
    {
        private LinearLayout rootView;
        private TextView name;
        public ViewHolder(View view){
            super(view);
            rootView = (LinearLayout) view.findViewById(R.id.rootView);
            name = (TextView) view.findViewById(R.id.tv_groupName);
        }
    }
    @Override
    public int getItemCount()
    {
        return datas.size();
    }
}