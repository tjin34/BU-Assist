package net.bucssa.buassist.Ui.Classmates.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.bucssa.buassist.Bean.Temp.Post;
import net.bucssa.buassist.R;

import java.util.List;

/**
 * Created by KimuraShin on 17/7/24.
 */

public  class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{

    private List<Post> datas;

    public static interface OnRecyclerViewItemClickListener {
        void onItemClick(View view);
    }
    private OnRecyclerViewItemClickListener mOnItemClickListener = null;
    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        mOnItemClickListener = listener;
    }
    public RecyclerViewAdapter(List<Post> datas) {
        this.datas=datas;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post_list,parent,false);
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
        Post data = datas.get(position);

        viewHolder.author.setText(data.getAuthor());
        viewHolder.title.setText(data.getTitle());
        viewHolder.classCode.setText(data.getPostClass());
        viewHolder.content.setText(data.getContent());

        viewHolder.isHot.setVisibility(View.GONE);
        if (data.getIsHot() == 1)
            viewHolder.isHot.setVisibility(View.VISIBLE);

        viewHolder.time.setText(data.getTime());
    }

    class ViewHolder extends RecyclerView.ViewHolder
    {

        TextView author;
        TextView title;
        TextView classCode;
        TextView content;
        TextView isHot;
        TextView time;
        public ViewHolder(View convertView){
            super(convertView);
            author = (TextView) convertView.findViewById(R.id.tv_creator);
            title = (TextView) convertView.findViewById(R.id.tv_title);
            classCode = (TextView) convertView.findViewById(R.id.tv_classCode);
            content = (TextView) convertView.findViewById(R.id.tv_content);
            isHot = (TextView) convertView.findViewById(R.id.isHot);
            time = (TextView) convertView.findViewById(R.id.tv_time);
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