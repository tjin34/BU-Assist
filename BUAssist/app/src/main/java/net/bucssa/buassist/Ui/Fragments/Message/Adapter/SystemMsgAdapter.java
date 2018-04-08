package net.bucssa.buassist.Ui.Fragments.Message.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import net.bucssa.buassist.Bean.Message.SystemNotification;
import net.bucssa.buassist.R;
import net.bucssa.buassist.Util.DateUtil;
import net.bucssa.buassist.Util.MD5;

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
                    convertView.setTag(viewHolderRequest);
                } else {
                    viewHolderRequest = (ViewHolderRequest) convertView.getTag();
                }
                Glide.with(mContext)
                        .asBitmap()
                        .load(data.getAvatar())
                        .into(viewHolderRequest.ivAvatar);

                viewHolderRequest.tvTitle.setText(data.getFrom_type());
                viewHolderRequest.tvContent.setText(data.getContent());
                viewHolderRequest.tvDateline.setText(DateUtil.dateToOutput(data.getDateline()));
                break;
            case 1:
                if (convertView == null) {
                    viewHolderMsg = new ViewHolderMsg();
                    convertView = LayoutInflater.from(mContext).inflate(R.layout.item_system_message, null);
                    viewHolderMsg.tvContent = (TextView) convertView.findViewById(R.id.tv_message);
                    viewHolderMsg.tvDateline = (TextView) convertView.findViewById(R.id.tv_dateline);
                    convertView.setTag(viewHolderMsg);
                } else {
                    viewHolderMsg = (ViewHolderMsg) convertView.getTag();
                }
                viewHolderMsg.tvContent.setText(data.getContent());
                viewHolderMsg.tvDateline.setText(DateUtil.dateToOutput(data.getDateline()));
                break;
        }

        return convertView;
    }

    private class ViewHolderRequest{
        ImageView ivAvatar;
        TextView tvTitle;
        TextView tvContent;
        TextView tvDateline;
    }

    private class ViewHolderMsg{
        TextView tvContent;
        TextView tvDateline;
    }




}
