package net.bucssa.buassist.Ui.Classmates.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import net.bucssa.buassist.Bean.Classmate.Post;
import net.bucssa.buassist.R;
import net.bucssa.buassist.Util.DateUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by KimuraShin on 17/7/30.
 */

public class PostListAdapter extends BaseAdapter {

    private Context mContext;
    private List<Post> mDatas = new ArrayList<>();
    private LayoutInflater mInflater;

    public PostListAdapter(Context context, List<Post> data) {
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
        Post data = mDatas.get(position);
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_post_list, null);
            viewHolder = new ViewHolder();
            viewHolder.author = (TextView) convertView.findViewById(R.id.tv_creator);
            viewHolder.title = (TextView) convertView.findViewById(R.id.tv_title);
            viewHolder.classCode = (TextView) convertView.findViewById(R.id.tv_classCode);
            viewHolder.content = (TextView) convertView.findViewById(R.id.tv_content);
            viewHolder.isHot = (TextView) convertView.findViewById(R.id.isHot);
            viewHolder.time = (TextView) convertView.findViewById(R.id.tv_time);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.author.setText(data.getAuthorName());
        viewHolder.title.setText(data.getSubject());
        viewHolder.classCode.setText(data.getClassId());
        viewHolder.content.setText(data.getContent());

        viewHolder.isHot.setVisibility(View.GONE);
        if (data.getComment() > 0)
            viewHolder.isHot.setVisibility(View.VISIBLE);

        viewHolder.time.setText(String.valueOf(data.getDateline()));

        return convertView;
    }

    private class ViewHolder{
        TextView author;
        TextView title;
        TextView classCode;
        TextView content;
        TextView isHot;
        TextView time;
    }
}
