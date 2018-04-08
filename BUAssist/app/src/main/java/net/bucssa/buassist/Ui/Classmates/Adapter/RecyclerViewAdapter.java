package net.bucssa.buassist.Ui.Classmates.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import net.bucssa.buassist.Bean.Classmate.Post;
import net.bucssa.buassist.R;
import net.bucssa.buassist.Ui.Classmates.PostDetailActivity;
import net.bucssa.buassist.Util.DateUtil;

import java.util.List;

/**
 * Created by KimuraShin on 17/7/24.
 */

public  class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{

    private List<Post> datas;
    private Context context;

    public static interface OnRecyclerViewItemClickListener {
        void onItemClick(View view);
    }
    private OnRecyclerViewItemClickListener mOnItemClickListener = null;
    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        mOnItemClickListener = listener;
    }
    public RecyclerViewAdapter(Context context, List<Post> datas) {
        this.context = context;
        this.datas=datas;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post_list_final,parent,false);
//        v.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//                mOnItemClickListener.onItemClick(v);
//            }
//        });
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        final Post data = datas.get(position);

        Glide.with(context)
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
                Intent intent = new Intent(context, PostDetailActivity.class);
                intent.putExtra("Post",data);
                ((Activity) context).startActivity(intent);
            }
        });


    }

    class ViewHolder extends RecyclerView.ViewHolder
    {
        LinearLayout rootView;
        ImageView ivProfile;
        TextView tvCreator;
        TextView tvTitle;
        TextView tvContent;
        TextView tvComment;
        TextView tvTime;
        ImageView ivComment;

        public ViewHolder(View convertView){
            super(convertView);
            rootView = (LinearLayout) convertView.findViewById(R.id.rootView);
            ivProfile = (ImageView) convertView.findViewById(R.id.ivProfile);
            tvCreator = (TextView) convertView.findViewById(R.id.tvCreator);
            tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
            tvComment = (TextView) convertView.findViewById(R.id.tvComment);
            tvContent = (TextView) convertView.findViewById(R.id.tvContent);
            ivComment = (ImageView) convertView.findViewById(R.id.ivComment);
            tvTime = (TextView) convertView.findViewById(R.id.tvTime);
        }
    }
    @Override
    public int getItemCount()
    {
        return datas.size();
    }

    public void addItem(Post post, int position) {
        datas.add(post);
        notifyItemInserted(position);
    }

    public void removeItem(final int position) {
        datas.remove(position);
        notifyItemRemoved(position);
    }
}