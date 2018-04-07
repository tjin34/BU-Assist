package net.bucssa.buassist.Ui.Classmates.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import net.bucssa.buassist.Bean.Classmate.Comment;
import net.bucssa.buassist.R;
import net.bucssa.buassist.Util.DateUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shinji on 2018/4/6.
 */

public class CommentListAdapter extends BaseAdapter {

    private List<Comment> mDatas = new ArrayList<>();
    private Context mContext;

    public CommentListAdapter(Context context, List<Comment> comments) {
        this.mContext = context;
        this.mDatas = comments;
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int i) {
        return mDatas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public void clear() {
        mDatas = new ArrayList<>();
        notifyDataSetChanged();
    }

    public void addData(Comment comments) {
        mDatas.add(comments);
        notifyDataSetChanged();
    }

    public void addDatas(List<Comment> comments) {
        for(int i = 0; i < comments.size(); i++) {
            addData(comments.get(i));
        }
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final Comment comment = mDatas.get(i);
        ViewHolder viewHolder;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.item_post_comment, null);
            viewHolder.ivProfile = (ImageView) view.findViewById(R.id.ivProfile);
            viewHolder.tvCreator = (TextView) view.findViewById(R.id.tvCreator);
            viewHolder.tvContent = (TextView) view.findViewById(R.id.tvContent);
            viewHolder.tvTime = (TextView) view.findViewById(R.id.tvTime);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        Glide.with(mContext)
                .load(comment.getAvatar())
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
                .into(viewHolder.ivProfile);
        viewHolder.tvCreator.setText(comment.getFromUsername());
        viewHolder.tvTime.setText(DateUtil.dateToOutput(comment.getDateline()));
        viewHolder.tvContent.setText(comment.getContent());
        return view;
    }


    private class ViewHolder {
        ImageView ivProfile;
        TextView tvCreator;
        TextView tvContent;
        TextView tvTime;
    }
}
