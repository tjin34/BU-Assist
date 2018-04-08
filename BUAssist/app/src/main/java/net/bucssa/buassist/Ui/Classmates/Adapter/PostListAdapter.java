package net.bucssa.buassist.Ui.Classmates.Adapter;

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
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import net.bucssa.buassist.Bean.Classmate.Group;
import net.bucssa.buassist.Bean.Classmate.Post;
import net.bucssa.buassist.R;
import net.bucssa.buassist.Ui.Classmates.PostDetailActivity;
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

    public void clear() {
        mDatas = new ArrayList<>();
        notifyDataSetChanged();
    }

    public void addData(Post post) {
        mDatas.add(post);
        notifyDataSetChanged();
    }

    public void addDatas(List<Post> posts) {
        for(int i = 0; i < posts.size(); i++) {
            addData(posts.get(i));
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Post data = mDatas.get(position);
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_post_list_final, null);
            viewHolder = new ViewHolder();
            viewHolder.rootView = (LinearLayout) convertView.findViewById(R.id.rootView);
            viewHolder.ivProfile = (ImageView) convertView.findViewById(R.id.ivProfile);
            viewHolder.tvCreator = (TextView) convertView.findViewById(R.id.tvCreator);
            viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
            viewHolder.tvComment = (TextView) convertView.findViewById(R.id.tvComment);
            viewHolder.tvContent = (TextView) convertView.findViewById(R.id.tvContent);
            viewHolder.ivComment = (ImageView) convertView.findViewById(R.id.ivComment);
            viewHolder.tvTime = (TextView) convertView.findViewById(R.id.tvTime);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Glide.with(mContext)
                .asBitmap()
                .load(data.getAvatar())
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
                .into(viewHolder.ivProfile);
        viewHolder.tvCreator.setText(data.getAuthorName());
        viewHolder.tvTitle.setText(data.getSubject());
        viewHolder.tvContent.setText(data.getContent());
        viewHolder.tvComment.setText(String.valueOf(data.getComment()));


        viewHolder.ivComment.setSelected(false);
        if (data.getComment() > 5)
            viewHolder.ivComment.setSelected(false);

        viewHolder.tvTime.setText(DateUtil.dateToOutput(data.getDateline()));

        viewHolder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, PostDetailActivity.class);
                intent.putExtra("Post",data);
                ((Activity) mContext).startActivity(intent);
            }
        });

        return convertView;
    }

    private class ViewHolder{
        LinearLayout rootView;
        ImageView ivProfile;
        TextView tvCreator;
        TextView tvTitle;
        TextView tvContent;
        TextView tvComment;
        TextView tvTime;
        ImageView ivComment;
    }
}
