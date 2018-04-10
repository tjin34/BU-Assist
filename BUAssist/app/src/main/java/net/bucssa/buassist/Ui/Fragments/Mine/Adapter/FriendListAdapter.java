package net.bucssa.buassist.Ui.Fragments.Mine.Adapter;

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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import net.bucssa.buassist.Bean.Friend.Friend;
import net.bucssa.buassist.R;
import net.bucssa.buassist.Ui.Fragments.Mine.OtherProfileActivity;
import net.bucssa.buassist.Util.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.http.POST;


/**
 * Created by Shinji on 2018/4/9.
 */

public class FriendListAdapter extends BaseAdapter {

    private Context mContext;
    private List<Friend> mData = new ArrayList<>();
    private List<Boolean> mCheck = new ArrayList<>();

    public  FriendListAdapter(Context context, List<Friend> data) {
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


    public List<Friend> getData() {
        return mData;
    }

    public void clearData() {
        this.mData.clear();
        notifyDataSetChanged();
    }


    public void addItems(List<Friend> datas) {
        for (int i = 0; i < datas.size(); i++) {
            addItem(datas.get(i));
        }
    }

    //添加一个item
    public void addItem(Friend data) {
        mData.add(data);
        notifyDataSetChanged();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Friend friend = mData.get(position);
        final ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_friend, parent,false);
            viewHolder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
            viewHolder.rootView = (RelativeLayout) convertView.findViewById(R.id.rootView);
            viewHolder.ivAvatar = (ImageView) convertView.findViewById(R.id.iv_avatar);
            viewHolder.ivCheck = (ImageView) convertView.findViewById(R.id.ivCheck);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tvName.setText(friend.getUsername());
        viewHolder.ivCheck.setVisibility(View.VISIBLE);
        viewHolder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder.ivCheck.setSelected(!viewHolder.ivCheck.isSelected());
                onFriendSelectedListener.onFriendSelected(friend);
            }
        });
        Glide.with(mContext)
                .asBitmap()
                .load(friend.getAvatar())
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.DATA))
                .into(viewHolder.ivAvatar);
        viewHolder.ivAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, OtherProfileActivity.class);
                intent.putExtra("OtherId",friend.getFriendid());
                ((Activity)mContext).startActivity(intent);
            }
        });
        return convertView;
    }

    private class ViewHolder {
        TextView tvName;
        ImageView ivAvatar;
        RelativeLayout rootView;
        ImageView ivCheck;
    }

    public interface onFriendSelectedListener{
        void onFriendSelected(Friend friend);
    }

    private onFriendSelectedListener onFriendSelectedListener;

    public void setOnFriendSelectedListener(FriendListAdapter.onFriendSelectedListener onFriendSelectedListener) {
        this.onFriendSelectedListener = onFriendSelectedListener;
    }
}
