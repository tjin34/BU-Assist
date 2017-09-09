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

import com.squareup.picasso.Picasso;

import net.bucssa.buassist.Bean.Friend.Friend;
import net.bucssa.buassist.R;
import net.bucssa.buassist.Ui.News.WebPageActivity;
import net.bucssa.buassist.Util.DateUtil;
import net.bucssa.buassist.Util.ToastUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by KimuraShin on 17/8/22.
 */

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.ViewHolder>  {


    private Context mContext;
    private List<Friend> mDatas = new ArrayList<>();

    public  FriendAdapter(Context context, List<Friend> data) {
        this.mContext = context;
        this.mDatas = data;
    }

    public List<Friend> getmData() {
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

    public void addItems(List<Friend> datas, int position) {
        for (int i = 0; i < datas.size(); i++) {
            addItem(datas.get(i), position);
            position+=1;
        }
    }

    //添加一个item
    public void addItem(Friend data, int position) {
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
        final Friend friend = mDatas.get(position);
        holder.tv_name.setText(friend.getUsername());
        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showToast(mContext, friend.getComment());
//                Intent intent = new Intent(mContext, WebPageActivity.class);
//                intent.putExtra("tid", Friend.getTid());
//                ((Activity) mContext).startActivity(intent);
            }
        });
        Picasso.with(mContext).load(friend.getAvatar()).error(R.drawable.profile_photo).into(holder.iv_avatar);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_friend, parent,false));
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_name;
        ImageView iv_avatar;
        RelativeLayout rootView;
        public ViewHolder(View convertView) {
            super(convertView);
            tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            rootView = (RelativeLayout) convertView.findViewById(R.id.rootView);
            iv_avatar = (ImageView) convertView.findViewById(R.id.iv_avatar);
        }
    }
}
