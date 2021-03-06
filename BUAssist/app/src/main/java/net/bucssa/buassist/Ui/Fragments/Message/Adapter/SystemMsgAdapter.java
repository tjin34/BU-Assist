package net.bucssa.buassist.Ui.Fragments.Message.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import net.bucssa.buassist.Bean.Message.SystemNotification;
import net.bucssa.buassist.R;
import net.bucssa.buassist.Ui.Fragments.Mine.OtherProfileActivity;
import net.bucssa.buassist.Util.DateUtil;
import net.bucssa.buassist.Util.MD5;

import java.util.ArrayList;
import java.util.List;

import retrofit2.http.POST;

/**
 * Created by Shinji on 2017/12/22.
 */

public class SystemMsgAdapter extends BaseAdapter{

    private Context mContext;
    private List<SystemNotification> mData;

    public SystemMsgAdapter(Context context, List<SystemNotification> data) {
        this.mContext = context;
        this.mData = data;
    }

    public List<SystemNotification> getmData() {
        return mData;
    }

    public void clearData() {
        this.mData.clear();
        notifyDataSetChanged();
    }

    public void addData(int position, List<SystemNotification> data) {
        if (data != null && data.size() > 0) {
            mData.addAll(data);
            notifyDataSetChanged();
        }
    }
    public void addData(List<SystemNotification> data) {
        addData(0, data);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        return mData.get(position).getCategory() == 1 ? 1 : 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final SystemNotification data = mData.get(position);
        ViewHolderRequest viewHolderRequest;
        ViewHolderMsg viewHolderMsg;
        switch (getItemViewType(position)) {
            case 0:
                if (convertView == null) {
                    viewHolderRequest = new ViewHolderRequest();
                    convertView = LayoutInflater.from(mContext).inflate(R.layout.item_system_request, null);
                    viewHolderRequest.ivAvatar = (ImageView) convertView.findViewById(R.id.iv_avatar);
                    viewHolderRequest.tvTitle = (TextView) convertView.findViewById(R.id.tv_msg_type);
                    viewHolderRequest.tvContent = (TextView) convertView.findViewById(R.id.tv_message);
                    viewHolderRequest.tvDateline = (TextView) convertView.findViewById(R.id.tv_dateline);
                    viewHolderRequest.rootView = (LinearLayout) convertView.findViewById(R.id.rootView);
                    convertView.setTag(viewHolderRequest);
                } else {
                    viewHolderRequest = (ViewHolderRequest) convertView.getTag();
                }
                Glide.with(mContext)
                        .asBitmap()
                        .load(data.getAvatar())
                        .into(viewHolderRequest.ivAvatar);
                viewHolderRequest.ivAvatar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, OtherProfileActivity.class);
                        intent.putExtra("OtherId",data.getFrom_id());
                        ((Activity)mContext).startActivity(intent);
                    }
                });
                viewHolderRequest.tvTitle.setText(data.getAuthor());
                viewHolderRequest.tvContent.setText(data.getFrom_type());
                viewHolderRequest.tvDateline.setText(DateUtil.dateToOutput(data.getDateline()));

                viewHolderRequest.rootView.setSelected(data.getIs_new() == 1);
                viewHolderRequest.rootView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        v.setSelected(false);
                        onSystemRequestClickListener.OnSystemRequestClick(data);
                    }
                });

                break;
            case 1:
                if (convertView == null) {
                    viewHolderMsg = new ViewHolderMsg();
                    convertView = LayoutInflater.from(mContext).inflate(R.layout.item_system_message, null);
                    viewHolderMsg.tvContent = (TextView) convertView.findViewById(R.id.tv_message);
                    viewHolderMsg.tvDateline = (TextView) convertView.findViewById(R.id.tv_dateline);
                    viewHolderMsg.rootView = (LinearLayout) convertView.findViewById(R.id.rootView);
                    convertView.setTag(viewHolderMsg);
                } else {
                    viewHolderMsg = (ViewHolderMsg) convertView.getTag();
                }
                viewHolderMsg.tvContent.setText(data.getContent());
                viewHolderMsg.tvDateline.setText(DateUtil.dateToOutput(data.getDateline()));
                viewHolderMsg.rootView.setSelected(data.getIs_new() == 1);
                break;
        }

        return convertView;
    }

    private class ViewHolderRequest{
        ImageView ivAvatar;
        TextView tvTitle;
        TextView tvContent;
        TextView tvDateline;
        LinearLayout rootView;
    }

    private class ViewHolderMsg{
        TextView tvContent;
        TextView tvDateline;
        LinearLayout rootView;
    }


    public interface OnSystemRequestClickListener {
        void OnSystemRequestClick(SystemNotification systemNotification);
    }

    private OnSystemRequestClickListener onSystemRequestClickListener;

    public void setOnSystemRequestClickListener(OnSystemRequestClickListener onSystemRequestClickListener) {
        this.onSystemRequestClickListener = onSystemRequestClickListener;
    }
}
