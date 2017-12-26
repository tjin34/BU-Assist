package net.bucssa.buassist.Ui.Fragments.Message.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import net.bucssa.buassist.Bean.Message.SystemNotification;
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
        return super.getItemViewType(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }




}
