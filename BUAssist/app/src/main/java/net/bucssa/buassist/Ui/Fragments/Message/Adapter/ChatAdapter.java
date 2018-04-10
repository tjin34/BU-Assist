package net.bucssa.buassist.Ui.Fragments.Message.Adapter;

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
import com.squareup.picasso.Picasso;

import net.bucssa.buassist.Bean.Message.Chat;
import net.bucssa.buassist.R;
import net.bucssa.buassist.Ui.Fragments.Message.ChatRoomActivity;
import net.bucssa.buassist.Ui.Fragments.Mine.OtherProfileActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by KimuraShin on 17/6/22.
 */

public class ChatAdapter extends BaseAdapter{

            private Context mContext;
            private List<Chat> mData = new ArrayList<>();
            private LayoutInflater mInflater;

    public ChatAdapter(Context context, List<Chat> data) {
                this.mContext = context;
                this.mData = data;
                this.mInflater = LayoutInflater.from(context);
    }

    public void clearData() {
        this.mData.clear();
        notifyDataSetChanged();
    }

    public void addData(int position, List<Chat> data) {
        if (data != null && data.size() > 0) {
            mData.addAll(data);
            notifyDataSetChanged();
        }
    }
    public void addData(List<Chat> data) {
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
    public View getView(int position, View convertView, ViewGroup parent) {
        final Chat chat = mData.get(position);
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_chat_listview, null);
            viewHolder.title = (TextView) convertView.findViewById(R.id.tv_message_title);
            viewHolder.lastMsg = (TextView) convertView.findViewById(R.id.tv_last_message);
            viewHolder.avatar = (ImageView) convertView.findViewById(R.id.iv_profile);
            viewHolder.hasNew = (ImageView) convertView.findViewById(R.id.iv_hasNew);
            viewHolder.rootView = (LinearLayout) convertView.findViewById(R.id.rootView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ChatRoomActivity.class);
                intent.putExtra("Chat", chat);
                ((Activity) mContext).startActivityForResult(intent, 101);
            }
        });
        if (chat.getMembers() > 2)
            viewHolder.title.setText(chat.getSubject());
        else
            viewHolder.title.setText(chat.getTousername());
        viewHolder.lastMsg.setText(chat.getSummary());
        Glide.with(mContext)
                .asBitmap()
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.DATA))
                .load(chat.getAvatar())
                .into(viewHolder.avatar);

        viewHolder.avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, OtherProfileActivity.class);
                intent.putExtra("OtherId",chat.getUid());
                ((Activity)mContext).startActivity(intent);
            }
        });
        if (chat.getHasnew()==1) {
            viewHolder.hasNew.setVisibility(View.VISIBLE);
        } else if (chat.getHasnew() == 0){
            viewHolder.hasNew.setVisibility(View.GONE);
        }

        return convertView;
    }

    private class ViewHolder{
        LinearLayout rootView;
        TextView lastMsg;
        TextView title;
        ImageView avatar;
        ImageView hasNew;
    }
}
