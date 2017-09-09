package net.bucssa.buassist.Ui.Classmates.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.bucssa.buassist.R;
import net.bucssa.buassist.Ui.Classmates.GroupDetailActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by KimuraShin on 17/7/30.
 */

public class GroupsListAdapter extends BaseAdapter {

    private Context mContext;
    private List<String> mDatas = new ArrayList<>();
    private LayoutInflater mInflater;

    public GroupsListAdapter(Context context, List<String> data) {
        this.mContext = context;
        this.mDatas = data;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public long getItemId(int position) {
        return position;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        final String data = mDatas.get(position);
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_group_list, null);
            viewHolder = new ViewHolder();
            viewHolder.rootView = (LinearLayout) convertView.findViewById(R.id.rootView);
            viewHolder.groupName = (TextView) convertView.findViewById(R.id.tv_groupName);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.groupName.setText(data);
        viewHolder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, GroupDetailActivity.class);
                intent.putExtra("groupName", data);
                ((Activity) mContext).startActivity(intent);
            }
        });
        return convertView;
    }

    private class ViewHolder{
        LinearLayout rootView;
        TextView groupName;
    }
}
