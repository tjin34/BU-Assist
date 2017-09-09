package net.bucssa.buassist.Ui.Fragments.Home.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import net.bucssa.buassist.Bean.Thread.TuiSong;
import net.bucssa.buassist.R;
import net.bucssa.buassist.Util.DateUtil;
import net.bucssa.buassist.Util.PicassoRoundCornerTrans;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by KimuraShin on 17/7/25.
 */

public class NewsRecyclerAdapter extends RecyclerView.Adapter<NewsRecyclerAdapter.MyViewHolder>{

    private Context mContext;
    private List<TuiSong> mDatas = new ArrayList<>();

    public NewsRecyclerAdapter(Context context, List<TuiSong> data) {
        this.mContext = context;
        this.mDatas = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder;
//        if (viewType == 1) {
//            holder = new MyViewHolder(LayoutInflater.from(
//                    mContext).inflate(R.layout.item_news_recycler_top, parent,
//                    false));
//        } else {
//            holder = new MyViewHolder(LayoutInflater.from(
//                    mContext).inflate(R.layout.item_recycler_view_news, parent,
//                    false));
//        }
        holder = new MyViewHolder(LayoutInflater.from(
                mContext).inflate(R.layout.item_news_recyclerview, parent,
                false));
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.title.setText(mDatas.get(position).getSubject());
        holder.time.setText(DateUtil.stampToDate(mDatas.get(position).getDateline()));
        holder.author.setText(mDatas.get(position).getAuthor());
        Picasso.with(mContext)
                .load("http://bucssa.net/"+ mDatas.get(position).getCoverpath())
                .error(R.drawable.bucssa)
                .transform(new PicassoRoundCornerTrans(mContext, 5))
                .into(holder.news_photo);
        final int tid = mDatas.get(position).getTid();
        holder.topbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRecyclerItemClickListener.onTopbarClick(tid);
            }
        });
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
    public int getItemViewType(int position) {
        if (position == 1 || position == 0) {
            return 1;
        } else {
            return 2;
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder {


        TextView title;
        TextView author;
        TextView time;
        ImageView news_photo;
        LinearLayout topbar;

        public MyViewHolder(View view) {
            super(view);

            title = (TextView) view.findViewById(R.id.tv_news_title);
            time = (TextView) view.findViewById(R.id.tv_time);
            author = (TextView) view.findViewById(R.id.tv_author);
            news_photo = (ImageView) view.findViewById(R.id.iv_news_photo);
            topbar = (LinearLayout) view.findViewById(R.id.rootView);
        }
    }

    public interface OnRecyclerItemClickListener{
        void onTopbarClick(int tid);
        void onCommentClick(int tid);
        void onLikeClick(int tid);
    }

    private OnRecyclerItemClickListener onRecyclerItemClickListener;

    public void setOnRecyclerItemClickListener(OnRecyclerItemClickListener listener) {
        this.onRecyclerItemClickListener = listener;
    }
    
}
