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

import net.bucssa.buassist.Bean.Classmate.Group;
import net.bucssa.buassist.R;
import net.bucssa.buassist.Ui.Classmates.Group.GroupDetailActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by KimuraShin on 17/7/30.
 */

public class GroupsListAdapter extends BaseAdapter {

    private Context mContext;
    private List<Group> mDatas = new ArrayList<>();
    private LayoutInflater mInflater;

    public GroupsListAdapter(Context context, List<Group> data) {
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

    public void clear() {
        mDatas = new ArrayList<>();
        notifyDataSetChanged();
    }

    public void addData(Group groups) {
        mDatas.add(groups);
        notifyDataSetChanged();
    }

    public void addDatas(List<Group> groups) {
        for(int i = 0; i < groups.size(); i++) {
            addData(groups.get(i));
        }
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Group data = mDatas.get(position);
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_group_list, null);
            viewHolder = new ViewHolder();
            viewHolder.rootView = (LinearLayout) convertView.findViewById(R.id.rootView);
            viewHolder.name = (TextView) convertView.findViewById(R.id.tv_groupName);
            viewHolder.description = (TextView) convertView.findViewById(R.id.tv_description);
            viewHolder.members = (TextView) convertView.findViewById(R.id.tv_member);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.name.setText(data.getGroupName());
        viewHolder.description.setText(data.getGroupIntro());
        viewHolder.members.setText(String.valueOf(data.getMembers()));
        viewHolder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, GroupDetailActivity.class);
                intent.putExtra("group", data);
                ((Activity) mContext).startActivity(intent);
            }
        });
        return convertView;
    }

    private class ViewHolder{
        private LinearLayout rootView;
        private TextView name;
        private TextView description;
        private TextView members;
    }
}
