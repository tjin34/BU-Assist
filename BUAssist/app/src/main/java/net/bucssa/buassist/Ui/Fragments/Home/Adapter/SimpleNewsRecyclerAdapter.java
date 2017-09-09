package net.bucssa.buassist.Ui.Fragments.Home.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.bucssa.buassist.Bean.Thread.TuiSong;
import net.bucssa.buassist.R;
import net.bucssa.buassist.Ui.News.WebPageActivity;
import net.bucssa.buassist.Util.DateUtil;
import net.bucssa.buassist.Util.ToastUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by KimuraShin on 17/8/18.
 */

public class SimpleNewsRecyclerAdapter extends RecyclerView.Adapter<SimpleNewsRecyclerAdapter.ViewHolder>  {


    private Context mContext;
    private List<TuiSong> mDatas = new ArrayList<>();

    public  SimpleNewsRecyclerAdapter(Context context, List<TuiSong> data) {
        this.mContext = context;
        this.mDatas = data;
    }

    public List<TuiSong> getmData() {
        return mDatas;
    }

    public void clearData() {
        this.mDatas.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public void addItems(List<TuiSong> datas, int position) {
        for (int i = 0; i < datas.size(); i++) {
            addItem(datas.get(i), position);
            position+=1;
        }
    }

    //添加一个item
    public void addItem(TuiSong data, int position) {
        mDatas.add(position, data);
        notifyItemInserted(position);
    }

    //删除一个item
    public void removeItem(final int position) {
        mDatas.remove(position);
        notifyItemRemoved(position);
    }
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final TuiSong tuiSong = mDatas.get(position);
        holder.tv_title.setText(tuiSong.getSubject());
        holder.tv_time.setText(DateUtil.stampToDate(tuiSong.getDateline()));
        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ToastUtils.showToast(mContext, tuiSong.getAuthor());
                Intent intent = new Intent(mContext, WebPageActivity.class);
                intent.putExtra("tid", tuiSong.getTid());
                ((Activity) mContext).startActivity(intent);
            }
        });
        if (tuiSong.isIsCollected()) {
            holder.iv_like.setSelected(true);
        } else {
            holder.iv_like.setSelected(false);
        }
        holder.iv_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.iv_like.isSelected()) {
                    onRecyclerItemClickListener.onDelCollection(tuiSong);
                    holder.iv_like.setSelected(false);
                } else {
                    onRecyclerItemClickListener.onAddCollection(tuiSong);
                    holder.iv_like.setSelected(true);

                }
            }
        });
        
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_news_recyclerview_simple, parent,false));
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_title;
        TextView tv_time;
        ImageView iv_like;
        RelativeLayout rootView;
        public ViewHolder(View convertView) {
            super(convertView);
            tv_title = (TextView) convertView.findViewById(R.id.tv_news_title);
            tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            rootView = (RelativeLayout) convertView.findViewById(R.id.rootView);
            iv_like = (ImageView) convertView.findViewById(R.id.iv_like);
        }
    }



    public interface OnRecyclerItemClickListener{
        void onClick(String url);

        void onAddCollection(TuiSong tuiSong);

        void onDelCollection(TuiSong tuiSong);
    }

    private OnRecyclerItemClickListener onRecyclerItemClickListener;

    public void setOnRecyclerItemClickListener(OnRecyclerItemClickListener listener) {
        this.onRecyclerItemClickListener = listener;
    }
}
