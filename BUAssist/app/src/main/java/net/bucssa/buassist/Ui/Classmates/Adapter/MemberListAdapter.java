package net.bucssa.buassist.Ui.Classmates.Adapter;

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

import net.bucssa.buassist.Bean.Classmate.Member;
import net.bucssa.buassist.R;
import net.bucssa.buassist.Ui.Fragments.Mine.OtherProfileActivity;

import java.util.List;


/**
 * Created by Shinji on 2018/4/10.
 */

public class MemberListAdapter extends BaseAdapter {

    private Context mContext;
    private List<Member> mDatas;
    private LayoutInflater mInflater;
    private List<ImageView> mDataCheck;

    public MemberListAdapter(Context context, List<Member> data) {
        this.mContext = context;
        this.mDatas = data;
        this.mInflater = LayoutInflater.from(context);
    }

    public void clearData() {
        this.mDatas.clear();
        notifyDataSetChanged();
    }

    public List<ImageView> getCheckList() {
        return mDataCheck;
    }

    public void addData(int position, List<Member> data) {
        if (data != null && data.size() > 0) {
            mDatas.addAll(data);
            notifyDataSetChanged();
        }
    }
    public void addData(List<Member> data) {
        addData(0, data);
        notifyDataSetChanged();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Member member = mDatas.get(position);
        final ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_member, parent,false);
            viewHolder.tvNickname = (TextView) convertView.findViewById(R.id.tv_name);
            viewHolder.tvTag = (TextView) convertView.findViewById(R.id.tvTag);
            viewHolder.rootView = (RelativeLayout) convertView.findViewById(R.id.rootView);
            viewHolder.ivAvatar = (ImageView) convertView.findViewById(R.id.iv_avatar);
            viewHolder.ivCheck = (ImageView) convertView.findViewById(R.id.ivCheck);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        switch (member.getRole()) {
            case 3:
                viewHolder.tvTag.setText("创始人");
                viewHolder.tvTag.setBackground(((Activity)mContext).getResources().getDrawable(R.drawable.bg_area_wrap_layout_orange));
                break;
            case 2:
                viewHolder.tvTag.setText("管理员");
                viewHolder.tvTag.setBackground(((Activity)mContext).getResources().getDrawable(R.drawable.bg_area_wrap_layout_green));
                break;
            case 1:
                viewHolder.tvTag.setText("成员");
                viewHolder.tvTag.setBackground(((Activity)mContext).getResources().getDrawable(R.drawable.bg_area_wrap_layout_blue));
                break;
            default:
                break;
        }

        viewHolder.tvNickname.setText(member.getUsername());
        viewHolder.ivCheck.setVisibility(View.INVISIBLE);
//        mDataCheck.add((ImageView) viewHolder.ivCheck);
        viewHolder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder.ivCheck.setSelected(!viewHolder.ivCheck.isSelected());
                onClickEventListener.OnItemClick(member);
            }
        });
        viewHolder.rootView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                onClickEventListener.OnItemLongClick();
                return false;
            }
        });
        Glide.with(mContext)
                .asBitmap()
                .load(member.getAvatar())
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.DATA))
                .into(viewHolder.ivAvatar);
        viewHolder.ivAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, OtherProfileActivity.class);
                intent.putExtra("OtherId",member.getUserid());
                ((Activity)mContext).startActivity(intent);
            }
        });
        return convertView;
    }

    private class ViewHolder{
        RelativeLayout rootView;
        ImageView ivAvatar;
        TextView tvNickname;
        TextView tvTag;
        ImageView ivCheck;
    }

    public interface OnClickEventListener{
        void OnItemClick(Member member);
        void OnItemLongClick();
    }

    private OnClickEventListener onClickEventListener;

    public void setOnClickEventListener(OnClickEventListener onClickEventListener) {
        this.onClickEventListener = onClickEventListener;
    }
}
