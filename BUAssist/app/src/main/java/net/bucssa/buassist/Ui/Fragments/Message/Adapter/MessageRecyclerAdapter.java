package net.bucssa.buassist.Ui.Fragments.Message.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import net.bucssa.buassist.Bean.Message.Message;
import net.bucssa.buassist.R;
import net.bucssa.buassist.UserSingleton;
import net.bucssa.buassist.Util.DateUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by KimuraShin on 17/7/27.
 */

public class MessageRecyclerAdapter extends RecyclerView.Adapter<MessageRecyclerAdapter.ViewHolder> {

    private Context mContext;
    private List<Message> mDatas = new ArrayList<>();


    public MessageRecyclerAdapter(Context context, List<Message> data) {
        this.mContext = context;
        this.mDatas = data;
    }

    public List<Message> getmDatas() {
        return mDatas;
    }

    public void clearData() {
        this.mDatas.clear();
        notifyDataSetChanged();
    }


    public void addItems(List<Message> datas, int position) {
        for (int i = 0; i < datas.size(); i++) {
            addItem(datas.get(i), position);
            position+=1;
        }
    }

    //添加一个item
    public void addItem(Message data, int position) {
        mDatas.add(position, data);
        notifyItemInserted(position);
    }

    //删除一个item
    public void removeItem(final int position) {
        mDatas.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (mDatas.get(position).getAuthorid() == UserSingleton.USERINFO.getUid()) {
            return 1;
        } else {
            return 2;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder viewHolder = null;
        switch (viewType) {
            case 1:
                viewHolder = new ViewHolder(LayoutInflater.from(
                        mContext).inflate(R.layout.item_message_from_me, parent,
                        false));
                break;
            case 2:
                viewHolder = new ViewHolder(LayoutInflater.from(
                        mContext).inflate(R.layout.item_message_from_others, parent,
                        false));
                break;
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        Message message = mDatas.get(position);
        viewHolder.tv_time.setText(DateUtil.stampToDate(message.getDateline()));
        viewHolder.tv_message.setText(message.getMessage());
        Picasso.with(mContext).load(message.getAuthoravatar()).error(R.drawable.profile_photo).into(viewHolder.iv_profile);
        viewHolder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListeners.OnItemClick(v);
            }
        });
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tv_time;
        private TextView tv_message;
        private ImageView iv_profile;
        private LinearLayout rootView;

        public ViewHolder(View convertView){
            super(convertView);
            tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            tv_message = (TextView) convertView.findViewById(R.id.tv_message);
            iv_profile = (ImageView) convertView.findViewById(R.id.iv_profile);
            rootView = (LinearLayout) convertView.findViewById(R.id.rootView);
        }

    }

    public interface OnClickListeners{
        void OnItemClick(View view);
    }

    private OnClickListeners onClickListeners;

    public void setOnClickListeners(OnClickListeners listeners) {
        this.onClickListeners = listeners;
    }

}
