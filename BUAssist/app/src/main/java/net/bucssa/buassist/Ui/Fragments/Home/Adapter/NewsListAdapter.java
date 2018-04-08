package net.bucssa.buassist.Ui.Fragments.Home.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.bucssa.buassist.Bean.Thread.TuiSong;
import net.bucssa.buassist.R;
import net.bucssa.buassist.Ui.News.WebPageActivity;
import net.bucssa.buassist.Util.DateUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shinji on 2018/4/7.
 */

public class NewsListAdapter extends BaseAdapter{

    private List<TuiSong> mData = new ArrayList<>();
    private Context mContext;

    public NewsListAdapter(Context context, List<TuiSong> data) {
        this.mContext = context;
        this.mData = data;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    public void clearData() {
        this.mData.clear();
        notifyDataSetChanged();
    }

    public void addItems(List<TuiSong> datas, int position) {
        for (int i = 0; i < datas.size(); i++) {
            addItem(datas.get(i), position);
            position+=1;
        }
    }

    //添加一个item
    public void addItem(TuiSong data, int position) {
        mData.add(position, data);
        notifyDataSetChanged();
    }

    //删除一个item
    public void removeItem(final int position) {
        mData.remove(position);
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final TuiSong tuiSong = mData.get(position);
        final ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_news_recyclerview_simple, parent,false);
            viewHolder.tv_title = (TextView) convertView.findViewById(R.id.tv_news_title);
            viewHolder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            viewHolder.rootView = (RelativeLayout) convertView.findViewById(R.id.rootView);
            viewHolder.iv_like = (ImageView) convertView.findViewById(R.id.iv_like);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tv_title.setText(tuiSong.getSubject());
        viewHolder.tv_time.setText(DateUtil.stampToDate(tuiSong.getDateline()));
        viewHolder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ToastUtils.showToast(mContext, tuiSong.getAuthor());
                Intent intent = new Intent(mContext, WebPageActivity.class);
                intent.putExtra("tid", tuiSong.getTid());
                ((Activity) mContext).startActivity(intent);
            }
        });
        if (tuiSong.isIsCollected()) {
            viewHolder.iv_like.setSelected(true);
        } else {
            viewHolder.iv_like.setSelected(false);
        }
        viewHolder.iv_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewHolder.iv_like.isSelected()) {
                    onRecyclerItemClickListener.onDelCollection(tuiSong);
                    viewHolder.iv_like.setSelected(false);
                } else {
                    onRecyclerItemClickListener.onAddCollection(tuiSong);
                    viewHolder.iv_like.setSelected(true);

                }
            }
        });

        return convertView;
    }

    private class ViewHolder {
        TextView tv_title;
        TextView tv_time;
        ImageView iv_like;
        RelativeLayout rootView;
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
