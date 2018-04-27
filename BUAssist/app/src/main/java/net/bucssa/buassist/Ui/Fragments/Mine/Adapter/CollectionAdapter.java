package net.bucssa.buassist.Ui.Fragments.Mine.Adapter;

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

import net.bucssa.buassist.Bean.Thread.Collection;
import net.bucssa.buassist.R;
import net.bucssa.buassist.Ui.News.WebPageActivity;
import net.bucssa.buassist.Util.DateUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by KimuraShin on 17/8/22.
 */

public class CollectionAdapter extends RecyclerView.Adapter<CollectionAdapter.ViewHolder>  {


    private Context mContext;
    private List<Collection> mDatas = new ArrayList<>();

    public  CollectionAdapter(Context context, List<Collection> data) {
        this.mContext = context;
        this.mDatas = data;
    }

    public List<Collection> getmData() {
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

    public void addItems(List<Collection> datas, int position) {
        for (int i = 0; i < datas.size(); i++) {
            addItem(datas.get(i), position);
            position+=1;
        }
    }

    //添加一个item
    public void addItem(Collection data, int position) {
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
        final Collection collection = mDatas.get(position);
        holder.tv_title.setText(collection.getSubject());
        holder.tv_time.setText(DateUtil.stampToDate(collection.getDateline()));
        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //                ToastUtils.showToast(mContext, Collection.getAuthor());
                Intent intent = new Intent(mContext, WebPageActivity.class);
                intent.putExtra("tid", collection.getTid());
                ((Activity) mContext).startActivity(intent);
            }
        });
        holder.iv_like.setSelected(true);
        holder.iv_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRecyclerItemClickListener.onDelCollection(collection);
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

        void onAddCollection(Collection Collection);

        void onDelCollection(Collection Collection);
    }

    private OnRecyclerItemClickListener onRecyclerItemClickListener;

    public void setOnRecyclerItemClickListener(OnRecyclerItemClickListener listener) {
        this.onRecyclerItemClickListener = listener;
    }
}